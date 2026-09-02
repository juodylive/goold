package com.example.core

enum class SensorSource {
    PHONE_MAGNETOMETER,
    EXTERNAL_BLUETOOTH_BLE,
    EXTERNAL_USB_OTG,
    DEVELOPER_SIMULATION_MODE
}

data class SensorReading(
    val timestamp: Long = System.currentTimeMillis(),
    val magnitudeUt: Float,
    val xUt: Float = 0f,
    val yUt: Float = 0f,
    val zUt: Float = 0f,
    val accuracy: Int = 3, // SensorManager.SENSOR_STATUS_ACCURACY_HIGH
    val source: SensorSource = SensorSource.PHONE_MAGNETOMETER,
    
    // Telemetry fields from optional external metal detector hardware
    val frequencyKhz: Float? = null,
    val amplitude: Float? = null,
    val phaseAngleDeg: Float? = null,
    val conductivityIndex: Float? = null, // VDI 0-100
    val targetId: Int? = null,
    val signalStrengthPct: Float? = null,
    val groundBalance: Float? = null,
    val batteryLevelPct: Int? = null
)
