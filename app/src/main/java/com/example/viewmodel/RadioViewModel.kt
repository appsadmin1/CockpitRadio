package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.RadioAudioPlayer
import com.example.model.CockpitScreen
import com.example.model.RadioStation
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RadioViewModel(application: Application) : AndroidViewModel(application) {

  private val defaultArtwork = "https://lh3.googleusercontent.com/aida-public/AB6AXuBE02QqWtplccATH0udmKByAAXW81V897r_QqkW6v63W5w234mV_d5u80Nzd9toNuKPrR6tG31k-X7YfpxRdjg_uZjO6RN5K2hZ1a9q1Q05OJeL3WG1oyzjgqD24F7-cCY2EU7pqDSNFu3bv0nio1MD2ucG_7MZ3UIWgqNEs-j9O3Hd7c6NrO7D8H9lRNSuZVQtWPZOcAHK6EVMzODm7tvNvL2rWd3ucuwd7ytJ5zAwjnERHIvaTDDyTQ"

  private val initialStations = listOf(
    RadioStation(
      id = "p1",
      presetNumber = 1,
      callSign = "100FM רדיוס",
      genreTag = "HITS & POP",
      currentArtist = "רדיוס 100FM",
      currentTrack = "100% מוזיקה מעולה • Hits & Dance",
      currentShow = "תוכנית הלהיטים של ישראל",
      streamUrl = "https://cdn.cybercdn.live/Radios_100FM/Audio/icecast.audio",
      albumArtwork = defaultArtwork,
      isFavorite = true
    ),
    RadioStation(
      id = "p2",
      presetNumber = 2,
      callSign = "ECO 99FM",
      genreTag = "TOP HITS",
      currentArtist = "אקו 99FM",
      currentTrack = "מוזיקה מעולה בדרכים • Eco Hits",
      currentShow = "בוקר אקו • מוזיקה ירוקה וקצבית",
      streamUrl = "https://eco-live.mediacast.co.il/99fm_aac",
      albumArtwork = defaultArtwork,
      isFavorite = false
    ),
    RadioStation(
      id = "p3",
      presetNumber = 3,
      callSign = "103FM רדיו",
      genreTag = "TALK & NEWS",
      currentArtist = "רדיו 103FM",
      currentTrack = "שיחות ישירות ופרשנות • Non-Stop Talk",
      currentShow = "שיחות עם מאזינים • אקטואליה",
      streamUrl = "https://cdn.cybercdn.live/103FM/Live/icecast.audio",
      albumArtwork = defaultArtwork,
      isFavorite = false
    ),
    RadioStation(
      id = "p4",
      presetNumber = 4,
      callSign = "גלי צה\"ל (GLZ)",
      genreTag = "NEWS & TALK",
      currentArtist = "גלי צה\"ל",
      currentTrack = "יומן החדשות • Galei Zahal Live",
      currentShow = "מה בוער • נכון להבוקר",
      streamUrl = "https://glzwizzlv.bynetcdn.com/glz_mp3",
      albumArtwork = defaultArtwork,
      isFavorite = false
    ),
    RadioStation(
      id = "p5",
      presetNumber = 5,
      callSign = "גלגלצ (GLGLZ)",
      genreTag = "MUSIC & TRAFFIC",
      currentArtist = "גלגלצ",
      currentTrack = "המצעד הרשמי של ישראל • דיווחי תנועה",
      currentShow = "קולות החיילים • מדינה בדרך",
      streamUrl = "https://glzwizzlv.bynetcdn.com/glglz_mp3",
      albumArtwork = defaultArtwork,
      isFavorite = true
    ),
    RadioStation(
      id = "p6",
      presetNumber = 6,
      callSign = "102FM תל אביב",
      genreTag = "URBAN BEATS",
      currentArtist = "רדיו תל אביב",
      currentTrack = "הקצב של העיר • Non-Stop City Hits",
      currentShow = "תל אביב בבוקר • Tel Aviv Live",
      streamUrl = "https://102.livecdn.biz/102fm_aac",
      albumArtwork = defaultArtwork,
      isFavorite = false
    ),
    RadioStation(
      id = "p7",
      presetNumber = 7,
      callSign = "כאן 88 (KAN)",
      genreTag = "ALTERNATIVE",
      currentArtist = "כאן 88",
      currentTrack = "מוזיקה מעולה • Rock, Indie & Jazz",
      currentShow = "ערב עירוני • הבחירות של כאן 88",
      streamUrl = "https://29073.live.streamtheworld.com/KAN_88.mp3",
      albumArtwork = defaultArtwork,
      isFavorite = false
    ),
    RadioStation(
      id = "p8",
      presetNumber = 8,
      callSign = "91FM לב המדינה",
      genreTag = "MEDITERRANEAN",
      currentArtist = "לב המדינה",
      currentTrack = "מוזיקה ים תיכונית וישראלית",
      currentShow = "חגיגה בלב המדינה • Live Mizrahi Hits",
      streamUrl = "https://cdn.cybercdn.live/Lev_Hamedina/Audio/icecast.audio",
      albumArtwork = defaultArtwork,
      isFavorite = false
    )
  )

  private val _stations = MutableStateFlow(initialStations)
  val stations: StateFlow<List<RadioStation>> = _stations.asStateFlow()

  private val _currentIndex = MutableStateFlow(0)
  val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

  val currentStation: StateFlow<RadioStation> = _currentIndex.map { index ->
    val list = _stations.value
    if (index in list.indices) list[index] else list.first()
  }.stateIn(viewModelScope, SharingStarted.Eagerly, initialStations.first())

  private val _currentScreen = MutableStateFlow(CockpitScreen.PRESETS)
  val currentScreen: StateFlow<CockpitScreen> = _currentScreen.asStateFlow()

  private val _isDaylightMode = MutableStateFlow(true)
  val isDaylightMode: StateFlow<Boolean> = _isDaylightMode.asStateFlow()

  private val _steeringWheelNotification = MutableStateFlow<String?>(null)
  val steeringWheelNotification: StateFlow<String?> = _steeringWheelNotification.asStateFlow()

  private var notificationJob: Job? = null

  val player = RadioAudioPlayer(
    context = application.applicationContext,
    onNextStation = { playNextStation("Steering Wheel") },
    onPreviousStation = { playPreviousStation("Steering Wheel") }
  )

  val isPlaying = player.isPlaying
  val isBuffering = player.isBuffering

  fun toggleDaylightMode() {
    _isDaylightMode.value = !_isDaylightMode.value
  }

  fun setScreen(screen: CockpitScreen) {
    _currentScreen.value = screen
  }

  fun selectStation(station: RadioStation, autoPlay: Boolean = true, navigateToNowPlaying: Boolean = false) {
    val index = _stations.value.indexOfFirst { it.id == station.id }
    if (index >= 0) {
      _currentIndex.value = index
      if (autoPlay) {
        player.playStation(station)
      }
      if (navigateToNowPlaying) {
        _currentScreen.value = CockpitScreen.NOW_PLAYING
      }
    }
  }

  fun playNextStation(source: String = "Control Dock") {
    val list = _stations.value
    val nextIndex = (currentIndex.value + 1) % list.size
    _currentIndex.value = nextIndex
    val targetStation = list[nextIndex]
    player.playStation(targetStation)
    showWheelNotification("$source: P${targetStation.presetNumber} ${targetStation.callSign}")
  }

  fun playPreviousStation(source: String = "Control Dock") {
    val list = _stations.value
    val prevIndex = if (currentIndex.value - 1 < 0) list.size - 1 else currentIndex.value - 1
    _currentIndex.value = prevIndex
    val targetStation = list[prevIndex]
    player.playStation(targetStation)
    showWheelNotification("$source: P${targetStation.presetNumber} ${targetStation.callSign}")
  }

  fun togglePlayPause() {
    player.togglePlayPause { currentStation.value }
  }

  fun toggleFavorite(stationId: String) {
    val list = _stations.value.map {
      if (it.id == stationId) it.copy(isFavorite = !it.isFavorite) else it
    }
    _stations.value = list
  }

  fun onSteeringWheelEvent(buttonName: String) {
    showWheelNotification("Car Wheel: $buttonName")
  }

  private fun showWheelNotification(message: String) {
    notificationJob?.cancel()
    _steeringWheelNotification.value = message
    notificationJob = viewModelScope.launch {
      delay(2400)
      _steeringWheelNotification.value = null
    }
  }

  override fun onCleared() {
    super.onCleared()
    notificationJob?.cancel()
    player.release()
  }
}
