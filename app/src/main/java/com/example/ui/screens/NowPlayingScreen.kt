package com.example.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.Podcasts
import androidx.compose.material.icons.filled.Radio
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.RadioStation
import com.example.ui.components.EqualizerVisualizer
import com.example.ui.theme.LocalCockpitColors

@Composable
fun NowPlayingScreen(
  station: RadioStation,
  isPlaying: Boolean,
  onToggleFavorite: () -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = LocalCockpitColors.current

  val infiniteTransition = rememberInfiniteTransition(label = "nowPlayingAnimation")
  val rotation by infiniteTransition.animateFloat(
    initialValue = 0f,
    targetValue = 360f,
    animationSpec = infiniteRepeatable(
      animation = tween(20000, easing = LinearEasing),
      repeatMode = RepeatMode.Restart
    ),
    label = "vinylRotation"
  )

  val glowPulse by infiniteTransition.animateFloat(
    initialValue = 0.95f,
    targetValue = 1.05f,
    animationSpec = infiniteRepeatable(
      animation = tween(1500),
      repeatMode = RepeatMode.Reverse
    ),
    label = "glowPulse"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(colors.background)
      .verticalScroll(rememberScrollState())
      .padding(horizontal = 20.dp, vertical = 16.dp),
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.SpaceBetween
  ) {
    // 1. Station Banner Header
    Box(
      modifier = Modifier
        .testTag("now_playing_station_banner")
        .fillMaxWidth()
        .clip(RoundedCornerShape(20.dp))
        .background(colors.cardBackground)
        .border(1.dp, colors.cardBorder, RoundedCornerShape(20.dp))
    ) {
      // Left solid accent bar
      Box(
        modifier = Modifier
          .width(6.dp)
          .matchParentSize()
          .background(colors.primaryAccent)
      )

      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(start = 18.dp, top = 14.dp, end = 16.dp, bottom = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(colors.primaryAccent.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Radio,
              contentDescription = null,
              tint = colors.primaryAccent,
              modifier = Modifier.size(24.dp)
            )
          }

          Column {
            Text(
              text = "PRESET P${station.presetNumber} • ${station.genreTag}",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 1.sp,
              color = colors.primaryAccent
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = station.callSign,
              fontSize = 22.sp,
              fontWeight = FontWeight.ExtraBold,
              fontFamily = FontFamily.Monospace,
              color = colors.textPrimary,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
        }

        // Quick Favorite Action Button
        Box(
          modifier = Modifier
            .testTag("now_playing_star_button")
            .size(48.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (station.isFavorite) colors.liveAmberBg else colors.surfaceContainer)
            .border(
              1.dp,
              if (station.isFavorite) colors.liveAmberBorder else colors.cardBorder,
              RoundedCornerShape(14.dp)
            )
            .clickable { onToggleFavorite() },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (station.isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline,
            contentDescription = "Favorite Station",
            tint = if (station.isFavorite) colors.liveAmber else colors.textSecondary,
            modifier = Modifier.size(24.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // 2. Focal Audio Artwork & Animated Vinyl Disc
    Box(
      modifier = Modifier
        .testTag("now_playing_vinyl_disc")
        .size(220.dp),
      contentAlignment = Alignment.Center
    ) {
      // Outer Glowing Ring
      Box(
        modifier = Modifier
          .fillMaxSize()
          .scale(if (isPlaying) glowPulse else 1f)
          .clip(CircleShape)
          .border(2.dp, colors.primaryAccent.copy(alpha = 0.45f), CircleShape)
      )

      // Inset Ring
      Box(
        modifier = Modifier
          .size(204.dp)
          .clip(CircleShape)
          .border(1.dp, colors.primaryAccent.copy(alpha = 0.2f), CircleShape)
      )

      // Rotating Album Art Vinyl Disc
      Box(
        modifier = Modifier
          .size(188.dp)
          .graphicsLayer { rotationZ = if (isPlaying) rotation else 0f }
          .clip(CircleShape)
          .background(colors.cardBackground)
          .border(2.5.dp, colors.primaryAccent, CircleShape),
        contentAlignment = Alignment.Center
      ) {
        AsyncImage(
          model = station.albumArtwork,
          contentDescription = "Station Album Artwork",
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxSize()
            .clip(CircleShape)
        )

        // Center Vinyl Spindle Hole
        Box(
          modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(colors.cardBackground)
            .border(2.dp, colors.cardBorder, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Box(
            modifier = Modifier
              .size(12.dp)
              .clip(CircleShape)
              .background(colors.primaryAccent)
          )
        }
      }

      // Floating Live Stream Indicator Badge
      Box(
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .clip(CircleShape)
          .background(colors.cardBackground.copy(alpha = 0.95f))
          .border(1.dp, colors.primaryAccent.copy(alpha = 0.6f), CircleShape)
          .padding(horizontal = 14.dp, vertical = 5.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.GraphicEq,
            contentDescription = null,
            tint = colors.primaryAccent,
            modifier = Modifier.size(14.dp)
          )
          Text(
            text = "HQ WEB AUDIO",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.8.sp,
            color = colors.primaryAccent
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // 3. Track Metadata
    Column(
      modifier = Modifier
        .testTag("now_playing_metadata")
        .fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Show Pill Badge
      Row(
        modifier = Modifier
          .clip(CircleShape)
          .background(colors.liveAmberBg)
          .border(1.dp, colors.liveAmberBorder, CircleShape)
          .padding(horizontal = 16.dp, vertical = 6.dp),
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
          text = station.currentShow.uppercase(),
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 0.5.sp,
          color = colors.liveAmber,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Track Title
      Text(
        text = station.currentTrack,
        fontSize = 24.sp,
        fontWeight = FontWeight.ExtraBold,
        fontFamily = FontFamily.Monospace,
        letterSpacing = (-0.5).sp,
        color = colors.textPrimary,
        textAlign = TextAlign.Center,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(4.dp))

      // Artist Details
      Text(
        text = station.currentArtist,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
        color = colors.textSecondary,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
      )

      Spacer(modifier = Modifier.height(14.dp))

      // Real-time animated DSP Graphic Equalizer mini-strip
      EqualizerVisualizer(isPlaying = isPlaying)
    }

    Spacer(modifier = Modifier.height(16.dp))
  }
}
