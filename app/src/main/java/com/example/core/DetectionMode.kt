package com.example.core

enum class DetectionMode(
    val title: String,
    val description: String,
    val isExternalOnly: Boolean = false,
    val defaultFilterAlpha: Float = 0.25f,
    val defaultThresholdUt: Float = 6.0f,
    val sampleRateHz: Int = 50
) {
    QUICK_SCAN(
        title = "Quick Scan",
        description = "Light filtering for rapid sweeping & immediate response",
        isExternalOnly = false,
        defaultFilterAlpha = 0.35f,
        defaultThresholdUt = 5.0f,
        sampleRateHz = 50
    ),
    DEEP_ANALYSIS(
        title = "Deep Signal Analysis",
        description = "Enhanced multi-sample SNR filtering for subtle anomalies",
        isExternalOnly = false,
        defaultFilterAlpha = 0.12f,
        defaultThresholdUt = 3.5f,
        sampleRateHz = 50
    ),
    FERROUS_DETECTION(
        title = "Ferrous Metal Focus",
        description = "Optimized for iron, steel, pipes, and ferromagnetic targets",
        isExternalOnly = false,
        defaultFilterAlpha = 0.20f,
        defaultThresholdUt = 7.0f,
        sampleRateHz = 50
    ),
    NON_FERROUS_DETECTION(
        title = "Non-Ferrous Discrimination",
        description = "Phase & conductivity discrimination (requires external hardware coil)",
        isExternalOnly = true,
        defaultFilterAlpha = 0.20f,
        defaultThresholdUt = 4.0f,
        sampleRateHz = 50
    ),
    EXTERNAL_SENSOR_MODE(
        title = "External Hardware Mode",
        description = "Full telemetry streaming from connected BLE or USB hardware",
        isExternalOnly = true,
        defaultFilterAlpha = 0.25f,
        defaultThresholdUt = 5.0f,
        sampleRateHz = 100
    ),
    EXPERT_MANUAL(
        title = "Expert / Manual Mode",
        description = "Customizable sensitivity, threshold offset, filter, and ground balance",
        isExternalOnly = false,
        defaultFilterAlpha = 0.25f,
        defaultThresholdUt = 6.0f,
        sampleRateHz = 50
    )
}
