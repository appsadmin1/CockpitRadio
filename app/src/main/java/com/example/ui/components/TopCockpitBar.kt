package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.SignalCellular4Bar
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TunerBand
import com.example.ui.theme.LocalCockpitColors
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TopCockpitBar(
  currentBand: TunerBand,
  isDaylightMode: Boolean,
  isNowPlayingScreen: Boolean = false,
  onToggleDaylight: () -> Unit,
  onSelectBand: (TunerBand) -> Unit,
  onOpenPresets: () -> Unit,
  onOpenNowPlaying: () -> Unit,
  onOpenDirectTune: () -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = LocalCockpitColors.current

  var currentTime by remember {
    mutableStateOf(SimpleDateFormat("hh:mm", Locale.US).format(Date()))
  }
  var amPm by remember {
    mutableStateOf(SimpleDateFormat("a", Locale.US).format(Date()))
  }

  LaunchedEffect(Unit) {
    while (true) {
      currentTime = SimpleDateFormat("hh:mm", Locale.US).format(Date())
      amPm = SimpleDateFormat("a", Locale.US).format(Date())
      delay(10000)
    }
  }

  if (isNowPlayingScreen) {
    // Header for Now Playing Screen (matching Image 3 & 7)
    Row(
      modifier = modifier
        .fillMaxWidth()
        .background(colors.surface.copy(alpha = 0.95f))
        .border(1.dp, colors.cardBorder.copy(alpha = 0.6f))
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 12.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Back to Presets + Time & HD Live Status
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Direct Presets Back Pill
        Box(
          modifier = Modifier
            .testTag("top_back_to_presets")
            .clip(RoundedCornerShape(8.dp))
            .background(colors.surfaceContainer)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(8.dp))
            .clickable { onOpenPresets() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Text(
            text = "← PRESETS",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = colors.primaryAccent
          )
        }

        Text(
          text = currentTime,
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          color = colors.textPrimary,
          letterSpacing = (-0.5).sp
        )

        Row(
          modifier = Modifier
            .clip(CircleShape)
            .background(colors.liveAmberBg)
            .border(1.dp, colors.liveAmberBorder, CircleShape)
            .clickable { onToggleDaylight() }
            .padding(horizontal = 8.dp, vertical = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
          Box(
            modifier = Modifier
              .size(6.dp)
              .clip(CircleShape)
              .background(colors.liveAmber)
          )
          Text(
            text = "HD LIVE",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = colors.liveAmber
          )
        }
      }

      // Center Tuner Band Switcher (FM / AM / SXM)
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(colors.surfaceContainer)
          .border(1.dp, colors.cardBorder, RoundedCornerShape(12.dp))
          .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        listOf("FM", "AM", "SXM").forEach { bandStr ->
          val isSelected = (bandStr == "FM" && currentBand == TunerBand.FM) ||
            (bandStr == "AM" && currentBand == TunerBand.AM) ||
            (bandStr == "SXM" && currentBand == TunerBand.SATELLITE) ||
            (bandStr == "FM" && currentBand == TunerBand.PRESETS)
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (isSelected) colors.primaryAccent else Color.Transparent)
              .clickable {
                when (bandStr) {
                  "FM" -> onSelectBand(TunerBand.FM)
                  "AM" -> onSelectBand(TunerBand.AM)
                  "SXM" -> onSelectBand(TunerBand.SATELLITE)
                }
              }
              .padding(horizontal = 14.dp, vertical = 6.dp)
          ) {
            Text(
              text = bandStr,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              color = if (isSelected) colors.onPrimaryAccent else colors.textSecondary
            )
          }
        }
      }

      // Signal Strength & Settings/Tune Button
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Row(
          modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colors.surfaceContainer)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 6.dp, vertical = 4.dp),
          verticalAlignment = Alignment.Bottom,
          horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
          Box(modifier = Modifier.width(3.dp).height(5.dp).background(colors.primaryAccent))
          Box(modifier = Modifier.width(3.dp).height(8.dp).background(colors.primaryAccent))
          Box(modifier = Modifier.width(3.dp).height(11.dp).background(colors.primaryAccent))
          Box(modifier = Modifier.width(3.dp).height(14.dp).background(colors.primaryAccent))
        }

        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(colors.surfaceContainer)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(10.dp))
            .clickable { onOpenDirectTune() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = "Tune",
            tint = colors.primaryAccent,
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  } else {
    // Header for Presets Screen (matching Image 1 & 5)
    Column(
      modifier = modifier
        .fillMaxWidth()
        .background(colors.surface.copy(alpha = 0.95f))
        .border(1.dp, colors.cardBorder.copy(alpha = 0.6f))
        .statusBarsPadding()
        .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
      // Row 1: System Telemetry
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Daylight / OLED Night Switcher Pill + Time
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier
              .testTag("mode_toggle_pill")
              .clip(CircleShape)
              .background(if (isDaylightMode) colors.liveAmberBg else colors.surfaceHigh)
              .border(
                width = 1.dp,
                color = if (isDaylightMode) colors.liveAmberBorder else colors.primaryAccent.copy(alpha = 0.5f),
                shape = CircleShape
              )
              .clickable { onToggleDaylight() }
              .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            if (isDaylightMode) {
              Icon(
                imageVector = Icons.Default.LightMode,
                contentDescription = "Daylight Mode",
                tint = colors.liveAmber,
                modifier = Modifier.size(15.dp)
              )
              Text(
                text = "DAYLIGHT CABIN",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = colors.liveAmber
              )
            } else {
              Box(
                modifier = Modifier
                  .size(7.dp)
                  .clip(CircleShape)
                  .background(colors.primaryAccent)
              )
              Text(
                text = "COCKPIT OLED",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp,
                color = colors.primaryAccent
              )
            }
          }

          // Live Clock
          Row(verticalAlignment = Alignment.Bottom) {
            Text(
              text = currentTime,
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              color = colors.textPrimary,
              letterSpacing = 0.5.sp
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = amPm,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium,
              color = colors.textSecondary
            )
          }
        }

        // Connectivity Metrics
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.SatelliteAlt,
              contentDescription = "Synced",
              tint = colors.statusEmerald,
              modifier = Modifier.size(15.dp)
            )
            Text(
              text = "SYNCED",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = colors.statusEmerald
            )
          }

          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Podcasts,
              contentDescription = "HD Audio",
              tint = colors.primaryAccent,
              modifier = Modifier.size(15.dp)
            )
            Text(
              text = "HD AUDIO MAX",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = colors.primaryAccent
            )
          }

          Icon(
            imageVector = Icons.Default.SignalCellular4Bar,
            contentDescription = "Cellular signal",
            tint = colors.statusEmerald,
            modifier = Modifier.size(18.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Row 2: Tuner Source Bands & Keypad Button
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier
            .weight(1f)
            .horizontalScroll(rememberScrollState()),
          horizontalArrangement = Arrangement.spacedBy(8.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Dedicated NOW PLAYING Tab in the header
          Row(
            modifier = Modifier
              .testTag("band_tab_now_playing")
              .height(44.dp)
              .clip(CircleShape)
              .background(colors.cardBackground)
              .border(1.dp, colors.primaryAccent.copy(alpha = 0.7f), CircleShape)
              .clickable { onOpenNowPlaying() }
              .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.GraphicEq,
              contentDescription = null,
              tint = colors.primaryAccent,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "NOW PLAYING",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp,
              color = colors.primaryAccent
            )
          }

          TunerBand.values().forEach { band ->
            val isSelected = currentBand == band
            val bg by animateColorAsState(
              if (isSelected) colors.primaryAccent else colors.cardBackground,
              label = "bandBg"
            )
            val textColor by animateColorAsState(
              if (isSelected) colors.onPrimaryAccent else colors.textSecondary,
              label = "bandText"
            )

            Row(
              modifier = Modifier
                .testTag("band_tab_${band.name}")
                .height(44.dp)
                .clip(CircleShape)
                .background(bg)
                .border(
                  1.dp,
                  if (isSelected) colors.primaryAccent else colors.cardBorder,
                  CircleShape
                )
                .clickable {
                  onSelectBand(band)
                  onOpenPresets()
                }
                .padding(horizontal = 16.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              if (band == TunerBand.PRESETS) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = textColor,
                  modifier = Modifier.size(16.dp)
                )
              }
              Text(
                text = band.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp,
                color = textColor
              )
            }
          }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Direct Frequency Keypad Button
        Box(
          modifier = Modifier
            .testTag("direct_dialpad_button")
            .size(44.dp)
            .clip(CircleShape)
            .background(colors.cardBackground)
            .border(1.dp, colors.cardBorder, CircleShape)
            .clickable { onOpenDirectTune() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Dialpad,
            contentDescription = "Direct Frequency Tune",
            tint = colors.primaryAccent,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }
  }
}
