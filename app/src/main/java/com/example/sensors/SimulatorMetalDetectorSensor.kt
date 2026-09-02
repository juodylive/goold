package com.example.sensors

import com.example.core.SensorReading
import com.example.core.SensorSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random

class SimulatorMetalDetectorSensor : MetalDetectorSensor {

    override val sensorId: String = "SIMULATOR_TEST_SENSOR"
    override val sensorName: String = "Developer Algorithm Test Mode"
    override val sensorSource: SensorSource = SensorSource.DEVELOPER_SIMULATION_MODE

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    override val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private val _statusMessage = MutableStateFlow("TEST MODE (Synthetic Data Generator)")
    override val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

    private val _readingsFlow = MutableSharedFlow<SensorReading>(
        replay = 1,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val rawReadings: Flow<SensorReading> = _readingsFlow.asSharedFlow()

    private var simJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private var samplingRateHz: Int = 50

    var simulateExternalVdi: Boolean = false
    var simulatedMetalType: SimulatedTarget = SimulatedTarget.RANDOM_CYCLE

    enum class SimulatedTarget {
        RANDOM_CYCLE,
        FERROUS_NAIL,
        HIGH_CONDUCTIVITY_TARGET,
        WEAK_ANOMALY
    }

    override suspend fun startListening(): Boolean {
        simJob?.cancel()
        _isConnected.value = true
        _connectionStatus.value = ConnectionStatus.CONNECTED
        _statusMessage.value = "TEST MODE ACTIVE — Synthetic Signal"

        simJob = scope.launch {
            var tick = 0.0
            val baseline = 48.0 // Typical earth geomagnetic field in µT
            
            while (isActive && _isConnected.value) {
                tick += 0.05
                val noise = (Random.nextFloat() - 0.5f) * 0.8f
                
                // Anomaly pulse every 8 seconds
                val pulse = if ((tick % 10.0) in 3.0..6.0) {
                    val p = sin((tick % 10.0 - 3.0) * Math.PI / 3.0).toFloat()
                    p * 45f // +45 µT anomaly
                } else {
                    0f
                }

                val currentMag = (baseline + noise + pulse).toFloat()

                val vdi: Float? = if (simulateExternalVdi && pulse > 5f) {
                    when (simulatedMetalType) {
                        SimulatedTarget.FERROUS_NAIL -> 15f
                        SimulatedTarget.HIGH_CONDUCTIVITY_TARGET -> 82f
                        SimulatedTarget.WEAK_ANOMALY -> 35f
                        SimulatedTarget.RANDOM_CYCLE -> (20f + (tick.toInt() * 15) % 75).toFloat()
                    }
                } else null

                val phase: Float? = if (simulateExternalVdi && pulse > 5f) {
                    when (simulatedMetalType) {
                        SimulatedTarget.FERROUS_NAIL -> 12f
                        SimulatedTarget.HIGH_CONDUCTIVITY_TARGET -> 78f
                        else -> 45f
                    }
                } else null

                val reading = SensorReading(
                    timestamp = System.currentTimeMillis(),
                    magnitudeUt = currentMag,
                    xUt = 18f + noise * 0.5f,
                    yUt = 24f + noise * 0.5f,
                    zUt = (currentMag * 0.8f),
                    accuracy = 3,
                    source = SensorSource.DEVELOPER_SIMULATION_MODE,
                    frequencyKhz = if (simulateExternalVdi) 18.75f else null,
                    conductivityIndex = vdi,
                    phaseAngleDeg = phase,
                    targetId = vdi?.toInt(),
                    batteryLevelPct = 94
                )

                _readingsFlow.tryEmit(reading)
                delay((1000L / samplingRateHz).coerceAtLeast(10L))
            }
        }
        return true
    }

    override suspend fun stopListening() {
        simJob?.cancel()
        simJob = null
        _isConnected.value = false
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
        _statusMessage.value = "Test simulation stopped"
    }

    override fun setSamplingRate(hz: Int) {
        samplingRateHz = hz.coerceIn(10, 100)
    }
}
