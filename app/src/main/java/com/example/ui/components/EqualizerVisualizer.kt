package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.ui.theme.LocalCockpitColors

@Composable
fun EqualizerVisualizer(
  isPlaying: Boolean,
  modifier: Modifier = Modifier
) {
  val colors = LocalCockpitColors.current

  val transition = rememberInfiniteTransition(label = "eqAnim")

  val bar1 by transition.animateFloat(
    initialValue = 0.25f, targetValue = 0.95f,
    animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "b1"
  )
  val bar2 by transition.animateFloat(
    initialValue = 0.35f, targetValue = 0.85f,
    animationSpec = infiniteRepeatable(tween(1100, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "b2"
  )
  val bar3 by transition.animateFloat(
    initialValue = 0.2f, targetValue = 1.0f,
    animationSpec = infiniteRepeatable(tween(600, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "b3"
  )
  val bar4 by transition.animateFloat(
    initialValue = 0.3f, targetValue = 0.9f,
    animationSpec = infiniteRepeatable(tween(900, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "b4"
  )
  val bar5 by transition.animateFloat(
    initialValue = 0.25f, targetValue = 0.95f,
    animationSpec = infiniteRepeatable(tween(1200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
    label = "b5"
  )

  val heights = if (isPlaying) {
    listOf(bar1, bar2, bar3, bar4, bar5, bar2, bar1, bar4)
  } else {
    listOf(0.2f, 0.25f, 0.2f, 0.3f, 0.25f, 0.2f, 0.25f, 0.2f)
  }

  Row(
    modifier = modifier.height(22.dp),
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    heights.forEachIndexed { index, fraction ->
      val barColor = when (index) {
        3, 4 -> colors.liveAmber
        else -> colors.primaryAccent
      }

      Box(
        modifier = Modifier
          .width(3.5.dp)
          .height((fraction * 20).coerceIn(4f, 20f).dp)
          .clip(RoundedCornerShape(2.dp))
          .background(barColor)
      )
    }
  }
}
