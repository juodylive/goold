package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.AppStrings
import com.example.signal_processing.ProcessedSignal
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.AmberRadarLight
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.CyanGlowBright
import com.example.ui.theme.DetectorDarkBg
import com.example.ui.theme.DetectorSurfaceCard
import com.example.ui.theme.EmeraldSignal
import com.example.ui.theme.ReticleGrid
import com.example.ui.theme.ReticleRing
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CircularDetectorMeter(
    signal: ProcessedSignal,
    isDetecting: Boolean,
    isArabic: Boolean = true,
    modifier: Modifier = Modifier
) {
    // Smooth meter animated angle
    val animatedStrength = remember { Animatable(0f) }

    LaunchedEffect(signal.signalStrengthPct, isDetecting) {
        if (!isDetecting) {
            animatedStrength.animateTo(0f, tween(300))
        } else {
            animatedStrength.animateTo(
                signal.signalStrengthPct,
                tween(80, easing = FastOutSlowInEasing)
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .aspectRatio(1.15f)
            .testTag("circular_detector_meter"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height * 0.52f)
            val radius = size.minDimension * 0.44f

            // 1. Dark Gauge Plate
            drawCircle(
                color = DetectorSurfaceCard,
                radius = radius + 12.dp.toPx(),
                center = center
            )

            // Outer Metallic Bezel Ring
            drawCircle(
                color = ReticleRing,
                radius = radius + 12.dp.toPx(),
                center = center,
                style = Stroke(width = 2.dp.toPx())
            )

            // Inner Reticle Rings
            drawCircle(
                color = ReticleGrid,
                radius = radius * 0.68f,
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )
            drawCircle(
                color = ReticleGrid,
                radius = radius * 0.38f,
                center = center,
                style = Stroke(width = 1.dp.toPx())
            )

            // 2. Arc Track Angles: 240-degree sweep from 150° to 390°
            val startAngle = 150f
            val totalSweep = 240f

            // Background Arc Track
            drawArc(
                color = ReticleGrid,
                startAngle = startAngle,
                sweepAngle = totalSweep,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
            )

            // 3. Tick Marks
            val numTicks = 25
            for (i in 0..numTicks) {
                val tickAngle = startAngle + (i.toFloat() / numTicks) * totalSweep
                val angleRad = (tickAngle * PI / 180.0).toFloat()
                val isMajor = (i % 5 == 0)
                val tickInnerRadius = if (isMajor) radius - 18.dp.toPx() else radius - 12.dp.toPx()
                val tickOuterRadius = radius - 4.dp.toPx()

                val tickColor = when {
                    i > 20 -> CrimsonAlert
                    i > 15 -> AmberRadar
                    else -> ReticleRing
                }

                val p1 = Offset(
                    center.x + tickInnerRadius * cos(angleRad),
                    center.y + tickInnerRadius * sin(angleRad)
                )
                val p2 = Offset(
                    center.x + tickOuterRadius * cos(angleRad),
                    center.y + tickOuterRadius * sin(angleRad)
                )
                drawLine(
                    color = tickColor,
                    start = p1,
                    end = p2,
                    strokeWidth = if (isMajor) 2.5.dp.toPx() else 1.2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }

            // 4. Active Signal Glow Arc
            val activeSweep = (animatedStrength.value / 100f) * totalSweep
            if (activeSweep > 0.5f) {
                val arcBrush = Brush.sweepGradient(
                    0.4f to CyanGlow,
                    0.6f to AmberRadar,
                    0.8f to CrimsonAlert,
                    center = center
                )

                drawArc(
                    brush = arcBrush,
                    startAngle = startAngle,
                    sweepAngle = activeSweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 16.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            // 5. Needle Pointer
            val needleAngle = startAngle + (animatedStrength.value / 100f) * totalSweep
            val needleRad = (needleAngle * PI / 180.0).toFloat()
            val needleLength = radius * 0.92f
            val needleEnd = Offset(
                center.x + needleLength * cos(needleRad),
                center.y + needleLength * sin(needleRad)
            )

            // Needle Line
            drawLine(
                color = if (signal.isThresholdExceeded) CrimsonAlert else AmberRadarLight,
                start = center,
                end = needleEnd,
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Center Hub
            drawCircle(
                color = DetectorDarkBg,
                radius = 16.dp.toPx(),
                center = center
            )
            drawCircle(
                color = if (signal.isThresholdExceeded) CrimsonAlert else CyanGlow,
                radius = 8.dp.toPx(),
                center = center
            )
        }

        // Center Digital Numeric Readout
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 28.dp)
        ) {
            Text(
                text = String.format(Locale.US, "%.1f", signal.filteredMagnitudeUt),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (signal.isThresholdExceeded) CrimsonAlert else TextPrimary
                ),
                fontFamily = FontFamily.Monospace
            )

            Text(
                text = AppStrings.microtesla(isArabic),
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = if (isArabic) 0.5.sp else 1.5.sp,
                    color = CyanGlow
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Delta Badge
            val deltaSign = if (signal.deltaUt >= 0) "+" else ""
            Text(
                text = "Δ $deltaSign${String.format(Locale.US, "%.1f", signal.deltaUt)} µT",
                style = MaterialTheme.typography.labelLarge.copy(
                    color = if (signal.isThresholdExceeded) AmberRadarLight else TextSecondary
                )
            )
        }
    }
}
