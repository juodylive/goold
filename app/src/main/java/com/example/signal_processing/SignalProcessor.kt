package com.example.signal_processing

import com.example.core.DetectionMode
import com.example.core.ExpertSettings
import com.example.core.SensorReading
import com.example.detection.TargetClassifier
import kotlin.math.abs
import kotlin.math.max

class SignalProcessor(
    private val targetClassifier: TargetClassifier = TargetClassifier()
) {
    // Internal Filter States
    private var lastFilteredUt: Float = 48.0f
    private var baselineUt: Float = 48.0f
    private var isBaselineCalibrated: Boolean = false

    // Sliding Window for Stability & Noise Floor (last 30 samples)
    private val recentSamples = FloatArray(30)
    private var sampleIndex = 0
    private var sampleCount = 0

    // Peak Tracking
    private var recentPeakUt: Float = 0f
    private var peakDecayTick: Int = 0

    var currentMode: DetectionMode = DetectionMode.QUICK_SCAN
    var expertSettings: ExpertSettings = ExpertSettings()
    var calibratedNoiseFloor: Float = 0.4f
    var manualBaselineOffset: Float = 0.0f

    fun setBaseline(newBaseline: Float) {
        baselineUt = newBaseline
        isBaselineCalibrated = true
        lastFilteredUt = newBaseline
    }

    fun resetBaselineToCurrent() {
        baselineUt = lastFilteredUt
        isBaselineCalibrated = true
    }

    fun process(reading: SensorReading): ProcessedSignal {
        val rawMag = reading.magnitudeUt

        // 1. Outlier Rejection
        val cleaned = FilterUtils.rejectOutlier(rawMag, lastFilteredUt, maxStep = 120f)

        // 2. Determine Alpha from Mode / Settings
        val effectiveAlpha = if (currentMode == DetectionMode.EXPERT_MANUAL) {
            expertSettings.filterAlpha
        } else {
            currentMode.defaultFilterAlpha
        }

        // 3. Low-Pass Exponential Smoothing
        val filtered = FilterUtils.applyEma(cleaned, lastFilteredUt, effectiveAlpha)
        lastFilteredUt = filtered

        // Auto-initialize baseline on very first sample if uncalibrated
        if (!isBaselineCalibrated) {
            baselineUt = filtered
            isBaselineCalibrated = true
        }

        // 4. Update Ring Buffer for Noise & Stability Analysis
        recentSamples[sampleIndex] = filtered
        sampleIndex = (sampleIndex + 1) % recentSamples.size
        if (sampleCount < recentSamples.size) sampleCount++

        // 5. Environmental Drift Compensation (Slow continuous tracking when signal is quiet)
        val deltaRaw = filtered - (baselineUt + manualBaselineOffset)
        val shouldCompensate = if (currentMode == DetectionMode.EXPERT_MANUAL) {
            expertSettings.continuousDriftComp
        } else {
            true
        }

        if (shouldCompensate && abs(deltaRaw) < 1.5f) {
            // Very slow drift tracking: tau ~ 15-20 seconds
            baselineUt = (0.002f * filtered) + (0.998f * baselineUt)
        }

        val effectiveBaseline = baselineUt + manualBaselineOffset
        val delta = filtered - effectiveBaseline

        // 6. Threshold Determination
        val baseThreshold = if (currentMode == DetectionMode.EXPERT_MANUAL) {
            expertSettings.thresholdOffsetUt
        } else {
            currentMode.defaultThresholdUt
        }

        // Sensitivity scaling (1 - 100): scale factor from 2.0 (low sens) down to 0.5 (max sens)
        val sensitivityFactor = if (currentMode == DetectionMode.EXPERT_MANUAL) {
            (100f - expertSettings.sensitivity).coerceIn(0f, 100f) / 50f + 0.3f
        } else 1.0f

        val effectiveThreshold = max(1.5f, baseThreshold * sensitivityFactor)

        // 7. Calculate Variance / Jitter for Stability Percentage
        val stdDev = FilterUtils.computeStandardDeviation(recentSamples.copyOf(sampleCount))
        val stabilityPct = (100f - (stdDev * 25f)).coerceIn(10f, 100f)

        // 8. Signal-to-Noise Ratio (SNR)
        val currentNoise = max(calibratedNoiseFloor, stdDev)
        val snrDb = FilterUtils.estimateSnrDb(delta, currentNoise)

        // 9. Signal Strength Percentage (0 - 100%)
        // Maps delta logarithmic/linear scale up to 100 µT
        val absDelta = abs(delta)
        val rawStrength = (absDelta / (effectiveThreshold * 8f)) * 100f
        val signalStrengthPct = rawStrength.coerceIn(0f, 100f)

        // 10. Peak Detection
        val isThresholdExceeded = absDelta >= effectiveThreshold
        if (absDelta > recentPeakUt) {
            recentPeakUt = absDelta
            peakDecayTick = 0
        } else {
            peakDecayTick++
            if (peakDecayTick > 40) { // decay peak after ~1-2 seconds
                recentPeakUt *= 0.96f
            }
        }

        // 11. Detection Confidence
        val confidencePct = if (isThresholdExceeded) {
            val snrFactor = (snrDb / 30f).coerceIn(0.2f, 1.0f)
            val stabFactor = (stabilityPct / 100f)
            ((snrFactor * 0.6f + stabFactor * 0.4f) * 100f).coerceIn(20f, 99f)
        } else 0f

        // 12. Classification
        val classification = targetClassifier.classify(
            reading = reading,
            deltaUt = delta,
            thresholdUt = effectiveThreshold,
            snrDb = snrDb
        )

        return ProcessedSignal(
            rawReading = reading,
            rawMagnitudeUt = rawMag,
            filteredMagnitudeUt = filtered,
            baselineUt = effectiveBaseline,
            deltaUt = delta,
            signalStrengthPct = signalStrengthPct,
            signalStabilityPct = stabilityPct,
            snrDb = snrDb,
            detectionConfidencePct = confidencePct,
            isThresholdExceeded = isThresholdExceeded,
            effectiveThresholdUt = effectiveThreshold,
            isPeakDetected = (recentPeakUt > effectiveThreshold && absDelta > (recentPeakUt * 0.85f)),
            peakValueUt = recentPeakUt,
            classification = classification
        )
    }
}
