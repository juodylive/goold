package com.example.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BluetoothSearching
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.AppStrings
import com.example.core.SensorSource
import com.example.sensors.ConnectionStatus
import com.example.sensors.HardwareProtocol
import com.example.sensors.SimulatorMetalDetectorSensor
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DetectorDarkBg
import com.example.ui.theme.DetectorSurfaceBorder
import com.example.ui.theme.DetectorSurfaceCard
import com.example.ui.theme.EmeraldSignal
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DetectorViewModel

@Composable
fun DevicesScreen(
    viewModel: DetectorViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activeSensor by viewModel.activeSensor.collectAsStateWithLifecycle()
    val isDetecting by viewModel.isDetecting.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()
    val isAr = appLanguage == "ar"

    val bleSensor = viewModel.bleSensor
    val bleDevices by bleSensor.discoveredDevices.collectAsStateWithLifecycle()
    val isBleScanning by bleSensor.isScanning.collectAsStateWithLifecycle()
    val bleStatusMsg by bleSensor.statusMessage.collectAsStateWithLifecycle()
    val bleConnectionStatus by bleSensor.connectionStatus.collectAsStateWithLifecycle()
    val connectedBleAddress by bleSensor.connectedDeviceAddress.collectAsStateWithLifecycle()

    val usbSensor = viewModel.usbSensor
    val usbConnectionStatus by usbSensor.connectionStatus.collectAsStateWithLifecycle()
    val usbStatusMsg by usbSensor.statusMessage.collectAsStateWithLifecycle()
    val connectedUsbDevice by usbSensor.connectedUsbDevice.collectAsStateWithLifecycle()

    val simSensor = viewModel.simSensor
    var simVdiEnabled by remember { mutableStateOf(simSensor.simulateExternalVdi) }

    val scrollState = rememberScrollState()

    // Bluetooth Permission Request Launcher
    val blePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms.values.all { it }
        if (granted) {
            bleSensor.startBleScan()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DetectorDarkBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("devices_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = AppStrings.devicesScreenTitle(isAr),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        // 1. Built-In Phone Magnetometer Card
        val isPhoneActive = activeSensor.sensorSource == SensorSource.PHONE_MAGNETOMETER
        Card(
            modifier = Modifier.fillMaxWidth().testTag("sensor_phone_card"),
            colors = CardDefaults.cardColors(
                containerColor = if (isPhoneActive) DetectorSurfaceCard else DetectorDarkBg
            ),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
                if (isPhoneActive) 1.5.dp else 1.dp,
                if (isPhoneActive) AmberRadar else DetectorSurfaceBorder
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PhoneAndroid,
                            contentDescription = null,
                            tint = if (isPhoneActive) AmberRadar else TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.phoneSensorTitle(isAr),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = viewModel.phoneSensor.sensorName,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                    }

                    if (isPhoneActive) {
                        Text(
                            text = AppStrings.activeBadge(isAr),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = AmberRadar,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .background(AmberRadar.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    } else {
                        Button(
                            onClick = { viewModel.selectSensor(SensorSource.PHONE_MAGNETOMETER) },
                            colors = ButtonDefaults.buttonColors(containerColor = DetectorSurfaceBorder, contentColor = TextPrimary),
                            modifier = Modifier.height(34.dp).testTag("select_phone_sensor_btn")
                        ) {
                            Text(AppStrings.selectBtn(isAr), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Vendor: ${viewModel.phoneSensor.sensorVendor} | Resolution: ${viewModel.phoneSensor.sensorResolutionUt} µT | Max: ${viewModel.phoneSensor.maxRangeUt.toInt()} µT",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp
                    )
                )
            }
        }

        // 2. Bluetooth Low Energy (BLE) Detector Search Coil
        val isBleActive = activeSensor.sensorSource == SensorSource.EXTERNAL_BLUETOOTH_BLE
        Card(
            modifier = Modifier.fillMaxWidth().testTag("sensor_ble_card"),
            colors = CardDefaults.cardColors(
                containerColor = if (isBleActive) DetectorSurfaceCard else DetectorDarkBg
            ),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
                if (isBleActive) 1.5.dp else 1.dp,
                if (isBleActive) CyanGlow else DetectorSurfaceBorder
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Bluetooth,
                            contentDescription = null,
                            tint = if (isBleActive) CyanGlow else TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.bleSensorTitle(isAr),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = bleStatusMsg,
                                style = MaterialTheme.typography.bodySmall.copy(color = CyanGlow)
                            )
                        }
                    }

                    if (isBleActive) {
                        Text(
                            text = AppStrings.activeBadge(isAr),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = CyanGlow,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .background(CyanGlow.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    } else {
                        Button(
                            onClick = { viewModel.selectSensor(SensorSource.EXTERNAL_BLUETOOTH_BLE) },
                            colors = ButtonDefaults.buttonColors(containerColor = DetectorSurfaceBorder, contentColor = TextPrimary),
                            modifier = Modifier.height(34.dp).testTag("select_ble_sensor_btn")
                        ) {
                            Text(AppStrings.selectBtn(isAr), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // BLE Scan Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            if (isBleScanning) {
                                bleSensor.stopBleScan()
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    val scanPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)
                                    val connPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT)
                                    if (scanPerm != PackageManager.PERMISSION_GRANTED || connPerm != PackageManager.PERMISSION_GRANTED) {
                                        blePermissionLauncher.launch(arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT))
                                    } else {
                                        bleSensor.startBleScan()
                                    }
                                } else {
                                    val locPerm = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                                    if (locPerm != PackageManager.PERMISSION_GRANTED) {
                                        blePermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION))
                                    } else {
                                        bleSensor.startBleScan()
                                    }
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(40.dp).testTag("ble_scan_btn"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBleScanning) CrimsonAlert else CyanGlow,
                            contentColor = DetectorDarkBg
                        )
                    ) {
                        Icon(
                            imageVector = if (isBleScanning) Icons.Default.Stop else Icons.Default.BluetoothSearching,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = if (isBleScanning) (if (isAr) "إيقاف البحث" else "Stop Scan") else AppStrings.bleScanBtn(isAr), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Discovered BLE Devices List
                if (bleDevices.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = if (isAr) "الأجهزة المكتشفة" else "DISCOVERED DEVICES", fontSize = 10.sp, color = TextSecondary, letterSpacing = 1.sp)
                    Spacer(modifier = Modifier.height(6.dp))

                    bleDevices.forEach { dev ->
                        val isThisConnected = dev.address == connectedBleAddress
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DetectorDarkBg, RoundedCornerShape(8.dp))
                                .border(1.dp, if (isThisConnected) EmeraldSignal else DetectorSurfaceBorder, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = dev.name, fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                                Text(text = "${dev.address} | RSSI: ${dev.rssi} dBm", color = TextSecondary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }
                            Button(
                                onClick = {
                                    bleSensor.connectToDevice(dev.device)
                                    viewModel.selectSensor(SensorSource.EXTERNAL_BLUETOOTH_BLE)
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isThisConnected) EmeraldSignal else CyanGlow,
                                    contentColor = DetectorDarkBg
                                ),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(if (isThisConnected) (if (isAr) "متصل" else "Connected") else (if (isAr) "اتصال" else "Connect"), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }

        // 3. USB OTG / Serial Interface Card
        val isUsbActive = activeSensor.sensorSource == SensorSource.EXTERNAL_USB_OTG
        Card(
            modifier = Modifier.fillMaxWidth().testTag("sensor_usb_card"),
            colors = CardDefaults.cardColors(
                containerColor = if (isUsbActive) DetectorSurfaceCard else DetectorDarkBg
            ),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
                if (isUsbActive) 1.5.dp else 1.dp,
                if (isUsbActive) EmeraldSignal else DetectorSurfaceBorder
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Usb,
                            contentDescription = null,
                            tint = if (isUsbActive) EmeraldSignal else TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.usbSensorTitle(isAr),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = usbStatusMsg,
                                style = MaterialTheme.typography.bodySmall.copy(color = EmeraldSignal)
                            )
                        }
                    }

                    if (isUsbActive) {
                        Text(
                            text = AppStrings.activeBadge(isAr),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = EmeraldSignal,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .background(EmeraldSignal.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    } else {
                        Button(
                            onClick = { viewModel.selectSensor(SensorSource.EXTERNAL_USB_OTG) },
                            colors = ButtonDefaults.buttonColors(containerColor = DetectorSurfaceBorder, contentColor = TextPrimary),
                            modifier = Modifier.height(34.dp).testTag("select_usb_sensor_btn")
                        ) {
                            Text(AppStrings.selectBtn(isAr), fontSize = 12.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                val usbList = usbSensor.getAvailableUsbDevices()
                if (usbList.isEmpty()) {
                    Text(
                        text = if (isAr) "قم بتوصيل كابل USB OTG لمستشعر المعادن للتعرف على الجهاز تلقائياً." else "Plug in a USB OTG serial metal detector cable to auto-detect hardware.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                } else {
                    usbList.forEach { dev ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(DetectorDarkBg, RoundedCornerShape(8.dp))
                                .padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = dev.productName ?: "Serial Hardware", fontWeight = FontWeight.Bold, color = TextPrimary, fontSize = 13.sp)
                                Text(text = "Vendor ID: ${dev.vendorId} | Product ID: ${dev.productId}", color = TextSecondary, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }
                            Button(
                                onClick = {
                                    usbSensor.connectUsbDevice(dev)
                                    viewModel.selectSensor(SensorSource.EXTERNAL_USB_OTG)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSignal, contentColor = DetectorDarkBg),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Text(if (isAr) "فتح USB" else "Open USB", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // 4. Developer Algorithm Simulation Mode
        val isSimActive = activeSensor.sensorSource == SensorSource.DEVELOPER_SIMULATION_MODE
        Card(
            modifier = Modifier.fillMaxWidth().testTag("sensor_sim_card"),
            colors = CardDefaults.cardColors(
                containerColor = if (isSimActive) DetectorSurfaceCard else DetectorDarkBg
            ),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(
                if (isSimActive) 1.5.dp else 1.dp,
                if (isSimActive) CrimsonAlert else DetectorSurfaceBorder
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.BugReport,
                            contentDescription = null,
                            tint = if (isSimActive) CrimsonAlert else TextSecondary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = AppStrings.simSensorTitle(isAr),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = if (isAr) "توليد إشارات اصطناعية لاختبار الخوارزميات بدون هاردوير" else "Generates synthetic signals for testing",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                    }

                    Switch(
                        checked = isSimActive,
                        onCheckedChange = { checked ->
                            if (checked) {
                                viewModel.selectSensor(SensorSource.DEVELOPER_SIMULATION_MODE)
                            } else {
                                viewModel.selectSensor(SensorSource.PHONE_MAGNETOMETER)
                            }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CrimsonAlert,
                            checkedTrackColor = CrimsonAlert.copy(alpha = 0.3f),
                            uncheckedTrackColor = DetectorDarkBg
                        ),
                        modifier = Modifier.testTag("toggle_simulator_switch")
                    )
                }

                if (isSimActive) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isAr) "محاكاة هوية الهدف VDI الخارجية" else "Simulate External Hardware VDI (Target ID)",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary)
                        )
                        Switch(
                            checked = simVdiEnabled,
                            onCheckedChange = {
                                simVdiEnabled = it
                                simSensor.simulateExternalVdi = it
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = CyanGlow,
                                checkedTrackColor = CyanGlow.copy(alpha = 0.3f)
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
