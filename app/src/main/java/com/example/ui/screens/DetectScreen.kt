package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.SensorSource
import com.example.ui.components.CircularDetectorMeter
import com.example.ui.components.HonestDisclaimerBanner
import com.example.ui.components.RealtimeDetectorGraph
import com.example.ui.components.SignalReadoutCards
import com.example.ui.components.TargetClassificationBadge
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
fun DetectScreen(
    viewModel: DetectorViewModel,
    onNavigateCalibration: () -> Unit,
    onNavigateSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val processedSignal by viewModel.processedSignal.collectAsStateWithLifecycle()
    val isDetecting by viewModel.isDetecting.collectAsStateWithLifecycle()
    val graphPoints by viewModel.graphPoints.collectAsStateWithLifecycle()
    val timeWindow by viewModel.graphTimeWindowSec.collectAsStateWithLifecycle()
    val currentMode by viewModel.currentMode.collectAsStateWithLifecycle()
    val audioConfig by viewModel.audioConfig.collectAsStateWithLifecycle()
    val activeSensor by viewModel.activeSensor.collectAsStateWithLifecycle()

    var showSaveDialog by remember { mutableStateOf(false) }
    var notesText by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DetectorDarkBg)
            .verticalScroll(scrollState)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag("detect_screen"),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Mode & Quick Toggles Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DetectorSurfaceCard, RoundedCornerShape(8.dp))
                .border(1.dp, DetectorSurfaceBorder, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "MODE",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 9.sp)
                )
                Text(
                    text = currentMode.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = AmberRadar,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Audio Quick Toggle
                IconButton(
                    onClick = { viewModel.updateAudioConfig(audioConfig.copy(isEnabled = !audioConfig.isEnabled)) },
                    modifier = Modifier.size(36.dp).testTag("quick_audio_toggle")
                ) {
                    Icon(
                        imageVector = if (audioConfig.isEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Audio Feedback",
                        tint = if (audioConfig.isEnabled) CyanGlow else TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Zero Baseline Quick Button
                OutlinedButton(
                    onClick = { viewModel.resetBaseline() },
                    modifier = Modifier.height(32.dp).testTag("quick_zero_baseline_btn"),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldSignal),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSignal.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.CleaningServices,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "ZERO", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Circular Gauge Meter
        CircularDetectorMeter(
            signal = processedSignal,
            isDetecting = isDetecting
        )

        // Metrics Readout Cards
        SignalReadoutCards(
            signal = processedSignal
        )

        // Target Classification Badge
        TargetClassificationBadge(
            classification = processedSignal.classification
        )

        // Real-Time Scrolling Graph
        RealtimeDetectorGraph(
            points = graphPoints,
            selectedWindowSec = timeWindow,
            onSelectWindow = { viewModel.setGraphTimeWindow(it) }
        )

        // Primary Control Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Start / Stop Main Action Button
            Button(
                onClick = {
                    if (isDetecting) viewModel.stopDetection() else viewModel.startDetection()
                },
                modifier = Modifier
                    .weight(1.3f)
                    .height(48.dp)
                    .testTag("start_stop_detect_btn"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDetecting) CrimsonAlert else AmberRadar,
                    contentColor = DetectorDarkBg
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = if (isDetecting) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isDetecting) "STOP SCAN" else "START SCAN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            // Calibrate Button
            FilledTonalButton(
                onClick = onNavigateCalibration,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("nav_calibrate_btn"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = DetectorSurfaceCard,
                    contentColor = CyanGlow
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "CALIBRATE", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            // Log Detection Event Button
            FilledTonalButton(
                onClick = { showSaveDialog = true },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("log_detection_btn"),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = DetectorSurfaceCard,
                    contentColor = EmeraldSignal
                )
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkAdd,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "LOG", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Physics Disclaimer Banner
        HonestDisclaimerBanner()

        Spacer(modifier = Modifier.height(12.dp))
    }

    // Save Detection Log Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            title = {
                Text(
                    text = "Save Detection Event",
                    style = MaterialTheme.typography.titleLarge.copy(color = TextPrimary)
                )
            },
            text = {
                Column {
                    Text(
                        text = "Peak: ${String.format("%.1f", processedSignal.rawMagnitudeUt)} µT (Δ ${String.format("%.1f", processedSignal.deltaUt)} µT)\nTarget: ${processedSignal.classification.title}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        label = { Text("Field Notes & Location Info") },
                        placeholder = { Text("e.g., Depth ~10cm in backyard, rocky ground...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_notes_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberRadar,
                            unfocusedBorderColor = DetectorSurfaceBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.saveCurrentDetectionLog(notesText)
                        notesText = ""
                        showSaveDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberRadar, contentColor = DetectorDarkBg),
                    modifier = Modifier.testTag("save_confirm_btn")
                ) {
                    Text("Save to History", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showSaveDialog = false }
                ) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = DetectorSurfaceCard
        )
    }
}
