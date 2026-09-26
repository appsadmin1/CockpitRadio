package com.example.model

data class RadioStation(
  val id: String,
  val presetNumber: Int,
  val callSign: String,
  val genreTag: String,
  val currentArtist: String,
  val currentTrack: String,
  val currentShow: String,
  val streamUrl: String,
  val albumArtwork: String,
  val isFavorite: Boolean = false
)

enum class CockpitScreen {
  PRESETS,
  NOW_PLAYING
}
