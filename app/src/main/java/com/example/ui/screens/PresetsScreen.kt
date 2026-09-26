package com.example.ui.screens

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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.MusicNote
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RadioStation
import com.example.ui.theme.LocalCockpitColors

@Composable
fun PresetsScreen(
  stations: List<RadioStation>,
  currentStation: RadioStation,
  isPlaying: Boolean,
  onStationSelected: (RadioStation) -> Unit,
  onToggleFavorite: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  val colors = LocalCockpitColors.current

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .background(colors.background),
    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Section Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Box(
            modifier = Modifier
              .size(10.dp)
              .clip(CircleShape)
              .background(colors.liveAmber)
          )
          Text(
            text = "WEB RADIO PRESETS",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 1.sp,
            color = colors.textSecondary
          )
        }

        Text(
          text = "${stations.size} Stations • Tap to Switch",
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium,
          fontFamily = FontFamily.Monospace,
          color = colors.textSecondary.copy(alpha = 0.85f)
        )
      }
    }

    // Presets List
    itemsIndexed(stations) { index, station ->
      val isActive = station.id == currentStation.id
      PresetCard(
        station = station,
        isActive = isActive,
        isPlaying = isPlaying && isActive,
        onSelect = { onStationSelected(station) },
        onToggleFavorite = { onToggleFavorite(station.id) }
      )
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun PresetCard(
  station: RadioStation,
  isActive: Boolean,
  isPlaying: Boolean,
  onSelect: () -> Unit,
  onToggleFavorite: () -> Unit
) {
  val colors = LocalCockpitColors.current

  val pulseScale = if (isActive && isPlaying) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
      initialValue = 0.85f,
      targetValue = 1.15f,
      animationSpec = infiniteRepeatable(
        animation = tween(800),
        repeatMode = RepeatMode.Reverse
      ),
      label = "pulseScale"
    )
    scale
  } else 1.0f

  val cardBorderColor = if (isActive) colors.primaryAccent else colors.cardBorder
  val cardBorderWidth = if (isActive) 2.5.dp else 1.dp

  Box(
    modifier = Modifier
      .testTag("preset_card_${station.presetNumber}")
      .fillMaxWidth()
      .clip(RoundedCornerShape(22.dp))
      .background(colors.cardBackground)
      .border(
        width = cardBorderWidth,
        color = cardBorderColor,
        shape = RoundedCornerShape(22.dp)
      )
      .clickable { onSelect() }
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 18.dp, vertical = 16.dp),
      verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      // Header Row: Preset Badge, Station Name, Status, Favorite Star
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          // Preset Badge (P1..P8)
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(8.dp))
              .background(if (isActive) colors.primaryAccent else colors.surfaceContainer)
              .padding(horizontal = 10.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "P${station.presetNumber}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              fontFamily = FontFamily.Monospace,
              letterSpacing = 0.5.sp,
              color = if (isActive) colors.onPrimaryAccent else colors.textPrimary
            )
          }

          // Station Callsign/Name
          Text(
            text = station.callSign,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            fontFamily = FontFamily.Monospace,
            letterSpacing = 0.5.sp,
            color = colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        // Status Tag (ON AIR or Genre Tag) & Favorite Star
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          if (isActive) {
            // ON AIR pulsing badge
            Row(
              modifier = Modifier
                .clip(CircleShape)
                .background(colors.liveAmber)
                .padding(horizontal = 12.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(6.dp)
                  .scale(pulseScale)
                  .clip(CircleShape)
                  .background(Color.White)
              )
              Text(
                text = "ON AIR",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 1.sp,
                color = colors.onLiveAmber
              )
            }
          } else {
            // Genre tag badge
            Box(
              modifier = Modifier
                .clip(CircleShape)
                .background(colors.surfaceContainer)
                .border(
                  width = 1.dp,
                  color = colors.cardBorder,
                  shape = CircleShape
                )
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
              Text(
                text = station.genreTag,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.5.sp,
                color = colors.textSecondary
              )
            }
          }

          // Favorite Button
          Box(
            modifier = Modifier
              .testTag("favorite_button_${station.id}")
              .size(44.dp)
              .clip(RoundedCornerShape(12.dp))
              .background(if (isActive) colors.primaryAccent.copy(alpha = 0.12f) else colors.surfaceContainer)
              .border(
                1.dp,
                if (isActive) colors.primaryAccent.copy(alpha = 0.5f) else colors.cardBorder,
                RoundedCornerShape(12.dp)
              )
              .clickable { onToggleFavorite() },
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = if (station.isFavorite) Icons.Default.Star else Icons.Outlined.StarOutline,
              contentDescription = "Toggle Favorite",
              tint = if (isActive) colors.primaryAccent else if (station.isFavorite) colors.liveAmber else colors.textSecondary.copy(alpha = 0.6f),
              modifier = Modifier.size(22.dp)
            )
          }
        }
      }

      // Divider line
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(1.dp)
          .background(colors.cardBorder.copy(alpha = 0.5f))
      )

      // Bottom Row: Artist & Track Details
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          modifier = Modifier.weight(1f),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          val icon = getStationIcon(station)
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isActive) colors.primaryAccent else colors.textSecondary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = station.currentArtist,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
          Text(
            text = "•",
            fontSize = 13.sp,
            color = colors.textSecondary.copy(alpha = 0.6f)
          )
          Text(
            text = station.currentTrack,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = colors.textSecondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
          text = "LIVE",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          fontFamily = FontFamily.Monospace,
          letterSpacing = 0.5.sp,
          color = if (isActive) colors.primaryAccent else colors.textSecondary
        )
      }
    }
  }
}

private fun getStationIcon(station: RadioStation): ImageVector {
  return when {
    station.genreTag.contains("TALK") || station.genreTag.contains("NEWS") -> Icons.Default.Podcasts
    station.genreTag.contains("HITS") -> Icons.Default.Album
    else -> Icons.Default.MusicNote
  }
}
