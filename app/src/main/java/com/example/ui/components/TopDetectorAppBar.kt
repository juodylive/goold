package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Usb
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.AppStrings
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
    appLanguage: String = "ar",
    isArabic: Boolean = (appLanguage == "ar"),
    onToggleLanguage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val langInfo = AppStrings.getLanguageInfo(appLanguage)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .background(DetectorSurfaceDark)
            .border(1.dp, DetectorSurfaceBorder)
            .padding(horizontal = 14.dp, vertical = 10.dp)
            .testTag("top_detector_app_bar"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // App Title & Tagline
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = AppStrings.appTitle(appLanguage),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = if (langInfo.isRtl) 0.5.sp else 1.5.sp,
                        color = TextPrimary,
                        fontSize = 18.sp
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = AppStrings.appPro(appLanguage),
                    style = MaterialTheme.typography.labelLarge.copy(
                        color = AmberRadar,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    ),
                    modifier = Modifier
                        .background(AmberRadar.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
                        .padding(horizontal = 5.dp, vertical = 2.dp)
                )
            }
            Text(
                text = AppStrings.appSubtitle(appLanguage),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 8.5.sp,
                    color = TextSecondary,
                    letterSpacing = if (langInfo.isRtl) 0.sp else 0.5.sp
                )
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Language Quick Switcher Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(CyanGlow.copy(alpha = 0.15f), RoundedCornerShape(16.dp))
                    .border(1.dp, CyanGlow.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .clickable { onToggleLanguage() }
                    .padding(horizontal = 8.dp, vertical = 4.dp)
                    .testTag("lang_toggle_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = "Language",
                    tint = CyanGlow,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${langInfo.flag} ${langInfo.code.uppercase()}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = CyanGlow
                )
            }

            // Active Sensor Pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(DetectorDarkBg, RoundedCornerShape(16.dp))
                    .border(1.dp, DetectorSurfaceBorder, RoundedCornerShape(16.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                val (icon, label) = when (activeSensor.sensorSource) {
                    SensorSource.PHONE_MAGNETOMETER -> Icons.Default.PhoneAndroid to AppStrings.sensorPhone(appLanguage)
                    SensorSource.EXTERNAL_BLUETOOTH_BLE -> Icons.Default.Bluetooth to AppStrings.sensorBle(appLanguage)
                    SensorSource.EXTERNAL_USB_OTG -> Icons.Default.Usb to AppStrings.sensorUsb(appLanguage)
                    SensorSource.DEVELOPER_SIMULATION_MODE -> Icons.Default.BugReport to AppStrings.sensorSim(appLanguage)
                }

                val statusColor = when {
                    !isDetecting -> TextSecondary
                    connectionStatus == ConnectionStatus.CONNECTED -> EmeraldSignal
                    connectionStatus == ConnectionStatus.CONNECTING -> AmberRadar
                    else -> CrimsonAlert
                }

                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(statusColor, CircleShape)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = TextPrimary,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = label,
                    fontSize = 9.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
            }
        }
    }
}
