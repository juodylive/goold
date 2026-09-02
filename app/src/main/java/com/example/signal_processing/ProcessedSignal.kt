package com.example.signal_processing

import com.example.core.SensorReading
import com.example.core.TargetClassification

data class ProcessedSignal(
    val rawReading: SensorReading,
    val rawMagnitudeUt: Float,
    val filteredMagnitudeUt: Float,
    val baselineUt: Float,
    val deltaUt: Float, // filtered - baseline
    val signalStrengthPct: Float, // 0 to 100%
    val signalStabilityPct: Float, // 0 to 100% (inverse of jitter variance)
    val snrDb: Float, // Signal-to-noise ratio in dB
    val detectionConfidencePct: Float, // 0 to 100%
    val isThresholdExceeded: Boolean,
    val effectiveThresholdUt: Float,
    val isPeakDetected: Boolean,
    val peakValueUt: Float,
    val classification: TargetClassification
)
