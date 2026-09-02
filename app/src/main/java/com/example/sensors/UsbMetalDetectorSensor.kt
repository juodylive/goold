package com.example.sensors

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbEndpoint
import android.hardware.usb.UsbInterface
import android.hardware.usb.UsbManager
import android.os.Build
import com.example.core.SensorReading
import com.example.core.SensorSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class UsbMetalDetectorSensor(
    private val context: Context
) : MetalDetectorSensor {

    private val usbManager = context.getSystemService(Context.USB_SERVICE) as? UsbManager

    override val sensorId: String = "EXTERNAL_USB_DETECTOR"
    override val sensorName: String = "USB OTG Metal Detector"
    override val sensorSource: SensorSource = SensorSource.EXTERNAL_USB_OTG

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    override val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private val _statusMessage = MutableStateFlow("USB Detector Disconnected")
    override val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

    private val _readingsFlow = MutableSharedFlow<SensorReading>(
        replay = 1,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val rawReadings: Flow<SensorReading> = _readingsFlow.asSharedFlow()

    private val _connectedUsbDevice = MutableStateFlow<UsbDevice?>(null)
    val connectedUsbDevice: StateFlow<UsbDevice?> = _connectedUsbDevice.asStateFlow()

    private var usbConnection: UsbDeviceConnection? = null
    private var usbInterface: UsbInterface? = null
    private var readEndpoint: UsbEndpoint? = null
    private var readJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    val isUsbHostSupported: Boolean get() = usbManager != null

    fun getAvailableUsbDevices(): List<UsbDevice> {
        return usbManager?.deviceList?.values?.toList() ?: emptyList()
    }

    fun requestUsbPermission(device: UsbDevice) {
        if (usbManager == null) return
        val permissionIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent("com.example.metalscan.USB_PERMISSION"),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0
        )
        usbManager.requestPermission(device, permissionIntent)
    }

    fun connectUsbDevice(device: UsbDevice): Boolean {
        if (usbManager == null) return false
        _connectionStatus.value = ConnectionStatus.CONNECTING
        _statusMessage.value = "Connecting to USB Device ${device.deviceName}..."

        try {
            val connection = usbManager.openDevice(device)
            if (connection == null) {
                _connectionStatus.value = ConnectionStatus.PERMISSION_REQUIRED
                _statusMessage.value = "Unable to open USB device. Grant permission."
                return false
            }

            // Find bulk transfer endpoint
            var endpoint: UsbEndpoint? = null
            var targetInterface: UsbInterface? = null

            for (i in 0 until device.interfaceCount) {
                val iface = device.getInterface(i)
                for (j in 0 until iface.endpointCount) {
                    val ep = iface.getEndpoint(j)
                    if (ep.direction == android.hardware.usb.UsbConstants.USB_DIR_IN) {
                        endpoint = ep
                        targetInterface = iface
                        break
                    }
                }
                if (endpoint != null) break
            }

            if (targetInterface != null && endpoint != null) {
                connection.claimInterface(targetInterface, true)
                usbConnection = connection
                usbInterface = targetInterface
                readEndpoint = endpoint
                _connectedUsbDevice.value = device
                _isConnected.value = true
                _connectionStatus.value = ConnectionStatus.CONNECTED
                _statusMessage.value = "USB Detector Connected: ${device.productName ?: "Serial Hardware"}"

                startUsbReadLoop()
                return true
            } else {
                connection.close()
                _connectionStatus.value = ConnectionStatus.ERROR_UNAVAILABLE
                _statusMessage.value = "No compatible serial/bulk endpoint found on USB device"
                return false
            }
        } catch (e: Exception) {
            _connectionStatus.value = ConnectionStatus.ERROR_UNAVAILABLE
            _statusMessage.value = "USB Connection error: ${e.localizedMessage}"
            return false
        }
    }

    private fun startUsbReadLoop() {
        readJob?.cancel()
        readJob = scope.launch {
            val buffer = ByteArray(256)
            val connection = usbConnection
            val endpoint = readEndpoint

            while (isActive && _isConnected.value && connection != null && endpoint != null) {
                val bytesRead = connection.bulkTransfer(endpoint, buffer, buffer.size, 100)
                if (bytesRead > 0) {
                    val rawStr = String(buffer, 0, bytesRead, Charsets.UTF_8).trim()
                    parseUsbData(rawStr)
                }
            }
        }
    }

    fun parseUsbData(raw: String) {
        // Parse incoming packet (e.g. $MDDATA,54.2,45,28.4,18.5)
        try {
            val clean = raw.trim()
            if (clean.contains("\$MDDATA,")) {
                val parts = clean.substringAfter("\$MDDATA,").split(",")
                val mag = parts.getOrNull(0)?.toFloatOrNull() ?: 50f
                val vdi = parts.getOrNull(1)?.toFloatOrNull()
                val phase = parts.getOrNull(2)?.toFloatOrNull()
                val freq = parts.getOrNull(3)?.toFloatOrNull()
                val batt = parts.getOrNull(4)?.toIntOrNull()

                val reading = SensorReading(
                    timestamp = System.currentTimeMillis(),
                    magnitudeUt = mag,
                    accuracy = 3,
                    source = SensorSource.EXTERNAL_USB_OTG,
                    frequencyKhz = freq ?: 20.0f,
                    conductivityIndex = vdi,
                    phaseAngleDeg = phase,
                    targetId = vdi?.toInt(),
                    batteryLevelPct = batt
                )
                _readingsFlow.tryEmit(reading)
            }
        } catch (ignored: Exception) {}
    }

    override suspend fun startListening(): Boolean {
        return _isConnected.value
    }

    override suspend fun stopListening() {
        readJob?.cancel()
        readJob = null
        try {
            usbInterface?.let { usbConnection?.releaseInterface(it) }
            usbConnection?.close()
        } catch (ignored: Exception) {}
        usbConnection = null
        usbInterface = null
        readEndpoint = null
        _connectedUsbDevice.value = null
        _isConnected.value = false
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
        _statusMessage.value = "USB Detector Disconnected"
    }

    override fun setSamplingRate(hz: Int) {}
}
