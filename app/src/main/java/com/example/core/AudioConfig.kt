package com.example.core

enum class ToneType(val displayName: String) {
    VCO_CONTINUOUS("VCO Variable Pitch"),
    PULSED_CLICKER("Geiger / Pulse Clicker"),
    MULTI_TONE("Multi-Tone Stepped")
}

enum class VibrationMode(val displayName: String) {
    OFF("Disabled"),
    SHORT_PULSE("Short Pulse on Threshold"),
    PROPORTIONAL_PULSE("Proportional Frequency Pulse"),
    CONTINUOUS_INTENSITY("Continuous Proportional Intensity")
}

data class AudioFeedbackConfig(
    val isEnabled: Boolean = true,
    val toneType: ToneType = ToneType.VCO_CONTINUOUS,
    val minSignalThresholdPct: Float = 10f, // only play when signal > 10%
    val volume: Float = 0.8f,
    val minPitchHz: Float = 240f,
    val maxPitchHz: Float = 2200f
)

data class VibrationFeedbackConfig(
    val isEnabled: Boolean = true,
    val mode: VibrationMode = VibrationMode.PROPORTIONAL_PULSE,
    val thresholdPct: Float = 15f
)

data class ExpertSettings(
    val sensitivity: Float = 75f, // 1 - 100
    val thresholdOffsetUt: Float = 6.0f, // µT
    val filterAlpha: Float = 0.25f, // 0.05 to 0.95
    val samplingRateHz: Int = 50,
    val groundBalanceUt: Float = 0f,
    val signalGain: Float = 1.0f,
    val continuousDriftComp: Boolean = true,
    val detectionDelayMs: Long = 0L
)
