package com.example.detection

import com.example.core.SensorReading
import com.example.core.SensorSource
import com.example.core.TargetClassification
import com.example.core.TargetClassificationType
import kotlin.math.abs

class TargetClassifier {

    /**
     * Classifies target based on physics-accurate guidelines.
     * Strictly differentiates between Phone Magnetometer vs External Sensor Hardware.
     */
    fun classify(
        reading: SensorReading,
        deltaUt: Float,
        thresholdUt: Float,
        snrDb: Float,
        goldSilverAnalysis: GoldSilverAnalysisResult? = null
    ): TargetClassification {
        val absDelta = abs(deltaUt)

        if (absDelta < thresholdUt && goldSilverAnalysis?.targetCategory == PreciousMetalCategory.NONE) {
            return TargetClassification.Idle
        }

        // Check if iron notch filtered out this target
        if (goldSilverAnalysis != null && goldSilverAnalysis.isIronFilteredOut) {
            return TargetClassification(
                type = TargetClassificationType.EXTERNAL_FERROUS,
                title = "Ferrous Trash Rejected",
                subtitle = "Iron notch filter actively rejected ferrous target",
                isEstimatedOnly = true,
                isExternalSensorRequired = false,
                ferromagneticScore = 0.95f,
                isIronFilteredOut = true
            )
        }

        // If Gold was identified by the specialized algorithm
        if (goldSilverAnalysis != null && (
            goldSilverAnalysis.targetCategory == PreciousMetalCategory.GOLD_FINE_JEWELRY ||
            goldSilverAnalysis.targetCategory == PreciousMetalCategory.GOLD_NUGGET_NATIVE ||
            goldSilverAnalysis.targetCategory == PreciousMetalCategory.GOLD_COIN_SOLID
        )) {
            val isVerified = goldSilverAnalysis.isHardwareTelemetryVerified
            return TargetClassification(
                type = if (isVerified) TargetClassificationType.EXTERNAL_GOLD_LIKE_ESTIMATED else TargetClassificationType.PHONE_GOLD_TRANSIENT_ESTIMATED,
                title = goldSilverAnalysis.materialSummary,
                subtitle = "Gold Match: ${goldSilverAnalysis.goldProbabilityPct.toInt()}% | VDI: ${goldSilverAnalysis.estimatedVdi?.toInt() ?: "--"} | ${goldSilverAnalysis.conductivityRating}",
                isEstimatedOnly = !isVerified,
                isExternalSensorRequired = !isVerified,
                conductivityVdi = goldSilverAnalysis.estimatedVdi,
                phaseDeg = goldSilverAnalysis.estimatedPhaseDeg,
                ferromagneticScore = 0.02f,
                goldProbabilityPct = goldSilverAnalysis.goldProbabilityPct,
                silverProbabilityPct = goldSilverAnalysis.silverProbabilityPct
            )
        }

        // If Silver was identified by the specialized algorithm
        if (goldSilverAnalysis != null && (
            goldSilverAnalysis.targetCategory == PreciousMetalCategory.SILVER_JEWELRY_RELIC ||
            goldSilverAnalysis.targetCategory == PreciousMetalCategory.SILVER_COIN_BULLION
        )) {
            val isVerified = goldSilverAnalysis.isHardwareTelemetryVerified
            return TargetClassification(
                type = if (isVerified) TargetClassificationType.EXTERNAL_SILVER_LIKE_ESTIMATED else TargetClassificationType.PHONE_SILVER_TRANSIENT_ESTIMATED,
                title = goldSilverAnalysis.materialSummary,
                subtitle = "Silver Match: ${goldSilverAnalysis.silverProbabilityPct.toInt()}% | VDI: ${goldSilverAnalysis.estimatedVdi?.toInt() ?: "--"} | ${goldSilverAnalysis.conductivityRating}",
                isEstimatedOnly = !isVerified,
                isExternalSensorRequired = !isVerified,
                conductivityVdi = goldSilverAnalysis.estimatedVdi,
                phaseDeg = goldSilverAnalysis.estimatedPhaseDeg,
                ferromagneticScore = 0.01f,
                goldProbabilityPct = goldSilverAnalysis.goldProbabilityPct,
                silverProbabilityPct = goldSilverAnalysis.silverProbabilityPct
            )
        }

        val isPhoneMode = reading.source == SensorSource.PHONE_MAGNETOMETER ||
                (reading.source == SensorSource.DEVELOPER_SIMULATION_MODE && reading.conductivityIndex == null)

        if (isPhoneMode) {
            // PHONE-ONLY MAGNETOMETER MODE
            // Strictly detect magnetic/ferrous anomalies only.
            return when {
                absDelta > 50.0f -> {
                    TargetClassification(
                        type = TargetClassificationType.PHONE_STRONG_MAGNETIC_OBJECT,
                        title = "Strong Magnetic Object",
                        subtitle = "High ferromagnetic field distortion (+${String.format("%.1f", absDelta)} µT)",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = false,
                        ferromagneticScore = 0.95f
                    )
                }
                absDelta > 20.0f -> {
                    TargetClassification(
                        type = TargetClassificationType.PHONE_FERROUS_MAGNETIC,
                        title = "Magnetic / Ferrous Anomaly",
                        subtitle = "Ferromagnetic material detected (Iron/Steel/Magnet)",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = false,
                        ferromagneticScore = 0.75f
                    )
                }
                absDelta >= thresholdUt -> {
                    TargetClassification(
                        type = TargetClassificationType.PHONE_WEAK_MAGNETIC_ANOMALY,
                        title = "Weak Magnetic Anomaly",
                        subtitle = "Minor geomagnetic variance (+${String.format("%.1f", absDelta)} µT)",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = false,
                        ferromagneticScore = 0.40f
                    )
                }
                else -> {
                    TargetClassification(
                        type = TargetClassificationType.PHONE_UNKNOWN_METALLIC_ANOMALY,
                        title = "Metallic Anomaly Detected",
                        subtitle = "Material cannot be identified with phone sensor alone",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = true,
                        ferromagneticScore = 0.50f
                    )
                }
            }
        } else {
            // EXTERNAL SENSOR HARDWARE MODE
            // Can process phase angle, conductivity index (VDI 0-100), frequency
            val vdi = reading.conductivityIndex ?: (absDelta * 1.5f).coerceIn(0f, 100f)
            val phase = reading.phaseAngleDeg

            return when {
                vdi < 20f -> {
                    TargetClassification(
                        type = TargetClassificationType.EXTERNAL_FERROUS,
                        title = "Ferrous Metal (Iron / Steel / Relic)",
                        subtitle = "VDI: ${vdi.toInt()} | Low phase conductivity",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = true,
                        conductivityVdi = vdi,
                        phaseDeg = phase,
                        ferromagneticScore = 0.90f
                    )
                }
                vdi in 20f..44f -> {
                    TargetClassification(
                        type = TargetClassificationType.EXTERNAL_ALUMINUM_LIKE_ESTIMATED,
                        title = "Estimated: Aluminum / Foil / Small Alloy",
                        subtitle = "VDI: ${vdi.toInt()} | Moderate conductivity range",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = true,
                        conductivityVdi = vdi,
                        phaseDeg = phase,
                        ferromagneticScore = 0.15f
                    )
                }
                vdi in 45f..64f -> {
                    TargetClassification(
                        type = TargetClassificationType.EXTERNAL_GOLD_LIKE_ESTIMATED,
                        title = "Estimated: Gold-Range Target ID / Jewelry",
                        subtitle = "VDI: ${vdi.toInt()} | Mid-range conductivity (Est. only)",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = true,
                        conductivityVdi = vdi,
                        phaseDeg = phase,
                        ferromagneticScore = 0.05f
                    )
                }
                vdi in 65f..79f -> {
                    TargetClassification(
                        type = TargetClassificationType.EXTERNAL_COPPER_LIKE_ESTIMATED,
                        title = "Estimated: Copper / Brass / Bronze",
                        subtitle = "VDI: ${vdi.toInt()} | High conductivity range",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = true,
                        conductivityVdi = vdi,
                        phaseDeg = phase,
                        ferromagneticScore = 0.05f
                    )
                }
                vdi >= 80f -> {
                    TargetClassification(
                        type = TargetClassificationType.EXTERNAL_SILVER_LIKE_ESTIMATED,
                        title = "Estimated: Silver / Large Coin / Clad",
                        subtitle = "VDI: ${vdi.toInt()} | Very high conductivity phase",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = true,
                        conductivityVdi = vdi,
                        phaseDeg = phase,
                        ferromagneticScore = 0.02f
                    )
                }
                else -> {
                    TargetClassification(
                        type = TargetClassificationType.EXTERNAL_UNKNOWN,
                        title = "Non-Ferrous Anomaly (Unknown Target ID)",
                        subtitle = "External telemetry inconclusive",
                        isEstimatedOnly = true,
                        isExternalSensorRequired = true,
                        conductivityVdi = vdi,
                        phaseDeg = phase,
                        ferromagneticScore = 0.30f
                    )
                }
            }
        }
    }
}
