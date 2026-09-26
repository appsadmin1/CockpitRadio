package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalCockpitColors

@Composable
fun SteeringWheelHudBanner(
  notification: String?,
  onWheelNext: () -> Unit,
  onWheelPrev: () -> Unit,
  onWheelPlayPause: () -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = LocalCockpitColors.current
  var isSimulatorExpanded by remember { mutableStateOf(false) }

  Column(
    modifier = modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Heads-up Car Wheel Trigger Notification Toast
    AnimatedVisibility(
      visible = notification != null,
      enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
      exit = fadeOut() + slideOutVertically(targetOffsetY = { -it })
    ) {
      Box(
        modifier = Modifier
          .padding(top = 10.dp, bottom = 4.dp)
          .shadow(12.dp, CircleShape)
          .clip(CircleShape)
          .background(colors.cardBackground)
          .border(2.dp, colors.primaryAccent, CircleShape)
          .padding(horizontal = 18.dp, vertical = 8.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Icon(
            imageVector = Icons.Default.SettingsInputComponent,
            contentDescription = "Car Steering Wheel",
            tint = colors.liveAmber,
            modifier = Modifier.size(18.dp)
          )
          Text(
            text = notification ?: "",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.5.sp,
            color = colors.textPrimary
          )
        }
      }
    }

    // Steering Wheel Quick Test Bar (Allows verifying car wheel button integration easily)
    Row(
      modifier = Modifier
        .testTag("steering_wheel_simulator_bar")
        .padding(horizontal = 16.dp, vertical = 4.dp)
        .clip(RoundedCornerShape(24.dp))
        .background(colors.surfaceContainer)
        .border(1.dp, colors.cardBorder, RoundedCornerShape(24.dp))
        .clickable { isSimulatorExpanded = !isSimulatorExpanded }
        .padding(horizontal = 12.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Icon(
        imageVector = Icons.Default.SettingsInputComponent,
        contentDescription = "Steering Wheel Integration",
        tint = colors.primaryAccent,
        modifier = Modifier.size(16.dp)
      )
      Text(
        text = if (isSimulatorExpanded) "CAR STEERING WHEEL CONTROLS (CAN-BUS ACTIVE):" else "CAR WHEEL BUTTONS ACTIVE • TAP TO TEST",
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FontFamily.Monospace,
        color = colors.textSecondary
      )

      if (isSimulatorExpanded) {
        // Prev button
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(colors.cardBackground)
            .border(1.dp, colors.cardBorder, CircleShape)
            .clickable { onWheelPrev() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.SkipPrevious,
              contentDescription = "Wheel Prev",
              tint = colors.textPrimary,
              modifier = Modifier.size(14.dp)
            )
            Text("PREV", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
          }
        }

        // Play/Pause button
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(colors.primaryAccent)
            .clickable { onWheelPlayPause() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.PlayArrow,
              contentDescription = "Wheel Play/Pause",
              tint = colors.onPrimaryAccent,
              modifier = Modifier.size(14.dp)
            )
            Text("MUTE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = colors.onPrimaryAccent)
          }
        }

        // Next button
        Box(
          modifier = Modifier
            .clip(CircleShape)
            .background(colors.cardBackground)
            .border(1.dp, colors.cardBorder, CircleShape)
            .clickable { onWheelNext() }
            .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text("NEXT", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = colors.textPrimary)
            Icon(
              imageVector = Icons.Default.SkipNext,
              contentDescription = "Wheel Next",
              tint = colors.textPrimary,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }
    }
  }
}
