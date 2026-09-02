package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.core.AppStrings
import com.example.signal_processing.ProcessedSignal
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DetectorSurfaceBorder
import com.example.ui.theme.DetectorSurfaceCard
import com.example.ui.theme.EmeraldSignal
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun SignalReadoutCards(
    signal: ProcessedSignal,
    appLanguage: String = "ar",
    isArabic: Boolean = (appLanguage == "ar"),
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("signal_readout_cards"),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Row 1: Baseline, Delta, Threshold
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReadoutMetricItem(
                label = AppStrings.baseline(appLanguage),
                value = String.format(Locale.US, "%.1f", signal.baselineUt),
                unit = "µT",
                accentColor = EmeraldSignal,
                modifier = Modifier.weight(1f)
            )
            ReadoutMetricItem(
                label = AppStrings.delta(appLanguage),
                value = (if (signal.deltaUt >= 0) "+" else "") + String.format(Locale.US, "%.1f", signal.deltaUt),
                unit = "µT",
                accentColor = if (signal.isThresholdExceeded) AmberRadar else CyanGlow,
                modifier = Modifier.weight(1f)
            )
            ReadoutMetricItem(
                label = AppStrings.threshold(appLanguage),
                value = String.format(Locale.US, "%.1f", signal.effectiveThresholdUt),
                unit = "µT",
                accentColor = AmberRadar,
                modifier = Modifier.weight(1f)
            )
        }

        // Row 2: Signal Strength, Stability, SNR dB, Confidence
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ReadoutMetricItem(
                label = AppStrings.signal(appLanguage),
                value = "${signal.signalStrengthPct.toInt()}",
                unit = "%",
                accentColor = if (signal.signalStrengthPct > 50) AmberRadar else CyanGlow,
                modifier = Modifier.weight(1f)
            )
            ReadoutMetricItem(
                label = AppStrings.stability(appLanguage),
                value = "${signal.signalStabilityPct.toInt()}",
                unit = "%",
                accentColor = if (signal.signalStabilityPct > 70) EmeraldSignal else AmberRadar,
                modifier = Modifier.weight(1f)
            )
            ReadoutMetricItem(
                label = AppStrings.snr(appLanguage),
                value = String.format(Locale.US, "%.1f", signal.snrDb),
                unit = "dB",
                accentColor = CyanGlow,
                modifier = Modifier.weight(1f)
            )
            ReadoutMetricItem(
                label = AppStrings.confidence(appLanguage),
                value = "${signal.detectionConfidencePct.toInt()}",
                unit = "%",
                accentColor = if (signal.detectionConfidencePct > 50) EmeraldSignal else TextSecondary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun ReadoutMetricItem(
    label: String,
    value: String,
    unit: String,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(DetectorSurfaceCard, RoundedCornerShape(8.dp))
            .border(1.dp, DetectorSurfaceBorder, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            color = TextSecondary,
            letterSpacing = 0.5.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = " $unit",
                fontSize = 10.sp,
                color = accentColor,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
