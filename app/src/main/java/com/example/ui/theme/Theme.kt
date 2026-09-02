package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val TacticalDetectorDarkColorScheme = darkColorScheme(
    primary = AmberRadar,
    onPrimary = DetectorDarkBg,
    primaryContainer = DetectorSurfaceCard,
    onPrimaryContainer = AmberRadarLight,
    secondary = CyanGlow,
    onSecondary = DetectorDarkBg,
    secondaryContainer = DetectorSurfaceCard,
    onSecondaryContainer = CyanGlowBright,
    tertiary = EmeraldSignal,
    onTertiary = DetectorDarkBg,
    background = DetectorDarkBg,
    onBackground = TextPrimary,
    surface = DetectorSurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = DetectorSurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = DetectorSurfaceBorder,
    error = CrimsonAlert,
    onError = TextPrimary
)

@Composable
fun MetalScanProTheme(
    content: @Composable () -> Unit
) {
    // Professional instrument dark theme by default for measuring equipment
    MaterialTheme(
        colorScheme = TacticalDetectorDarkColorScheme,
        typography = Typography,
        content = content
    )
}
