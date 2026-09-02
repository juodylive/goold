package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.AppStrings
import com.example.core.DetectionMode
import com.example.core.ToneType
import com.example.core.VibrationMode
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DetectorDarkBg
import com.example.ui.theme.DetectorSurfaceBorder
import com.example.ui.theme.DetectorSurfaceCard
import com.example.ui.theme.EmeraldSignal
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.DetectorViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen(
    viewModel: DetectorViewModel,
    modifier: Modifier = Modifier
) {
    val currentMode by viewModel.currentMode.collectAsStateWithLifecycle()
    val audioConfig by viewModel.audioConfig.collectAsStateWithLifecycle()
    val vibrationConfig by viewModel.vibrationConfig.collectAsStateWithLifecycle()
    val expertSettings by viewModel.expertSettings.collectAsStateWithLifecycle()
    val appLanguage by viewModel.appLanguage.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DetectorDarkBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("settings_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = AppStrings.settingsScreenTitle(appLanguage),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        // 0. Language Switcher Card (Supports Arabic, English, German, Spanish, Portuguese, French, Turkish)
        Card(
            modifier = Modifier.fillMaxWidth().testTag("language_settings_card"),
            colors = CardDefaults.cardColors(containerColor = DetectorSurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DetectorSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = CyanGlow,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.languageTitle(appLanguage),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = CyanGlow,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppStrings.SUPPORTED_LANGUAGES.forEach { lang ->
                        val isSelected = appLanguage == lang.code
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setLanguage(lang.code) },
                            label = {
                                Text(
                                    text = "${lang.flag} ${lang.nativeName}",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyanGlow.copy(alpha = 0.25f),
                                selectedLabelColor = CyanGlow
                            ),
                            modifier = Modifier.testTag("lang_chip_${lang.code}")
                        )
                    }
                }
            }
        }

        // 1. Detection Mode Selector Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("detection_mode_card"),
            colors = CardDefaults.cardColors(containerColor = DetectorSurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DetectorSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        tint = AmberRadar,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.detectionModesSection(appLanguage),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = AmberRadar,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                DetectionMode.entries.forEach { mode ->
                    val isSelected = currentMode == mode
                    val title = AppStrings.modeTitle(mode, appLanguage)
                    val desc = AppStrings.modeDesc(mode, appLanguage)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .background(
                                if (isSelected) AmberRadar.copy(alpha = 0.12f) else DetectorDarkBg,
                                RoundedCornerShape(8.dp)
                            )
                            .border(
                                1.dp,
                                if (isSelected) AmberRadar else DetectorSurfaceBorder,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.setDetectionMode(mode) }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(
                                    if (isSelected) AmberRadar else DetectorSurfaceBorder,
                                    CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = title,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) TextPrimary else TextSecondary,
                                fontSize = 13.sp
                            )
                            Text(
                                text = desc,
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // 2. Audio Feedback Configuration Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("audio_settings_card"),
            colors = CardDefaults.cardColors(containerColor = DetectorSurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DetectorSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = CyanGlow,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.audioSynthSection(appLanguage),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = CyanGlow,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Switch(
                        checked = audioConfig.isEnabled,
                        onCheckedChange = {
                            viewModel.updateAudioConfig(audioConfig.copy(isEnabled = it))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = CyanGlow,
                            checkedTrackColor = CyanGlow.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.testTag("audio_enable_switch")
                    )
                }

                if (audioConfig.isEnabled) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = AppStrings.toneSynthesisMode(appLanguage), fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ToneType.entries.forEach { tone ->
                            val isSelected = audioConfig.toneType == tone
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.updateAudioConfig(audioConfig.copy(toneType = tone)) },
                                label = { Text(AppStrings.toneName(tone, appLanguage), fontSize = 10.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyanGlow.copy(alpha = 0.2f),
                                    selectedLabelColor = CyanGlow
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Volume Slider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = AppStrings.volume(appLanguage), fontSize = 11.sp, color = TextSecondary)
                        Text(text = "${(audioConfig.volume * 100).toInt()}%", fontSize = 11.sp, color = CyanGlow, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = audioConfig.volume,
                        onValueChange = { viewModel.updateAudioConfig(audioConfig.copy(volume = it)) },
                        valueRange = 0.1f..1.0f,
                        colors = SliderDefaults.colors(thumbColor = CyanGlow, activeTrackColor = CyanGlow)
                    )

                    // Min Signal Trigger Threshold
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = AppStrings.muteThreshold(appLanguage), fontSize = 11.sp, color = TextSecondary)
                        Text(text = "${audioConfig.minSignalThresholdPct.toInt()}%", fontSize = 11.sp, color = CyanGlow, fontFamily = FontFamily.Monospace)
                    }
                    Slider(
                        value = audioConfig.minSignalThresholdPct,
                        onValueChange = { viewModel.updateAudioConfig(audioConfig.copy(minSignalThresholdPct = it)) },
                        valueRange = 0f..50f,
                        colors = SliderDefaults.colors(thumbColor = CyanGlow, activeTrackColor = CyanGlow)
                    )
                }
            }
        }

        // 3. Vibration Feedback Settings Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("vibration_settings_card"),
            colors = CardDefaults.cardColors(containerColor = DetectorSurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DetectorSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Vibration,
                            contentDescription = null,
                            tint = EmeraldSignal,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = AppStrings.vibrationSection(appLanguage),
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = EmeraldSignal,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Switch(
                        checked = vibrationConfig.isEnabled,
                        onCheckedChange = {
                            viewModel.updateVibrationConfig(vibrationConfig.copy(isEnabled = it))
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = EmeraldSignal,
                            checkedTrackColor = EmeraldSignal.copy(alpha = 0.3f)
                        ),
                        modifier = Modifier.testTag("vibration_enable_switch")
                    )
                }

                if (vibrationConfig.isEnabled) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = AppStrings.vibrationPattern(appLanguage), fontSize = 11.sp, color = TextSecondary)
                    Spacer(modifier = Modifier.height(6.dp))

                    VibrationMode.entries.filter { it != VibrationMode.OFF }.forEach { vMode ->
                        val isSelected = vibrationConfig.mode == vMode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp)
                                .background(
                                    if (isSelected) EmeraldSignal.copy(alpha = 0.12f) else DetectorDarkBg,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.updateVibrationConfig(vibrationConfig.copy(mode = vMode)) }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                .size(8.dp)
                                .background(
                                    if (isSelected) EmeraldSignal else DetectorSurfaceBorder,
                                    CircleShape
                                )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = AppStrings.vibrationModeName(vMode, appLanguage),
                                fontSize = 12.sp,
                                color = if (isSelected) TextPrimary else TextSecondary
                            )
                        }
                    }
                }
            }
        }

        // 4. Expert Fine-Tuning Parameter Card
        Card(
            modifier = Modifier.fillMaxWidth().testTag("expert_settings_card"),
            colors = CardDefaults.cardColors(containerColor = DetectorSurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DetectorSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.GraphicEq,
                        contentDescription = null,
                        tint = AmberRadar,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = AppStrings.expertSection(appLanguage),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = AmberRadar,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Filter Alpha Slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = AppStrings.filterAlpha(appLanguage), fontSize = 11.sp, color = TextSecondary)
                    Text(text = String.format("%.2f", expertSettings.filterAlpha), fontSize = 11.sp, color = AmberRadar, fontFamily = FontFamily.Monospace)
                }
                Slider(
                    value = expertSettings.filterAlpha,
                    onValueChange = { viewModel.updateExpertSettings(expertSettings.copy(filterAlpha = it)) },
                    valueRange = 0.05f..0.80f,
                    colors = SliderDefaults.colors(thumbColor = AmberRadar, activeTrackColor = AmberRadar)
                )

                // Continuous Environmental Drift Compensation
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = AppStrings.driftCompTitle(appLanguage), fontSize = 12.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                        Text(text = AppStrings.driftCompDesc(appLanguage), fontSize = 10.sp, color = TextSecondary)
                    }
                    Switch(
                        checked = expertSettings.continuousDriftComp,
                        onCheckedChange = { viewModel.updateExpertSettings(expertSettings.copy(continuousDriftComp = it)) },
                        colors = SwitchDefaults.colors(checkedThumbColor = AmberRadar, checkedTrackColor = AmberRadar.copy(alpha = 0.3f))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

