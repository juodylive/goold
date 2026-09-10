package com.example

import com.example.core.DetectionMode
import com.example.core.SensorReading
import com.example.core.SensorSource
import com.example.detection.GoldSilverAnalysisResult
import com.example.detection.GoldSilverDetectionAlgorithm
import com.example.detection.PreciousMetalCategory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GoldSilverAlgorithmTest {

    private lateinit var algorithm: GoldSilverDetectionAlgorithm

    @Before
    fun setUp() {
        algorithm = GoldSilverDetectionAlgorithm()
    }

    @Test
    fun testGoldDetectionWithHardwareTelemetry() {
        // Gold VDI around 54, Phase around 52 degrees
        val reading = SensorReading(
            timestamp = System.currentTimeMillis(),
            magnitudeUt = 68.0f,
            xUt = 25f,
            yUt = 30f,
            zUt = 55f,
            source = SensorSource.EXTERNAL_BLUETOOTH_BLE,
            conductivityIndex = 54.0f,
            phaseAngleDeg = 52.0f
        )

        val result: GoldSilverAnalysisResult = algorithm.analyze(
            reading = reading,
            deltaUt = 20.0f,
            filteredUt = 68.0f,
            thresholdUt = 4.0f,
            snrDb = 25.0f,
            stabilityPct = 95.0f,
            mode = DetectionMode.GOLD_PROSPECTING
        )

        assertTrue("Gold probability should be high for VDI 54", result.goldProbabilityPct > 70f)
        assertTrue("Silver probability should be lower than gold", result.silverProbabilityPct < result.goldProbabilityPct)
        assertEquals(PreciousMetalCategory.GOLD_NUGGET_NATIVE, result.targetCategory)
        assertTrue(result.isHardwareTelemetryVerified)
        assertFalse("Gold should not be filtered out by notch", result.isIronFilteredOut)
    }

    @Test
    fun testSilverDetectionWithHardwareTelemetry() {
        // Silver VDI around 91, Phase around 84 degrees
        val reading = SensorReading(
            timestamp = System.currentTimeMillis(),
            magnitudeUt = 75.0f,
            xUt = 25f,
            yUt = 30f,
            zUt = 60f,
            source = SensorSource.EXTERNAL_BLUETOOTH_BLE,
            conductivityIndex = 91.0f,
            phaseAngleDeg = 84.0f
        )

        val result: GoldSilverAnalysisResult = algorithm.analyze(
            reading = reading,
            deltaUt = 27.0f,
            filteredUt = 75.0f,
            thresholdUt = 4.0f,
            snrDb = 28.0f,
            stabilityPct = 96.0f,
            mode = DetectionMode.SILVER_COIN_CACHE
        )

        assertTrue("Silver probability should be high for VDI 91", result.silverProbabilityPct > 75f)
        assertTrue("Gold probability should be low for VDI 91", result.goldProbabilityPct < 30f)
        assertEquals(PreciousMetalCategory.SILVER_COIN_BULLION, result.targetCategory)
        assertTrue(result.isHardwareTelemetryVerified)
        assertFalse(result.isIronFilteredOut)
    }

    @Test
    fun testIronRejectionInGoldProspectingMode() {
        // Iron nail: low VDI (14.0)
        val reading = SensorReading(
            timestamp = System.currentTimeMillis(),
            magnitudeUt = 95.0f,
            xUt = 30f,
            yUt = 35f,
            zUt = 80f,
            source = SensorSource.EXTERNAL_BLUETOOTH_BLE,
            conductivityIndex = 14.0f,
            phaseAngleDeg = 12.0f
        )

        val result: GoldSilverAnalysisResult = algorithm.analyze(
            reading = reading,
            deltaUt = 47.0f,
            filteredUt = 95.0f,
            thresholdUt = 4.0f,
            snrDb = 22.0f,
            stabilityPct = 90.0f,
            mode = DetectionMode.GOLD_PROSPECTING
        )

        assertEquals(PreciousMetalCategory.FERROUS_REJECTED, result.targetCategory)
        assertTrue("Iron should be rejected by notch in gold mode", result.isIronFilteredOut)
        assertEquals(0f, result.goldProbabilityPct, 0.01f)
        assertEquals(0f, result.silverProbabilityPct, 0.01f)
    }
}
