package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.AppStrings
import com.example.core.DetectionMode
import com.example.detection.GoldSilverAnalysisResult
import com.example.detection.PreciousMetalCategory
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DetectorDarkBg
import com.example.ui.theme.DetectorSurfaceBorder
import com.example.ui.theme.DetectorSurfaceCard
import com.example.ui.theme.EmeraldSignal
import com.example.ui.theme.GoldEstimatedColor
import com.example.ui.theme.SilverEstimatedColor
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun GoldSilverSpectrumCard(
    analysis: GoldSilverAnalysisResult?,
    currentMode: DetectionMode,
    appLanguage: String,
    onModeSelect: (DetectionMode) -> Unit,
    modifier: Modifier = Modifier
) {
    val goldProb = analysis?.goldProbabilityPct ?: 0f
    val silverProb = analysis?.silverProbabilityPct ?: 0f
    val targetCategory = analysis?.targetCategory ?: PreciousMetalCategory.NONE
    val estimatedVdi = analysis?.estimatedVdi ?: 0f
    val isIronFilteredOut = analysis?.isIronFilteredOut ?: false
    val isHardwareTelemetry = analysis?.isHardwareTelemetryVerified ?: false

    val animatedGoldProb by animateFloatAsState(
        targetValue = goldProb / 100f,
        animationSpec = tween(300),
        label = "animated_gold_prob"
    )

    val animatedSilverProb by animateFloatAsState(
        targetValue = silverProb / 100f,
        animationSpec = tween(300),
        label = "animated_silver_prob"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DetectorSurfaceCard, RoundedCornerShape(16.dp))
            .border(1.dp, DetectorSurfaceBorder, RoundedCornerShape(16.dp))
            .padding(14.dp)
            .testTag("gold_silver_spectrum_card")
    ) {
        // 1. Header with Algorithm Name and Active Notch Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(GoldEstimatedColor.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Diamond,
                        contentDescription = null,
                        tint = GoldEstimatedColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = AppStrings.goldSilverAnalysisTitle(appLanguage),
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = if (isHardwareTelemetry) "VLF / PI Coils Calibrated" else "Dynamic Motion & Eddy Current",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isHardwareTelemetry) EmeraldSignal else AmberRadar,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            // Iron notch filter badge
            val notchActive = currentMode == DetectionMode.GOLD_PROSPECTING ||
                    currentMode == DetectionMode.SILVER_COIN_CACHE ||
                    currentMode == DetectionMode.NON_FERROUS_DETECTION

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (isIronFilteredOut) CrimsonAlert.copy(alpha = 0.2f)
                        else if (notchActive) EmeraldSignal.copy(alpha = 0.15f)
                        else DetectorDarkBg
                    )
                    .border(
                        1.dp,
                        if (isIronFilteredOut) CrimsonAlert.copy(alpha = 0.6f)
                        else if (notchActive) EmeraldSignal.copy(alpha = 0.4f)
                        else DetectorSurfaceBorder,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isIronFilteredOut) Icons.Default.Block else Icons.Default.FilterAlt,
                        contentDescription = null,
                        tint = if (isIronFilteredOut) CrimsonAlert else if (notchActive) EmeraldSignal else TextMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isIronFilteredOut) "Iron Notched Out" else if (notchActive) "Notch ON" else "Notch OFF",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isIronFilteredOut) CrimsonAlert else if (notchActive) EmeraldSignal else TextMuted,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 2. Dual Precious Metal Probability Meters
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Gold Probability Meter
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (goldProb > 40f) GoldEstimatedColor.copy(alpha = 0.12f) else DetectorDarkBg,
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        if (goldProb > 40f) GoldEstimatedColor.copy(alpha = 0.5f) else DetectorSurfaceBorder,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.goldProbability(appLanguage),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = GoldEstimatedColor,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${goldProb.toInt()}%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = GoldEstimatedColor,
                            fontWeight = FontWeight.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { animatedGoldProb },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = GoldEstimatedColor,
                    trackColor = DetectorSurfaceBorder,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (targetCategory) {
                        PreciousMetalCategory.GOLD_FINE_JEWELRY -> "14k-18k Jewelry / Rings"
                        PreciousMetalCategory.GOLD_NUGGET_NATIVE -> "Native Placer Nugget"
                        PreciousMetalCategory.GOLD_COIN_SOLID -> "Solid 22k/24k Gold Coin"
                        else -> if (goldProb > 20f) "Possible Gold Target" else "Target Spectrum: VDI 45-65"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (goldProb > 30f) TextPrimary else TextMuted,
                        fontSize = 10.sp
                    )
                )
            }

            // Silver Probability Meter
            Column(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        if (silverProb > 40f) SilverEstimatedColor.copy(alpha = 0.12f) else DetectorDarkBg,
                        RoundedCornerShape(12.dp)
                    )
                    .border(
                        1.dp,
                        if (silverProb > 40f) SilverEstimatedColor.copy(alpha = 0.5f) else DetectorSurfaceBorder,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = AppStrings.silverProbability(appLanguage),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = SilverEstimatedColor,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${silverProb.toInt()}%",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = SilverEstimatedColor,
                            fontWeight = FontWeight.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { animatedSilverProb },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = SilverEstimatedColor,
                    trackColor = DetectorSurfaceBorder,
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (targetCategory) {
                        PreciousMetalCategory.SILVER_JEWELRY_RELIC -> "925 Sterling / Relic"
                        PreciousMetalCategory.SILVER_COIN_BULLION -> "999 Pure Coin / Bullion"
                        else -> if (silverProb > 20f) "Possible Silver Target" else "Target Spectrum: VDI 80-98"
                    },
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = if (silverProb > 30f) TextPrimary else TextMuted,
                        fontSize = 10.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. VDI Visual Discrimination Scale
        Text(
            text = AppStrings.vdiSpectrum(appLanguage),
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextSecondary,
                fontWeight = FontWeight.SemiBold
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Multi-segment VDI Spectrum Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(DetectorDarkBg)
                .border(1.dp, DetectorSurfaceBorder, RoundedCornerShape(6.dp))
        ) {
            Row(modifier = Modifier.matchParentSize()) {
                // Ferrous Segment (0-24)
                Box(
                    modifier = Modifier
                        .weight(24f)
                        .fillMaxHeight()
                        .background(CrimsonAlert.copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("FE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = CrimsonAlert)
                }
                // Aluminum/Foil Segment (25-44)
                Box(
                    modifier = Modifier
                        .weight(20f)
                        .fillMaxHeight()
                        .background(Color(0xFF64748B).copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("AL", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                }
                // Gold Window (45-65)
                Box(
                    modifier = Modifier
                        .weight(21f)
                        .fillMaxHeight()
                        .background(GoldEstimatedColor.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("GOLD", fontSize = 9.sp, fontWeight = FontWeight.Black, color = GoldEstimatedColor)
                }
                // Copper/Brass Segment (66-79)
                Box(
                    modifier = Modifier
                        .weight(14f)
                        .fillMaxHeight()
                        .background(Color(0xFFFB923C).copy(alpha = 0.25f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("CU", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFB923C))
                }
                // Silver Window (80-98)
                Box(
                    modifier = Modifier
                        .weight(21f)
                        .fillMaxHeight()
                        .background(SilverEstimatedColor.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("SILVER", fontSize = 9.sp, fontWeight = FontWeight.Black, color = SilverEstimatedColor)
                }
            }

            // Current target VDI marker needle
            if (estimatedVdi > 0f) {
                val clampedVdi = estimatedVdi.coerceIn(0f, 100f)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(clampedVdi / 100f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.dp)
                            .fillMaxHeight()
                            .background(CyanGlow)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4. Quick Mode Presets
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            ModePresetChip(
                label = "Gold Mode",
                selected = currentMode == DetectionMode.GOLD_PROSPECTING,
                accentColor = GoldEstimatedColor,
                onClick = { onModeSelect(DetectionMode.GOLD_PROSPECTING) },
                modifier = Modifier.weight(1f)
            )

            ModePresetChip(
                label = "Silver Mode",
                selected = currentMode == DetectionMode.SILVER_COIN_CACHE,
                accentColor = SilverEstimatedColor,
                onClick = { onModeSelect(DetectionMode.SILVER_COIN_CACHE) },
                modifier = Modifier.weight(1f)
            )

            ModePresetChip(
                label = "All Metals",
                selected = currentMode == DetectionMode.QUICK_SCAN,
                accentColor = CyanGlow,
                onClick = { onModeSelect(DetectionMode.QUICK_SCAN) },
                modifier = Modifier.weight(1f)
            )

            ModePresetChip(
                label = "Ferrous",
                selected = currentMode == DetectionMode.FERROUS_DETECTION,
                accentColor = CrimsonAlert,
                onClick = { onModeSelect(DetectionMode.FERROUS_DETECTION) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ModePresetChip(
    label: String,
    selected: Boolean,
    accentColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (selected) accentColor.copy(alpha = 0.2f) else DetectorDarkBg)
            .border(
                1.dp,
                if (selected) accentColor else DetectorSurfaceBorder,
                RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) accentColor else TextSecondary,
                fontSize = 11.sp
            )
        )
    }
}
