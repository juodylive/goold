package com.example.core

enum class TargetClassificationType {
    // Phone-Only Magnetometer Classifications (Honest physics)
    PHONE_NO_ANOMALY,
    PHONE_WEAK_MAGNETIC_ANOMALY,
    PHONE_STRONG_MAGNETIC_OBJECT,
    PHONE_FERROUS_MAGNETIC,
    PHONE_UNKNOWN_METALLIC_ANOMALY,

    // External Sensor Classifications (Based on phase & conductivity VDI data)
    EXTERNAL_FERROUS,
    EXTERNAL_NON_FERROUS_GENERAL,
    EXTERNAL_GOLD_LIKE_ESTIMATED,
    EXTERNAL_SILVER_LIKE_ESTIMATED,
    EXTERNAL_COPPER_LIKE_ESTIMATED,
    EXTERNAL_ALUMINUM_LIKE_ESTIMATED,
    EXTERNAL_UNKNOWN,

    // Dynamic Motion / Induction Classifications
    PHONE_GOLD_TRANSIENT_ESTIMATED,
    PHONE_SILVER_TRANSIENT_ESTIMATED
}

data class TargetClassification(
    val type: TargetClassificationType,
    val title: String,
    val subtitle: String,
    val isEstimatedOnly: Boolean = true,
    val isExternalSensorRequired: Boolean = false,
    val conductivityVdi: Float? = null,
    val phaseDeg: Float? = null,
    val ferromagneticScore: Float = 0f, // 0.0 (non-magnetic) to 1.0 (strongly magnetic)
    val goldProbabilityPct: Float = 0f, // 0 to 100%
    val silverProbabilityPct: Float = 0f, // 0 to 100%
    val isIronFilteredOut: Boolean = false
) {
    companion object {
        val Idle = TargetClassification(
            type = TargetClassificationType.PHONE_NO_ANOMALY,
            title = "No Magnetic Anomaly",
            subtitle = "Ambient geomagnetic baseline normal",
            isEstimatedOnly = false,
            ferromagneticScore = 0f
        )
    }
}
