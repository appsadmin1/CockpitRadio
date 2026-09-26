package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SettingsInputComponent
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalCockpitColors
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
  }
}
