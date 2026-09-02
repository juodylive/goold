package com.example.sensors

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import com.example.core.SensorReading
import com.example.core.SensorSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

data class DiscoveredBleDevice(
    val device: BluetoothDevice,
    val name: String,
    val address: String,
    val rssi: Int
)

enum class HardwareProtocol {
    STANDARD_ASCII, // $MDDATA,mag,vdi,phase,freq,batt
    JSON_STREAM,
    BINARY_FRAME
}

class BluetoothMetalDetectorSensor(
    private val context: Context
) : MetalDetectorSensor {

    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter

    override val sensorId: String = "EXTERNAL_BLE_DETECTOR"
    override val sensorName: String = "BLE Metal Detector Coil"
    override val sensorSource: SensorSource = SensorSource.EXTERNAL_BLUETOOTH_BLE

    private val _isConnected = MutableStateFlow(false)
    override val isConnected: StateFlow<Boolean> = _isConnected.asStateFlow()

    private val _connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)
    override val connectionStatus: StateFlow<ConnectionStatus> = _connectionStatus.asStateFlow()

    private val _statusMessage = MutableStateFlow("BLE Detector Disconnected")
    override val statusMessage: StateFlow<String> = _statusMessage.asStateFlow()

    private val _readingsFlow = MutableSharedFlow<SensorReading>(
        replay = 1,
        extraBufferCapacity = 64,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val rawReadings: Flow<SensorReading> = _readingsFlow.asSharedFlow()

    private val _discoveredDevices = MutableStateFlow<List<DiscoveredBleDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<DiscoveredBleDevice>> = _discoveredDevices.asStateFlow()

    private val _isScanning = MutableStateFlow(false)
    val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

    private val _deviceBatteryPct = MutableStateFlow<Int?>(null)
    val deviceBatteryPct: StateFlow<Int?> = _deviceBatteryPct.asStateFlow()

    private val _connectedDeviceAddress = MutableStateFlow<String?>(null)
    val connectedDeviceAddress: StateFlow<String?> = _connectedDeviceAddress.asStateFlow()

    var activeProtocol: HardwareProtocol = HardwareProtocol.STANDARD_ASCII

    private var bluetoothGatt: BluetoothGatt? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    // Standard Nordic UART / Custom Metal Detector UUIDs
    private val UART_SERVICE_UUID = UUID.fromString("6E400001-B5A3-F393-E0A9-E50E24DCCA9E")
    private val UART_TX_CHAR_UUID = UUID.fromString("6E400003-B5A3-F393-E0A9-E50E24DCCA9E")
    private val CLIENT_CONFIG_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

    val isBluetoothSupported: Boolean = bluetoothAdapter != null
    val isBluetoothEnabled: Boolean get() = bluetoothAdapter?.isEnabled == true

    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            result?.device?.let { device ->
                val name = device.name ?: "Unknown Detector Hardware"
                val address = device.address
                val rssi = result.rssi
                val existing = _discoveredDevices.value.filter { it.address != address }
                _discoveredDevices.value = existing + DiscoveredBleDevice(device, name, address, rssi)
            }
        }

        override fun onScanFailed(errorCode: Int) {
            _isScanning.value = false
            _statusMessage.value = "BLE Scan failed: Code $errorCode"
        }
    }

    @SuppressLint("MissingPermission")
    fun startBleScan(): Boolean {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
            _statusMessage.value = "Bluetooth is turned off"
            return false
        }
        _discoveredDevices.value = emptyList()
        val scanner = bluetoothAdapter.bluetoothLeScanner
        if (scanner == null) {
            _statusMessage.value = "BLE Scanner unavailable"
            return false
        }
        _isScanning.value = true
        _statusMessage.value = "Scanning for external BLE detector hardware..."
        try {
            scanner.startScan(scanCallback)
            return true
        } catch (e: Exception) {
            _isScanning.value = false
            _statusMessage.value = "Error starting scan: ${e.localizedMessage}"
            return false
        }
    }

    @SuppressLint("MissingPermission")
    fun stopBleScan() {
        if (_isScanning.value) {
            try {
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
            } catch (ignored: Exception) {}
            _isScanning.value = false
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(device: BluetoothDevice) {
        stopBleScan()
        _connectionStatus.value = ConnectionStatus.CONNECTING
        _statusMessage.value = "Connecting to ${device.name ?: device.address}..."
        
        bluetoothGatt?.close()
        bluetoothGatt = device.connectGatt(context, false, gattCallback)
    }

    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                _isConnected.value = true
                _connectionStatus.value = ConnectionStatus.CONNECTED
                _connectedDeviceAddress.value = gatt?.device?.address
                _statusMessage.value = "Connected. Discovering services..."
                gatt?.discoverServices()
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                _isConnected.value = false
                _connectionStatus.value = ConnectionStatus.DISCONNECTED
                _connectedDeviceAddress.value = null
                _statusMessage.value = "Disconnected from BLE detector"
                bluetoothGatt?.close()
                bluetoothGatt = null
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            if (status == BluetoothGatt.GATT_SUCCESS && gatt != null) {
                _statusMessage.value = "Hardware ready. Subscribing to telemetry..."
                val service = gatt.getService(UART_SERVICE_UUID)
                val characteristic = service?.getCharacteristic(UART_TX_CHAR_UUID)
                if (characteristic != null) {
                    gatt.setCharacteristicNotification(characteristic, true)
                    val descriptor = characteristic.getDescriptor(CLIENT_CONFIG_DESCRIPTOR)
                    descriptor?.let {
                        it.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        gatt.writeDescriptor(it)
                    }
                }
            }
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
            val dataBytes = characteristic?.value ?: return
            val packet = String(dataBytes, Charsets.UTF_8).trim()
            parseIncomingPacket(packet)
        }
    }

    fun parseIncomingPacket(raw: String) {
        try {
            when (activeProtocol) {
                HardwareProtocol.STANDARD_ASCII -> {
                    // Expected format: $MDDATA,magnitude,vdi,phase,freq,batt
                    // or simple CSV: magnitude,vdi,phase
                    if (raw.startsWith("\$MDDATA,")) {
                        val parts = raw.removePrefix("\$MDDATA,").split(",")
                        val magnitude = parts.getOrNull(0)?.toFloatOrNull() ?: 50f
                        val vdi = parts.getOrNull(1)?.toFloatOrNull()
                        val phase = parts.getOrNull(2)?.toFloatOrNull()
                        val freq = parts.getOrNull(3)?.toFloatOrNull()
                        val batt = parts.getOrNull(4)?.toIntOrNull()
                        if (batt != null) _deviceBatteryPct.value = batt

                        val reading = SensorReading(
                            timestamp = System.currentTimeMillis(),
                            magnitudeUt = magnitude,
                            accuracy = 3,
                            source = SensorSource.EXTERNAL_BLUETOOTH_BLE,
                            frequencyKhz = freq ?: 18.0f,
                            conductivityIndex = vdi,
                            phaseAngleDeg = phase,
                            targetId = vdi?.toInt(),
                            batteryLevelPct = batt
                        )
                        _readingsFlow.tryEmit(reading)
                    }
                }
                HardwareProtocol.JSON_STREAM -> {
                    // Simple parse if JSON format
                    val magRegex = "\"magnitude\":\\s*([0-9.]+)".toRegex()
                    val vdiRegex = "\"vdi\":\\s*([0-9.]+)".toRegex()
                    val phaseRegex = "\"phase\":\\s*([0-9.]+)".toRegex()
                    val mag = magRegex.find(raw)?.groupValues?.getOrNull(1)?.toFloatOrNull() ?: 50f
                    val vdi = vdiRegex.find(raw)?.groupValues?.getOrNull(1)?.toFloatOrNull()
                    val phase = phaseRegex.find(raw)?.groupValues?.getOrNull(1)?.toFloatOrNull()
                    
                    val reading = SensorReading(
                        timestamp = System.currentTimeMillis(),
                        magnitudeUt = mag,
                        accuracy = 3,
                        source = SensorSource.EXTERNAL_BLUETOOTH_BLE,
                        conductivityIndex = vdi,
                        phaseAngleDeg = phase,
                        targetId = vdi?.toInt()
                    )
                    _readingsFlow.tryEmit(reading)
                }
                HardwareProtocol.BINARY_FRAME -> {
                    // Fallback binary frame parsing
                }
            }
        } catch (ignored: Exception) {}
    }

    override suspend fun startListening(): Boolean {
        return _isConnected.value
    }

    @SuppressLint("MissingPermission")
    override suspend fun stopListening() {
        try {
            bluetoothGatt?.disconnect()
            bluetoothGatt?.close()
        } catch (ignored: Exception) {}
        bluetoothGatt = null
        _isConnected.value = false
        _connectionStatus.value = ConnectionStatus.DISCONNECTED
    }

    override fun setSamplingRate(hz: Int) {
        // Hardware streaming rate is governed by BLE peripheral notifications
    }
}
