package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryStd
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.SensorSource
import com.example.sensors.ConnectionStatus
import com.example.sensors.MetalDetectorSensor
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DetectorDarkBg
import com.example.ui.theme.DetectorSurfaceBorder
import com.example.ui.theme.DetectorSurfaceDark
import com.example.ui.theme.EmeraldSignal
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun TopDetectorAppBar(
    activeSensor: MetalDetectorSensor,
    connectionStatus: ConnectionStatus,
    isDetecting: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(DetectorSurfaceDark)
            .border(1.dp, DetectorSurfaceBorder)
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .testTag("top_detector_app_bar"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title & Tagline
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "METALSCAN",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        color = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "PRO",
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = AmberRadar,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .background(AmberRadar.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
            Text(
                text = "PRECISION MAGNETOMETER & TELEMETRY",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.sp,
                    color = TextSecondary,
                    letterSpacing = 0.8.sp
                )
            )
        }

        // Active Sensor & Live Status Pill
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .background(DetectorDarkBg, RoundedCornerShape(20.dp))
                .border(1.dp, DetectorSurfaceBorder, RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
            val (icon, label) = when (activeSensor.sensorSource) {
                SensorSource.PHONE_MAGNETOMETER -> Icons.Default.PhoneAndroid to "Phone Sensor"
                SensorSource.EXTERNAL_BLUETOOTH_BLE -> Icons.Default.Bluetooth to "BLE Hardware"
                SensorSource.EXTERNAL_USB_OTG -> Icons.Default.Usb to "USB Serial"
                SensorSource.DEVELOPER_SIMULATION_MODE -> Icons.Default.BugReport to "TEST MODE"
            }

            val statusColor = when {
                !isDetecting -> TextSecondary
                connectionStatus == ConnectionStatus.CONNECTED -> EmeraldSignal
                connectionStatus == ConnectionStatus.CONNECTING -> AmberRadar
                else -> CrimsonAlert
            }

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(statusColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = TextPrimary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = TextPrimary,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
