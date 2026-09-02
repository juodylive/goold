package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AudioFeedbackEngine
import com.example.calibration.CalibrationManager
import com.example.calibration.CalibrationResult
import com.example.core.AudioFeedbackConfig
import com.example.core.DetectionMode
import com.example.core.ExpertSettings
import com.example.core.SensorReading
import com.example.core.SensorSource
import com.example.core.TargetClassification
import com.example.core.VibrationFeedbackConfig
import com.example.database.AppDatabase
import com.example.database.ScanLogEntity
import com.example.database.ScanLogRepository
import com.example.location.LocationProvider
import com.example.sensors.BluetoothMetalDetectorSensor
import com.example.sensors.ConnectionStatus
import com.example.sensors.MetalDetectorSensor
import com.example.sensors.PhoneMagnetometerSensor
import com.example.sensors.SimulatorMetalDetectorSensor
import com.example.sensors.UsbMetalDetectorSensor
import com.example.signal_processing.ProcessedSignal
import com.example.signal_processing.SignalProcessor
import com.example.vibration.VibrationManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedDeque

data class GraphPoint(
    val timestampMs: Long,
    val rawUt: Float,
    val filteredUt: Float,
    val baselineUt: Float,
    val thresholdUt: Float
)

class DetectorViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val context = application.applicationContext

    // Sensors
    val phoneSensor = PhoneMagnetometerSensor(context)
    val bleSensor = BluetoothMetalDetectorSensor(context)
    val usbSensor = UsbMetalDetectorSensor(context)
    val simSensor = SimulatorMetalDetectorSensor()

    private val _activeSensor = MutableStateFlow<MetalDetectorSensor>(phoneSensor)
    val activeSensor: StateFlow<MetalDetectorSensor> = _activeSensor.asStateFlow()

    // Core Processing & Engines
    val signalProcessor = SignalProcessor()
    val calibrationManager = CalibrationManager()
    val audioEngine = AudioFeedbackEngine()
    val vibrationManager = VibrationManager(context)
    val locationProvider = LocationProvider(context)
    private val database = AppDatabase.getDatabase(context)
    val repository = ScanLogRepository(database.scanLogDao())

    // UI States
    private val _isDetecting = MutableStateFlow(false)
    val isDetecting: StateFlow<Boolean> = _isDetecting.asStateFlow()

    private val _processedSignal = MutableStateFlow<ProcessedSignal>(
        ProcessedSignal(
            rawReading = SensorReading(magnitudeUt = 48.0f),
            rawMagnitudeUt = 48.0f,
            filteredMagnitudeUt = 48.0f,
            baselineUt = 48.0f,
            deltaUt = 0.0f,
            signalStrengthPct = 0.0f,
            signalStabilityPct = 100.0f,
            snrDb = 0.0f,
            detectionConfidencePct = 0.0f,
            isThresholdExceeded = false,
            effectiveThresholdUt = 6.0f,
            isPeakDetected = false,
            peakValueUt = 0.0f,
            classification = TargetClassification.Idle
        )
    )
    val processedSignal: StateFlow<ProcessedSignal> = _processedSignal.asStateFlow()

    private val _currentMode = MutableStateFlow(DetectionMode.QUICK_SCAN)
    val currentMode: StateFlow<DetectionMode> = _currentMode.asStateFlow()

    private val _expertSettings = MutableStateFlow(ExpertSettings())
    val expertSettings: StateFlow<ExpertSettings> = _expertSettings.asStateFlow()

    private val _audioConfig = MutableStateFlow(AudioFeedbackConfig())
    val audioConfig: StateFlow<AudioFeedbackConfig> = _audioConfig.asStateFlow()

    private val _vibrationConfig = MutableStateFlow(VibrationFeedbackConfig())
    val vibrationConfig: StateFlow<VibrationFeedbackConfig> = _vibrationConfig.asStateFlow()

    // Real-Time Scrolling Graph Buffer
    private val graphDeque = ConcurrentLinkedDeque<GraphPoint>()
    private val _graphPoints = MutableStateFlow<List<GraphPoint>>(emptyList())
    val graphPoints: StateFlow<List<GraphPoint>> = _graphPoints.asStateFlow()

    private val _graphTimeWindowSec = MutableStateFlow(30)
    val graphTimeWindowSec: StateFlow<Int> = _graphTimeWindowSec.asStateFlow()

    // Scan Session Stats
    private var detectionStartTimeMs: Long = 0L
    private var sessionPeakUt: Float = 0f

    // History Flow
    val allLogs: StateFlow<List<ScanLogEntity>> = repository.allLogs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private var sensorJob: Job? = null
    private var graphUpdateJob: Job? = null

    init {
        // Apply initial configurations
        audioEngine.config = _audioConfig.value
        vibrationManager.config = _vibrationConfig.value
        signalProcessor.currentMode = _currentMode.value
        signalProcessor.expertSettings = _expertSettings.value

        // Start graph update loop (runs at ~15-20Hz to keep UI silky smooth without CPU burn)
        graphUpdateJob = viewModelScope.launch {
            while (true) {
                val now = System.currentTimeMillis()
                val windowMs = _graphTimeWindowSec.value * 1000L
                val cutoff = now - windowMs

                // Prune old points
                while (graphDeque.isNotEmpty() && graphDeque.peekFirst()?.timestampMs ?: 0L < cutoff) {
                    graphDeque.pollFirst()
                }

                _graphPoints.value = graphDeque.toList()
                delay(60) // ~16 FPS graph updates
            }
        }

        // Start listening to the default sensor immediately
        startDetection()
    }

    fun selectSensor(source: SensorSource) {
        viewModelScope.launch {
            val wasDetecting = _isDetecting.value
            stopDetection()

            val newSensor: MetalDetectorSensor = when (source) {
                SensorSource.PHONE_MAGNETOMETER -> phoneSensor
                SensorSource.EXTERNAL_BLUETOOTH_BLE -> bleSensor
                SensorSource.EXTERNAL_USB_OTG -> usbSensor
                SensorSource.DEVELOPER_SIMULATION_MODE -> simSensor
            }
            _activeSensor.value = newSensor

            if (wasDetecting) {
                startDetection()
            }
        }
    }

    fun startDetection() {
        if (_isDetecting.value) return
        viewModelScope.launch {
            val sensor = _activeSensor.value
            val success = sensor.startListening()
            if (success || sensor.sensorSource == SensorSource.PHONE_MAGNETOMETER) {
                _isDetecting.value = true
                detectionStartTimeMs = System.currentTimeMillis()
                sessionPeakUt = 0f
                audioEngine.start()
                vibrationManager.start()
                listenToSensorFlow(sensor)
            }
        }
    }

    fun stopDetection() {
        _isDetecting.value = false
        sensorJob?.cancel()
        sensorJob = null
        audioEngine.stop()
        vibrationManager.stop()
        viewModelScope.launch {
            _activeSensor.value.stopListening()
        }
    }

    private fun listenToSensorFlow(sensor: MetalDetectorSensor) {
        sensorJob?.cancel()
        sensorJob = viewModelScope.launch {
            sensor.rawReadings.collect { reading ->
                // Pass to Calibration Manager if active
                calibrationManager.onNewReading(reading)

                // Process Signal
                val processed = signalProcessor.process(reading)
                _processedSignal.value = processed

                // Update session peak
                if (processed.rawMagnitudeUt > sessionPeakUt) {
                    sessionPeakUt = processed.rawMagnitudeUt
                }

                // Feed Audio & Vibration
                audioEngine.updateSignal(processed)
                vibrationManager.onSignalUpdate(processed)

                // Append to Graph Deque
                graphDeque.addLast(
                    GraphPoint(
                        timestampMs = reading.timestamp,
                        rawUt = processed.rawMagnitudeUt,
                        filteredUt = processed.filteredMagnitudeUt,
                        baselineUt = processed.baselineUt,
                        thresholdUt = processed.baselineUt + processed.effectiveThresholdUt
                    )
                )
            }
        }
    }

    fun resetBaseline() {
        signalProcessor.resetBaselineToCurrent()
    }

    fun setDetectionMode(mode: DetectionMode) {
        _currentMode.value = mode
        signalProcessor.currentMode = mode
        _activeSensor.value.setSamplingRate(mode.sampleRateHz)
    }

    fun setGraphTimeWindow(seconds: Int) {
        _graphTimeWindowSec.value = seconds
    }

    fun updateAudioConfig(newConfig: AudioFeedbackConfig) {
        _audioConfig.value = newConfig
        audioEngine.config = newConfig
    }

    fun updateVibrationConfig(newConfig: VibrationFeedbackConfig) {
        _vibrationConfig.value = newConfig
        vibrationManager.config = newConfig
    }

    fun updateExpertSettings(settings: ExpertSettings) {
        _expertSettings.value = settings
        signalProcessor.expertSettings = settings
    }

    fun performGuidedCalibration(onCompleted: () -> Unit = {}) {
        calibrationManager.startGuidedCalibration { result: CalibrationResult ->
            signalProcessor.setBaseline(result.baselineUt)
            signalProcessor.calibratedNoiseFloor = result.noiseFloorUt
            onCompleted()
        }
    }

    fun saveCurrentDetectionLog(userNotes: String = "") {
        viewModelScope.launch {
            val signal = _processedSignal.value
            val durationSec = ((System.currentTimeMillis() - detectionStartTimeMs) / 1000L).toInt().coerceAtLeast(1)
            val gps = locationProvider.getCurrentLocation()

            val log = ScanLogEntity(
                timestamp = System.currentTimeMillis(),
                peakStrengthUt = if (sessionPeakUt > 0) sessionPeakUt else signal.rawMagnitudeUt,
                deltaUt = signal.deltaUt,
                sensorType = _activeSensor.value.sensorSource.name,
                targetClassification = signal.classification.title,
                estimatedMaterial = signal.classification.subtitle,
                durationSeconds = durationSec,
                latitude = gps?.latitude,
                longitude = gps?.longitude,
                altitude = gps?.altitude,
                userNotes = userNotes,
                modeName = _currentMode.value.title
            )
            repository.insertLog(log)
        }
    }

    fun deleteLog(id: Long) {
        viewModelScope.launch {
            repository.deleteLogById(id)
        }
    }

    fun clearAllLogs() {
        viewModelScope.launch {
            repository.clearAll()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopDetection()
        graphUpdateJob?.cancel()
    }
}
