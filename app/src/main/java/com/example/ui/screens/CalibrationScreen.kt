package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.calibration.CalibrationQuality
import com.example.calibration.CalibrationStep
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
import java.util.Locale
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CalibrationScreen(
    viewModel: DetectorViewModel,
    modifier: Modifier = Modifier
) {
    val calibrationManager = viewModel.calibrationManager
    val currentStep by calibrationManager.currentStep.collectAsStateWithLifecycle()
    val progressPct by calibrationManager.progressPct.collectAsStateWithLifecycle()
    val statusText by calibrationManager.statusText.collectAsStateWithLifecycle()
    val lastResult by calibrationManager.lastResult.collectAsStateWithLifecycle()
    val expertSettings by viewModel.expertSettings.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DetectorDarkBg)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("calibration_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Header
        Text(
            text = "SENSOR CALIBRATION & SELF-TEST",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        // Guide Instructions Card with Figure-8 Animation
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DetectorSurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DetectorSurfaceBorder)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated Figure-8 Trajectory Canvas
                FigureEightAnimation(
                    isCalibrating = currentStep != CalibrationStep.IDLE && currentStep != CalibrationStep.COMPLETED,
                    modifier = Modifier
                        .size(180.dp, 80.dp)
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "Guided Calibration Procedure",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = AmberRadar
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Move the phone away from metal objects and slowly rotate it through different orientations in a figure-8 motion.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondary,
                        lineHeight = 20.sp
                    ),
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { progressPct },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .testTag("calib_progress_bar"),
                    color = AmberRadar,
                    trackColor = DetectorDarkBg
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = CyanGlow,
                        fontFamily = FontFamily.Monospace
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Start Calibration Button
                Button(
                    onClick = {
                        viewModel.performGuidedCalibration()
                    },
                    enabled = currentStep == CalibrationStep.IDLE || currentStep == CalibrationStep.COMPLETED,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("start_calibration_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AmberRadar,
                        contentColor = DetectorDarkBg,
                        disabledContainerColor = DetectorSurfaceBorder,
                        disabledContentColor = TextSecondary
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = if (currentStep == CalibrationStep.COMPLETED) Icons.Default.Refresh else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (currentStep == CalibrationStep.COMPLETED) "RE-CALIBRATE SENSOR" else "START GUIDED CALIBRATION",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // 10-Step Workflow Status Checklist
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DetectorSurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DetectorSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "CALIBRATION PIPELINE PROTOCOL",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, letterSpacing = 1.sp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                val steps = listOf(
                    "1. Sensor hardware registers self-test",
                    "2. Ambient geomagnetic field baseline capture",
                    "3. Multi-sample spatial figure-8 acquisition",
                    "4. Geometric mean baseline calculation",
                    "5. Noise floor variance & standard deviation analysis",
                    "6. Dynamic signal trigger threshold determination",
                    "7. Thermal & environmental drift tracking configuration",
                    "8. Sensitivity coefficient optimization",
                    "9. Sensor accuracy & calibration quality assessment"
                )

                steps.forEachIndexed { index, stepName ->
                    val isDone = (progressPct >= (index + 1) * 0.11f) || currentStep == CalibrationStep.COMPLETED
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    if (isDone) EmeraldSignal else DetectorSurfaceBorder,
                                    CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = stepName,
                            fontSize = 12.sp,
                            color = if (isDone) TextPrimary else TextSecondary,
                            fontFamily = FontFamily.Default
                        )
                    }
                }
            }
        }

        // Calibration Quality & Results Display Card
        lastResult?.let { res ->
            Card(
                modifier = Modifier.fillMaxWidth().testTag("calibration_results_card"),
                colors = CardDefaults.cardColors(containerColor = DetectorSurfaceCard),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSignal.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CALIBRATION REPORT",
                            style = MaterialTheme.typography.labelSmall.copy(color = EmeraldSignal, letterSpacing = 1.sp)
                        )
                        val qualityBadgeColor = when (res.quality) {
                            CalibrationQuality.EXCELLENT -> EmeraldSignal
                            CalibrationQuality.GOOD -> AmberRadar
                            CalibrationQuality.POOR -> CrimsonAlert
                        }
                        Text(
                            text = res.quality.name,
                            style = MaterialTheme.typography.labelLarge.copy(
                                color = qualityBadgeColor,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .background(qualityBadgeColor.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Calibrated Baseline", fontSize = 11.sp, color = TextSecondary)
                            Text("${String.format(Locale.US, "%.1f", res.baselineUt)} µT", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary, fontFamily = FontFamily.Monospace)
                        }
                        Column {
                            Text("Noise Floor (σ)", fontSize = 11.sp, color = TextSecondary)
                            Text("±${String.format(Locale.US, "%.2f", res.noiseFloorUt)} µT", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = CyanGlow, fontFamily = FontFamily.Monospace)
                        }
                        Column {
                            Text("Trigger Threshold", fontSize = 11.sp, color = TextSecondary)
                            Text("${String.format(Locale.US, "%.1f", res.recommendedThresholdUt)} µT", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = AmberRadar, fontFamily = FontFamily.Monospace)
                        }
                    }
                }
            }
        }

        // Manual Sensitivity Tuning
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DetectorSurfaceCard),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, DetectorSurfaceBorder)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "MANUAL SENSITIVITY ADJUSTMENT",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )
                    Text(
                        text = "${expertSettings.sensitivity.toInt()}%",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = AmberRadar,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    )
                }

                Slider(
                    value = expertSettings.sensitivity,
                    onValueChange = { viewModel.updateExpertSettings(expertSettings.copy(sensitivity = it)) },
                    valueRange = 10f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = AmberRadar,
                        activeTrackColor = AmberRadar,
                        inactiveTrackColor = DetectorDarkBg
                    ),
                    modifier = Modifier.testTag("calib_sensitivity_slider")
                )

                Text(
                    text = "Higher sensitivity detects smaller anomalies but may increase sensitivity to device movement.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun FigureEightAnimation(
    isCalibrating: Boolean,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "Figure8")
    val animTime by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Fig8Angle"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f
        val rx = w * 0.38f
        val ry = h * 0.38f

        // Draw background Lemniscate of Bernoulli (Figure 8 Path)
        val path = Path()
        val steps = 80
        for (i in 0..steps) {
            val t = (i.toFloat() / steps) * (2 * PI.toFloat())
            val scale = 2 / (3 - cos(2 * t))
            val x = cx + rx * scale * cos(t)
            val y = cy + ry * scale * sin(2 * t) / 2
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        drawPath(
            path = path,
            color = DetectorSurfaceBorder,
            style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        )

        // Draw traveling pointer orb
        val activeT = if (isCalibrating) animTime else 0f
        val activeScale = 2 / (3 - cos(2 * activeT))
        val orbX = cx + rx * activeScale * cos(activeT)
        val orbY = cy + ry * activeScale * sin(2 * activeT) / 2

        drawCircle(
            color = if (isCalibrating) AmberRadar else CyanGlow,
            radius = 6.dp.toPx(),
            center = Offset(orbX, orbY)
        )
    }
}
