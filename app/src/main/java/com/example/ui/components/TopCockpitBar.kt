package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Podcasts
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalCockpitColors
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TopCockpitBar(
  isDaylightMode: Boolean,
  isNowPlayingScreen: Boolean = false,
  onToggleDaylight: () -> Unit,
  onOpenPresets: () -> Unit,
  onOpenNowPlaying: () -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = LocalCockpitColors.current

  var currentTime by remember {
    mutableStateOf(SimpleDateFormat("HH:mm", Locale.US).format(Date()))
  }

  LaunchedEffect(Unit) {
    while (true) {
      currentTime = SimpleDateFormat("HH:mm", Locale.US).format(Date())
      delay(10000)
    }
  }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .background(colors.surface.copy(alpha = 0.95f))
      .border(1.dp, colors.cardBorder.copy(alpha = 0.6f))
      .statusBarsPadding()
      .padding(horizontal = 16.dp, vertical = 10.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Left: Navigation pill (Back to Presets / Open Player) & Clock
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      if (isNowPlayingScreen) {
        Box(
          modifier = Modifier
            .testTag("top_back_to_presets")
            .clip(RoundedCornerShape(10.dp))
            .background(colors.surfaceContainer)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(10.dp))
            .clickable { onOpenPresets() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.FormatListBulleted,
              contentDescription = "Presets",
              tint = colors.primaryAccent,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "PRESETS",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              color = colors.primaryAccent
            )
          }
        }
      } else {
        Box(
          modifier = Modifier
            .testTag("top_open_player")
            .clip(RoundedCornerShape(10.dp))
            .background(colors.surfaceContainer)
            .border(1.dp, colors.cardBorder, RoundedCornerShape(10.dp))
            .clickable { onOpenNowPlaying() }
            .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Album,
              contentDescription = "Now Playing",
              tint = colors.primaryAccent,
              modifier = Modifier.size(16.dp)
            )
            Text(
              text = "NOW PLAYING",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              color = colors.primaryAccent
            )
          }
        }
      }

      Text(
        text = currentTime,
        fontSize = 18.sp,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = FontFamily.Monospace,
        color = colors.textPrimary,
        letterSpacing = 0.5.sp
      )
    }

    // Right: Live Web Stream Badge & Daylight Mode Switcher
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
      Row(
        modifier = Modifier
          .clip(CircleShape)
          .background(colors.liveAmberBg)
          .border(1.dp, colors.liveAmberBorder, CircleShape)
          .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        Icon(
          imageVector = Icons.Default.Podcasts,
          contentDescription = null,
          tint = colors.liveAmber,
          modifier = Modifier.size(14.dp)
        )
        Text(
          text = "WEB STREAM",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          color = colors.liveAmber
        )
      }

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
          .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        if (isDaylightMode) {
          Icon(
            imageVector = Icons.Default.LightMode,
            contentDescription = "Daylight Mode",
            tint = colors.liveAmber,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "DAYLIGHT",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = colors.liveAmber
          )
        } else {
          Icon(
            imageVector = Icons.Default.DarkMode,
            contentDescription = "OLED Night Mode",
            tint = colors.primaryAccent,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = "OLED NIGHT",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = colors.primaryAccent
          )
        }
      }
    }
  }
}
