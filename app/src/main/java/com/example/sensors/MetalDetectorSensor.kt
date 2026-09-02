package com.example.sensors

import com.example.core.SensorReading
import com.example.core.SensorSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

enum class ConnectionStatus {
    DISCONNECTED,
    CONNECTING,
    CONNECTED,
    ERROR_UNAVAILABLE,
    PERMISSION_REQUIRED
}

interface MetalDetectorSensor {
    val sensorId: String
    val sensorName: String
    val sensorSource: SensorSource
    val isConnected: StateFlow<Boolean>
    val connectionStatus: StateFlow<ConnectionStatus>
    val statusMessage: StateFlow<String>
    val rawReadings: Flow<SensorReading>
    
    suspend fun startListening(): Boolean
    suspend fun stopListening()
    fun setSamplingRate(hz: Int)
}
