package com.example.sensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.example.core.SensorReading
import com.example.core.SensorSource
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.sqrt

class PhoneMagnetometerSensor(
    context: Context
) : MetalDetectorSensor, SensorEventListener {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
    private val magneticSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    override val sensorId: String = "PHONE_INTERNAL_MAGNETOMETER"
    override val sensorName: String = magneticSensor?.name ?: "Android Device Magnetometer"
    override val sensorSource: SensorSource = SensorSource.PHONE_MAGNETOMETER

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _connectionStatus = MutableStateFlow(
        if (magneticSensor != null) ConnectionStatus.DISCONNECTED else ConnectionStatus.ERROR_UNAVAILABLE
    )
    override val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private val _statusMessage = MutableStateFlow(
        if (magneticSensor != null) "Internal magnetometer ready" else "No magnetometer hardware found"
    )
    override val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

    private val _readingsFlow = MutableSharedFlow<SensorReading>(
        replay = 1,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val rawReadings: Flow<SensorReading> = _readingsFlow.asSharedFlow()

    private var samplingPeriodUs: Int = SensorManager.SENSOR_DELAY_GAME // ~20ms = 50Hz

    val hasMagnetometer: Boolean = magneticSensor != null
    val sensorVendor: String = magneticSensor?.vendor ?: "Unknown"
    val sensorPowerMa: Float = magneticSensor?.power ?: 0f
    val sensorResolutionUt: Float = magneticSensor?.resolution ?: 0.1f
    val maxRangeUt: Float = magneticSensor?.maximumRange ?: 2000f

    override suspend fun startListening(): Boolean {
        if (magneticSensor == null || sensorManager == null) {
            _connectionStatus.value = ConnectionStatus.ERROR_UNAVAILABLE
            _statusMessage.value = "Hardware magnetometer not available on this device"
            return false
        }

        _connectionStatus.value = ConnectionStatus.CONNECTING
        val registered = sensorManager.registerListener(
            this,
            magneticSensor,
            samplingPeriodUs
        )

        if (registered) {
            _isConnected.value = true
            _connectionStatus.value = ConnectionStatus.CONNECTED
            _statusMessage.value = "Streaming real-time magnetometer data (${magneticSensor.name})"
            return true
        } else {
            _isConnected.value = false
            _connectionStatus.value = ConnectionStatus.ERROR_UNAVAILABLE
            _statusMessage.value = "Failed to register magnetometer sensor listener"
            return false
        }
    }

    override suspend fun stopListening() {
        sensorManager?.unregisterListener(this)
        _isConnected.value = false
        if (magneticSensor != null) {
            _connectionStatus.value = ConnectionStatus.DISCONNECTED
            _statusMessage.value = "Sensor stopped"
        }
    }

    override fun setSamplingRate(hz: Int) {
        val clampedHz = hz.coerceIn(10, 100)
        samplingPeriodUs = (1_000_000 / clampedHz)
        if (_isConnected.value && magneticSensor != null && sensorManager != null) {
            sensorManager.unregisterListener(this)
            sensorManager.registerListener(this, magneticSensor, samplingPeriodUs)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_MAGNETIC_FIELD) return

        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val magnitude = sqrt(x * x + y * y + z * z)

        val reading = SensorReading(
            timestamp = System.currentTimeMillis(),
            magnitudeUt = magnitude,
            xUt = x,
            yUt = y,
            zUt = z,
            accuracy = event.accuracy,
            source = SensorSource.PHONE_MAGNETOMETER
        )
        _readingsFlow.tryEmit(reading)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        val accuracyDesc = when (accuracy) {
            SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "High Accuracy"
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "Medium Accuracy"
            SensorManager.SENSOR_STATUS_ACCURACY_LOW -> "Low Accuracy (Calibration Recommended)"
            SensorManager.SENSOR_STATUS_UNRELIABLE -> "Unreliable (Perform 8-motion calibration)"
            else -> "Unknown Accuracy"
        }
        _statusMessage.value = "Sensor Status: $accuracyDesc"
    }
}
