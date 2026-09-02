package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberRadar
import com.example.ui.theme.CrimsonAlert
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DetectorDarkBg
import com.example.ui.theme.DetectorSurfaceBorder
import com.example.ui.theme.DetectorSurfaceCard
import com.example.ui.theme.EmeraldSignal
import com.example.ui.theme.ReticleGrid
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.viewmodel.GraphPoint
import java.util.Locale
import kotlin.math.max
import kotlin.math.min

@Composable
fun RealtimeDetectorGraph(
    points: List<GraphPoint>,
    selectedWindowSec: Int,
    onSelectWindow: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DetectorSurfaceCard, RoundedCornerShape(12.dp))
            .border(1.dp, DetectorSurfaceBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("realtime_detector_graph")
    ) {
        // Top Legend & Title
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "OSCILLOGRAM (µT)",
                style = MaterialTheme.typography.labelLarge.copy(color = CyanGlow)
            )

            // Legend indicators
            Row(verticalAlignment = Alignment.CenterVertically) {
                LegendIndicator(color = TextMuted, label = "Raw")
                Spacer(modifier = Modifier.width(8.dp))
                LegendIndicator(color = CyanGlow, label = "Filtered")
                Spacer(modifier = Modifier.width(8.dp))
                LegendIndicator(color = EmeraldSignal, label = "Base")
                Spacer(modifier = Modifier.width(8.dp))
                LegendIndicator(color = AmberRadar, label = "Threshold")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Canvas Waveform Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .background(DetectorDarkBg, RoundedCornerShape(8.dp))
                .border(1.dp, ReticleGrid, RoundedCornerShape(8.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize().padding(horizontal = 6.dp, vertical = 6.dp)) {
                val width = size.width
                val height = size.height

                // Draw Grid Lines (Horizontal & Vertical)
                val gridYCount = 4
                for (i in 0..gridYCount) {
                    val y = height * (i.toFloat() / gridYCount)
                    drawLine(
                        color = ReticleGrid,
                        start = Offset(0f, y),
                        end = Offset(width, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                val gridXCount = 6
                for (i in 0..gridXCount) {
                    val x = width * (i.toFloat() / gridXCount)
                    drawLine(
                        color = ReticleGrid,
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                if (points.size < 2) return@Canvas

                val now = System.currentTimeMillis()
                val windowMs = selectedWindowSec * 1000L
                val startTime = now - windowMs

                // Determine min/max Y scale
                var minY = Float.MAX_VALUE
                var maxY = Float.MIN_VALUE

                for (p in points) {
                    minY = min(minY, min(p.rawUt, p.baselineUt - 5f))
                    maxY = max(maxY, max(p.rawUt, p.thresholdUt + 10f))
                }

                // Ensure a sensible minimum Y span
                if (maxY - minY < 20f) {
                    val mid = (maxY + minY) / 2f
                    minY = mid - 10f
                    maxY = mid + 10f
                }

                val yRange = (maxY - minY).coerceAtLeast(1f)

                fun mapX(t: Long): Float {
                    val norm = (t - startTime).toFloat() / windowMs
                    return (norm * width).coerceIn(0f, width)
                }

                fun mapY(valUt: Float): Float {
                    val norm = (valUt - minY) / yRange
                    return height - (norm * height).coerceIn(0f, height)
                }

                val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                // 1. Draw Baseline Line
                val latestBaseline = points.lastOrNull()?.baselineUt ?: 48f
                val baselineY = mapY(latestBaseline)
                drawLine(
                    color = EmeraldSignal.copy(alpha = 0.7f),
                    start = Offset(0f, baselineY),
                    end = Offset(width, baselineY),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = dashedEffect
                )

                // 2. Draw Threshold Line
                val latestThreshold = points.lastOrNull()?.thresholdUt ?: 54f
                val thresholdY = mapY(latestThreshold)
                drawLine(
                    color = AmberRadar.copy(alpha = 0.8f),
                    start = Offset(0f, thresholdY),
                    end = Offset(width, thresholdY),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = dashedEffect
                )

                // 3. Draw Raw Signal Path (Muted line)
                val rawPath = Path()
                var rawStarted = false
                for (p in points) {
                    val x = mapX(p.timestampMs)
                    val y = mapY(p.rawUt)
                    if (!rawStarted) {
                        rawPath.moveTo(x, y)
                        rawStarted = true
                    } else {
                        rawPath.lineTo(x, y)
                    }
                }
                drawPath(
                    path = rawPath,
                    color = TextMuted.copy(alpha = 0.45f),
                    style = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)
                )

                // 4. Draw Filtered Signal Path (Glowing Cyan)
                val filteredPath = Path()
                var filterStarted = false
                for (p in points) {
                    val x = mapX(p.timestampMs)
                    val y = mapY(p.filteredUt)
                    if (!filterStarted) {
                        filteredPath.moveTo(x, y)
                        filterStarted = true
                    } else {
                        filteredPath.lineTo(x, y)
                    }
                }
                drawPath(
                    path = filteredPath,
                    color = CyanGlow,
                    style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Time Window Selection Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val windows = listOf(
                10 to "10s",
                30 to "30s",
                60 to "60s",
                300 to "5m"
            )

            windows.forEach { (sec, label) ->
                val isSelected = selectedWindowSec == sec
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectWindow(sec) },
                    label = { Text(label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AmberRadar.copy(alpha = 0.2f),
                        selectedLabelColor = AmberRadar,
                        containerColor = DetectorDarkBg,
                        labelColor = TextSecondary
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = isSelected,
                        borderColor = if (isSelected) AmberRadar else ReticleGrid
                    ),
                    modifier = Modifier.height(30.dp)
                )
            }
        }
    }
}

@Composable
private fun LegendIndicator(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(color, RoundedCornerShape(2.dp))
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            color = TextSecondary,
            fontFamily = FontFamily.Monospace
        )
    }
}
