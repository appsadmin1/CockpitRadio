package com.example.model

data class RadioStation(
  val id: String,
  val presetNumber: Int,
  val callSign: String,
  val frequency: String,
  val band: String, // "FM", "AM", "SXM", "WEB"
  val genreTag: String,
  val currentArtist: String,
  val currentTrack: String,
  val currentShow: String,
  val audioQuality: String,
  val streamUrl: String,
  val albumArtwork: String,
  val isFavorite: Boolean = false,
  val multicastChannels: List<String> = listOf("HD-1 Live", "HD-2 Deep Cuts", "HD-3 Rock News"),
  val selectedMulticast: Int = 0
)

enum class CockpitScreen {
  PRESETS,
  NOW_PLAYING
}

enum class TunerBand(val label: String) {
  PRESETS("Presets"),
  FM("FM Radio"),
  AM("AM Radio"),
  SATELLITE("Satellite"),
  WEB_STREAM("Web Stream")
}
