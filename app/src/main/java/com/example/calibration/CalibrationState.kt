package com.example.calibration

enum class CalibrationStep {
    IDLE,
    STEP_1_SELF_TEST,
    STEP_2_AMBIENT_MEASURE,
    STEP_3_SAMPLING_FIGURE_EIGHT,
    STEP_4_COMPUTING_BASELINE,
    STEP_5_NOISE_ANALYSIS,
    STEP_6_DYNAMIC_THRESHOLD,
    STEP_7_DRIFT_COMPENSATION,
    STEP_8_SENSITIVITY_CHECK,
    STEP_9_QUALITY_RATING,
    COMPLETED
}

enum class CalibrationQuality {
    EXCELLENT, // Low noise (<0.3 µT), clean geomagnetic field
    GOOD,      // Moderate noise (0.3 - 0.8 µT)
    POOR       // High electromagnetic interference (>0.8 µT)
}

data class CalibrationResult(
    val baselineUt: Float = 48.0f,
    val noiseFloorUt: Float = 0.35f,
    val standardDeviationUt: Float = 0.25f,
    val recommendedThresholdUt: Float = 5.0f,
    val quality: CalibrationQuality = CalibrationQuality.GOOD,
    val sampleCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)
