package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class CockpitColors(
  val isDaylight: Boolean,
  val background: Color,
  val surface: Color,
  val cardBackground: Color,
  val cardBorder: Color,
  val textPrimary: Color,
  val textSecondary: Color,
  val primaryAccent: Color,
  val onPrimaryAccent: Color,
  val liveAmber: Color,
  val onLiveAmber: Color,
  val liveAmberBg: Color,
  val liveAmberBorder: Color,
  val surfaceContainer: Color,
  val surfaceHigh: Color,
  val statusEmerald: Color,
  val activeBorderHighlight: Color
)

val LocalCockpitColors = staticCompositionLocalOf {
  CockpitColors(
    isDaylight = true,
    background = DaylightBackground,
    surface = DaylightSurface,
    cardBackground = DaylightSurfaceCard,
    cardBorder = DaylightBorder,
    textPrimary = DaylightOnSurface,
    textSecondary = DaylightOnSurfaceVariant,
    primaryAccent = DaylightPrimary,
    onPrimaryAccent = Color.White,
    liveAmber = DaylightSecondary,
    onLiveAmber = Color.White,
    liveAmberBg = DaylightAmberBg,
    liveAmberBorder = DaylightAmberBorder,
    surfaceContainer = DaylightSurfaceContainer,
    surfaceHigh = DaylightSurfaceHigh,
    statusEmerald = DaylightTertiary,
    activeBorderHighlight = DaylightPrimary
  )
}

private val LightColorScheme = lightColorScheme(
  primary = DaylightPrimary,
  onPrimary = Color.White,
  secondary = DaylightSecondary,
  onSecondary = Color.White,
  background = DaylightBackground,
  onBackground = DaylightOnSurface,
  surface = DaylightSurface,
  onSurface = DaylightOnSurface,
  surfaceVariant = DaylightSurfaceContainer,
  onSurfaceVariant = DaylightOnSurfaceVariant,
  outline = DaylightBorder
)

private val DarkColorScheme = darkColorScheme(
  primary = CockpitPrimary,
  onPrimary = CockpitPrimaryDark,
  secondary = CockpitSecondary,
  onSecondary = Color.Black,
  background = CockpitBackground,
  onBackground = CockpitOnSurface,
  surface = CockpitSurface,
  onSurface = CockpitOnSurface,
  surfaceVariant = CockpitSurfaceContainer,
  onSurfaceVariant = CockpitOnSurfaceVariant,
  outline = CockpitBorder
)

@Composable
fun CockpitRadioTheme(
  isDaylight: Boolean = true,
  content: @Composable () -> Unit
) {
  val customColors = if (isDaylight) {
    CockpitColors(
      isDaylight = true,
      background = DaylightBackground,
      surface = DaylightSurface,
      cardBackground = DaylightSurfaceCard,
      cardBorder = DaylightBorder,
      textPrimary = DaylightOnSurface,
      textSecondary = DaylightOnSurfaceVariant,
      primaryAccent = DaylightPrimary,
      onPrimaryAccent = Color.White,
      liveAmber = DaylightSecondary,
      onLiveAmber = Color.White,
      liveAmberBg = DaylightAmberBg,
      liveAmberBorder = DaylightAmberBorder,
      surfaceContainer = DaylightSurfaceContainer,
      surfaceHigh = DaylightSurfaceHigh,
      statusEmerald = DaylightTertiary,
      activeBorderHighlight = DaylightPrimary
    )
  } else {
    CockpitColors(
      isDaylight = false,
      background = CockpitBackground,
      surface = CockpitSurface,
      cardBackground = CockpitSurfaceCard,
      cardBorder = CockpitBorder,
      textPrimary = CockpitOnSurface,
      textSecondary = CockpitOnSurfaceVariant,
      primaryAccent = CockpitPrimary,
      onPrimaryAccent = CockpitPrimaryDark,
      liveAmber = CockpitSecondary,
      onLiveAmber = Color(0xFF4E2600),
      liveAmberBg = CockpitAmberBg,
      liveAmberBorder = CockpitAmberBorder,
      surfaceContainer = CockpitSurfaceContainer,
      surfaceHigh = CockpitSurfaceHigh,
      statusEmerald = CockpitTertiary,
      activeBorderHighlight = CockpitPrimary
    )
  }

  CompositionLocalProvider(LocalCockpitColors provides customColors) {
    MaterialTheme(
      colorScheme = if (isDaylight) LightColorScheme else DarkColorScheme,
      typography = Typography,
      content = content
    )
  }
}
