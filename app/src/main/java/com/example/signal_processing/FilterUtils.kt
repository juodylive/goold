package com.example.signal_processing

import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.sqrt

object FilterUtils {

    /**
     * Exponential Moving Average (1st order low-pass IIR filter)
     * y[n] = alpha * x[n] + (1 - alpha) * y[n-1]
     */
    fun applyEma(current: Float, previousFiltered: Float, alpha: Float): Float {
        val clampedAlpha = alpha.coerceIn(0.01f, 1.0f)
        return (clampedAlpha * current) + ((1.0f - clampedAlpha) * previousFiltered)
    }

    /**
     * Outlier Rejection: Clamps spike if it exceeds statistical max delta in a single tick
     */
    fun rejectOutlier(sample: Float, lastValid: Float, maxStep: Float = 150f): Float {
        val diff = sample - lastValid
        return if (abs(diff) > maxStep) {
            lastValid + (if (diff > 0) maxStep else -maxStep)
        } else {
            sample
        }
    }

    /**
     * Computes Standard Deviation of a float buffer
     */
    fun computeStandardDeviation(samples: FloatArray): Float {
        if (samples.isEmpty()) return 0f
        var sum = 0f
        for (s in samples) sum += s
        val mean = sum / samples.size

        var varianceSum = 0f
        for (s in samples) {
            val d = s - mean
            varianceSum += d * d
        }
        return sqrt(varianceSum / samples.size)
    }

    /**
     * Estimate Signal-to-Noise Ratio (SNR) in dB
     * SNR_dB = 20 * log10(signalDelta / noiseFloor)
     */
    fun estimateSnrDb(delta: Float, noiseFloor: Float): Float {
        val safeNoise = noiseFloor.coerceAtLeast(0.05f)
        val safeDelta = abs(delta).coerceAtLeast(0.01f)
        val ratio = safeDelta / safeNoise
        return (20f * log10(ratio)).coerceIn(0f, 60f)
    }
}
