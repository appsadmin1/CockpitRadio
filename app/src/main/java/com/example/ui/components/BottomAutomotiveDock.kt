package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalCockpitColors

@Composable
fun BottomAutomotiveDock(
  isNowPlayingScreen: Boolean,
  isPlaying: Boolean,
  isBuffering: Boolean,
  isFavorite: Boolean = false,
  onPresetsClick: () -> Unit,
  onNowPlayingClick: () -> Unit,
  onPrevPreset: () -> Unit,
  onTogglePlay: () -> Unit,
  onNextPreset: () -> Unit,
  onToggleFavorite: () -> Unit = {},
  modifier: Modifier = Modifier
) {
  val colors = LocalCockpitColors.current

  Column(
    modifier = modifier
      .fillMaxWidth()
      .background(colors.surface.copy(alpha = 0.98f))
      .border(1.dp, colors.cardBorder.copy(alpha = 0.8f))
      .navigationBarsPadding()
      .padding(horizontal = 12.dp, vertical = 8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      if (isNowPlayingScreen) {
        // Button 1: Presets Switcher
        DockButton(
          icon = Icons.Default.FormatListBulleted,
          label = "PRESETS",
          testTag = "dock_presets_button",
          tint = colors.primaryAccent,
          modifier = Modifier.weight(1f),
          onClick = onPresetsClick
        )
      } else {
        // Button 1: Now Playing Screen Switcher
        DockButton(
          icon = Icons.Default.Album,
          label = "PLAYER",
          testTag = "dock_now_playing_button",
          tint = colors.primaryAccent,
          modifier = Modifier.weight(1f),
          onClick = onNowPlayingClick
        )
      }

      // Button 2: Previous Station Step
      DockButton(
        icon = Icons.Default.SkipPrevious,
        label = "PREV",
        testTag = "dock_prev_button",
        modifier = Modifier.weight(1f),
        onClick = onPrevPreset
      )

      // Button 3: Main Play / Pause / Mute Hero Knob
      val heroInteraction = remember { MutableInteractionSource() }
      val heroPressed by heroInteraction.collectIsPressedAsState()
      val heroScale by animateFloatAsState(if (heroPressed) 0.92f else 1.0f, label = "heroScale")

      if (isNowPlayingScreen) {
        // Circular luminous dial knob
        Box(
          modifier = Modifier
            .testTag("dock_hero_play_button")
            .size(70.dp)
            .scale(heroScale)
            .clip(CircleShape)
            .background(colors.primaryAccent)
            .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape)
            .clickable(
              interactionSource = heroInteraction,
              indication = null
            ) { onTogglePlay() },
          contentAlignment = Alignment.Center
        ) {
          if (isBuffering) {
            CircularProgressIndicator(
              modifier = Modifier.size(32.dp),
              color = colors.onPrimaryAccent,
              strokeWidth = 3.dp
            )
          } else {
            Icon(
              imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
              contentDescription = if (isPlaying) "Pause Radio" else "Play Radio",
              tint = colors.onPrimaryAccent,
              modifier = Modifier.size(42.dp)
            )
          }
        }
      } else {
        // Wide hero button
        Box(
          modifier = Modifier
            .testTag("dock_hero_play_button")
            .weight(1.35f)
            .scale(heroScale)
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colors.primaryAccent)
            .clickable(
              interactionSource = heroInteraction,
              indication = null
            ) { onTogglePlay() },
          contentAlignment = Alignment.Center
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            if (isBuffering) {
              CircularProgressIndicator(
                modifier = Modifier.size(26.dp),
                color = colors.onPrimaryAccent,
                strokeWidth = 3.dp
              )
            } else {
              Icon(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause Radio" else "Play Radio",
                tint = colors.onPrimaryAccent,
                modifier = Modifier.size(30.dp)
              )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (isPlaying) "PAUSE" else "PLAY",
              fontSize = 10.sp,
              fontWeight = FontWeight.ExtraBold,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 1.sp,
              color = colors.onPrimaryAccent
            )
          }
        }
      }

      // Button 4: Next Station Step
      DockButton(
        icon = Icons.Default.SkipNext,
        label = "NEXT",
        testTag = "dock_next_button",
        modifier = Modifier.weight(1f),
        onClick = onNextPreset
      )

      // Button 5: Favorite Star Quick Action
      DockButton(
        icon = if (isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline,
        label = "FAV",
        testTag = "dock_favorite_button",
        tint = if (isFavorite) colors.liveAmber else colors.textPrimary,
        modifier = Modifier.weight(1f),
        onClick = onToggleFavorite
      )
    }
  }
}

@Composable
private fun DockButton(
  icon: ImageVector,
  label: String,
  testTag: String,
  modifier: Modifier = Modifier,
  tint: Color? = null,
  onClick: () -> Unit
) {
  val colors = LocalCockpitColors.current
  val interaction = remember { MutableInteractionSource() }
  val isPressed by interaction.collectIsPressedAsState()
  val scale by animateFloatAsState(if (isPressed) 0.94f else 1.0f, label = "buttonScale")

  Box(
    modifier = modifier
      .testTag(testTag)
      .scale(scale)
      .height(60.dp)
      .clip(RoundedCornerShape(14.dp))
      .background(colors.cardBackground)
      .border(1.dp, colors.cardBorder, RoundedCornerShape(14.dp))
      .clickable(
        interactionSource = interaction,
        indication = null
      ) { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = label,
        tint = tint ?: colors.textPrimary,
        modifier = Modifier.size(22.dp)
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = label,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        letterSpacing = 0.5.sp,
        color = colors.textSecondary
      )
    }
  }
}
