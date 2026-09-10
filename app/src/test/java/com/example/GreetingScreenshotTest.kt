package com.example

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import com.example.core.DetectionMode
import com.example.detection.GoldSilverAnalysisResult
import com.example.detection.PreciousMetalCategory
import com.example.ui.components.GoldSilverSpectrumCard
import com.example.ui.theme.MetalScanProTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import com.github.takahirom.roborazzi.captureRoboImage
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel8, sdk = [36])
class GreetingScreenshotTest {

  @get:Rule val composeTestRule = createComposeRule()

  @Test
  fun greeting_screenshot() {
    composeTestRule.setContent {
      MetalScanProTheme {
        GoldSilverSpectrumCard(
          analysis = GoldSilverAnalysisResult(
            goldProbabilityPct = 82f,
            silverProbabilityPct = 15f,
            targetCategory = PreciousMetalCategory.GOLD_COIN_SOLID,
            estimatedVdi = 58f,
            isIronFilteredOut = false,
            isHardwareTelemetryVerified = true
          ),
          currentMode = DetectionMode.GOLD_PROSPECTING,
          appLanguage = "en",
          onModeSelect = {}
        )
      }
    }

    composeTestRule.onRoot().captureRoboImage(filePath = "src/test/screenshots/greeting.png")
  }
}
