package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.RadioAudioPlayer
import com.example.model.CockpitScreen
import com.example.model.RadioStation
import com.example.model.TunerBand
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

class RadioViewModel(application: Application) : AndroidViewModel(application) {

  private val defaultArtwork = "https://lh3.googleusercontent.com/aida-public/AB6AXuBE02QqWtplccATH0udmKByAAXW81V897r_QqkW6v63W5w234mV_d5u80Nzd9toNuKPrR6tG31k-X7YfpxRdjg_uZjO6RN5K2hZ1a9q1Q05OJeL3WG1oyzjgqD24F7-cCY2EU7pqDSNFu3bv0nio1MD2ucG_7MZ3UIWgqNEs-j9O3Hd7c6NrO7D8H9lRNSuZVQtWPZOcAHK6EVMzODm7tvNvL2rWd3ucuwd7ytJ5zAwjnERHIvaTDDyTQ"

  private val initialStations = listOf(
    RadioStation(
      id = "p1",
      presetNumber = 1,
      callSign = "100FM",
      frequency = "100.0",
      band = "FM",
      genreTag = "HITS & POP",
      currentArtist = "רדיוס 100FM",
      currentTrack = "100% מוזיקה מעולה • Hits & Dance",
      currentShow = "תוכנית הלהיטים של ישראל",
      audioQuality = "HD1 STEREO",
      streamUrl = "https://cdn.cybercdn.live/Radios_100FM/Audio/icecast.audio",
      albumArtwork = defaultArtwork,
      isFavorite = true,
      multicastChannels = listOf("100% Hits", "100% 80s", "100% Rock"),
      selectedMulticast = 0
    ),
    RadioStation(
      id = "p2",
      presetNumber = 2,
      callSign = "ECO 99",
      frequency = "99.0",
      band = "FM",
      genreTag = "TOP HITS",
      currentArtist = "אקו 99FM",
      currentTrack = "מוזיקה מעולה בדרכים • Eco Hits",
      currentShow = "בוקר אקו • מוזיקה ירוקה וקצבית",
      audioQuality = "STEREO",
      streamUrl = "https://eco-live.mediacast.co.il/99fm_mp3",
      albumArtwork = defaultArtwork,
      isFavorite = false,
      multicastChannels = listOf("Eco 99FM Live", "Eco Hits", "Eco Chillout"),
      selectedMulticast = 0
    ),
    RadioStation(
      id = "p3",
      presetNumber = 3,
      callSign = "103FM",
      frequency = "103.0",
      band = "FM",
      genreTag = "TALK & NEWS",
      currentArtist = "רדיו 103FM",
      currentTrack = "שיחות ישירות ופרשנות • Non-Stop Talk",
      currentShow = "שיחות עם מאזינים • אקטואליה",
      audioQuality = "HD AUDIO",
      streamUrl = "https://cdn.cybercdn.live/103FM/Live/icecast.audio",
      albumArtwork = defaultArtwork,
      isFavorite = false,
      multicastChannels = listOf("103FM שידור חי", "103 פודקאסטים", "מיטב השיחות"),
      selectedMulticast = 0
    ),
    RadioStation(
      id = "p4",
      presetNumber = 4,
      callSign = "GLZ",
      frequency = "96.6",
      band = "FM",
      genreTag = "NEWS / TALK",
      currentArtist = "גלי צה\"ל",
      currentTrack = "יומן החדשות • Galei Zahal Live",
      currentShow = "מה בוער • נכון להבוקר",
      audioQuality = "HD1",
      streamUrl = "https://glzwizzlv.bynetcdn.com/glz_mp3",
      albumArtwork = defaultArtwork,
      isFavorite = false,
      multicastChannels = listOf("גלצ שידור חי", "יומן צבאי", "משדרים מיוחדים"),
      selectedMulticast = 0
    ),
    RadioStation(
      id = "p5",
      presetNumber = 5,
      callSign = "GLGLZ",
      frequency = "91.8",
      band = "FM",
      genreTag = "MUSIC & TRAFFIC",
      currentArtist = "גלגלצ",
      currentTrack = "המצעד הרשמי של ישראל • דיווחי תנועה",
      currentShow = "קולות החיילים • מדינה בדרך",
      audioQuality = "HD1 STEREO",
      streamUrl = "https://glzwizzlv.bynetcdn.com/glglz_mp3",
      albumArtwork = defaultArtwork,
      isFavorite = true,
      multicastChannels = listOf("גלגלצ שידור חי", "גלגלצ רוק", "גלגלצ סופשבוע"),
      selectedMulticast = 0
    ),
    RadioStation(
      id = "p6",
      presetNumber = 6,
      callSign = "102FM",
      frequency = "102.0",
      band = "FM",
      genreTag = "URBAN BEATS",
      currentArtist = "רדיו תל אביב",
      currentTrack = "הקצב של העיר • Non-Stop City Hits",
      currentShow = "תל אביב בבוקר • Tel Aviv Live",
      audioQuality = "FM STEREO",
      streamUrl = "https://102.livecdn.biz/102fm_aac",
      albumArtwork = defaultArtwork,
      isFavorite = false,
      multicastChannels = listOf("102FM Live", "Club Mix", "נוסטלגיה"),
      selectedMulticast = 0
    ),
    RadioStation(
      id = "p7",
      presetNumber = 7,
      callSign = "KAN 88",
      frequency = "88.0",
      band = "FM",
      genreTag = "ALTERNATIVE",
      currentArtist = "כאן 88",
      currentTrack = "מוזיקה מעולה • Rock, Indie & Jazz",
      currentShow = "ערב עירוני • הבחירות של כאן 88",
      audioQuality = "HD2 LIVE",
      streamUrl = "https://kanliveicy.media.kan.org.il/icy/kan88_mp3",
      albumArtwork = defaultArtwork,
      isFavorite = false,
      multicastChannels = listOf("כאן 88 שידור חי", "כאן גימל", "כאן קול המוסיקה"),
      selectedMulticast = 0
    ),
    RadioStation(
      id = "p8",
      presetNumber = 8,
      callSign = "91FM",
      frequency = "91.0",
      band = "FM",
      genreTag = "MEDITERRANEAN",
      currentArtist = "לב המדינה",
      currentTrack = "מוזיקה ים תיכונית וישראלית",
      currentShow = "חגיגה בלב המדינה • Live Mizrahi Hits",
      audioQuality = "CLEAR 50KW",
      streamUrl = "https://cdn.cybercdn.live/Lev_Hamedina/Audio/icecast.audio",
      albumArtwork = defaultArtwork,
      isFavorite = false,
      multicastChannels = listOf("91FM שידור חי", "ים תיכוני ישן", "חאפלה"),
      selectedMulticast = 0
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

  private val _currentBand = MutableStateFlow(TunerBand.PRESETS)
  val currentBand: StateFlow<TunerBand> = _currentBand.asStateFlow()

  private val _isDaylightMode = MutableStateFlow(true)
  val isDaylightMode: StateFlow<Boolean> = _isDaylightMode.asStateFlow()

  private val _steeringWheelNotification = MutableStateFlow<String?>(null)
  val steeringWheelNotification: StateFlow<String?> = _steeringWheelNotification.asStateFlow()

  private val _isDirectTuneOpen = MutableStateFlow(false)
  val isDirectTuneOpen: StateFlow<Boolean> = _isDirectTuneOpen.asStateFlow()

  private val _isScanning = MutableStateFlow(false)
  val isScanning: StateFlow<Boolean> = _isScanning.asStateFlow()

  private var scanJob: Job? = null
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

  fun setBand(band: TunerBand) {
    _currentBand.value = band
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
    showWheelNotification("$source: P${targetStation.presetNumber} ${targetStation.frequency} ${targetStation.callSign}")
  }

  fun playPreviousStation(source: String = "Control Dock") {
    val list = _stations.value
    val prevIndex = if (currentIndex.value - 1 < 0) list.size - 1 else currentIndex.value - 1
    _currentIndex.value = prevIndex
    val targetStation = list[prevIndex]
    player.playStation(targetStation)
    showWheelNotification("$source: P${targetStation.presetNumber} ${targetStation.frequency} ${targetStation.callSign}")
  }

  fun togglePlayPause() {
    player.togglePlayPause { currentStation.value }
  }

  fun seekUp() {
    val curr = currentStation.value
    val currentFreq = curr.frequency.toFloatOrNull() ?: 98.5f
    val nextFreq = if (curr.band == "AM") {
      (currentFreq + 10f).toInt().toString()
    } else {
      String.format(java.util.Locale.US, "%.1f", (currentFreq + 0.2f).coerceIn(87.5f, 107.9f))
    }
    updateFrequency(nextFreq)
    showWheelNotification("Seek + tuned to $nextFreq ${curr.band}")
  }

  fun seekDown() {
    val curr = currentStation.value
    val currentFreq = curr.frequency.toFloatOrNull() ?: 98.5f
    val prevFreq = if (curr.band == "AM") {
      (currentFreq - 10f).toInt().toString()
    } else {
      String.format(java.util.Locale.US, "%.1f", (currentFreq - 0.2f).coerceIn(87.5f, 107.9f))
    }
    updateFrequency(prevFreq)
    showWheelNotification("Seek - tuned to $prevFreq ${curr.band}")
  }

  private fun updateFrequency(newFreq: String) {
    val curr = currentStation.value
    val updated = curr.copy(
      frequency = newFreq,
      currentTrack = "Broadcasting on $newFreq ${curr.band}"
    )
    val list = _stations.value.toMutableList()
    list[_currentIndex.value] = updated
    _stations.value = list
    player.playStation(updated)
  }

  fun toggleFavorite(stationId: String) {
    val list = _stations.value.map {
      if (it.id == stationId) it.copy(isFavorite = !it.isFavorite) else it
    }
    _stations.value = list
  }

  fun selectMulticast(index: Int) {
    val curr = currentStation.value
    val updated = curr.copy(selectedMulticast = index)
    val list = _stations.value.toMutableList()
    list[_currentIndex.value] = updated
    _stations.value = list
    val channelName = curr.multicastChannels.getOrElse(index) { "HD-${index + 1}" }
    showWheelNotification("Multicast: $channelName")
  }

  fun openDirectTune() {
    _isDirectTuneOpen.value = true
  }

  fun closeDirectTune() {
    _isDirectTuneOpen.value = false
  }

  fun tuneDirectFrequency(frequency: String, band: String = "FM") {
    _isDirectTuneOpen.value = false
    val cleanFreq = frequency.trim()
    val curr = currentStation.value
    val updated = curr.copy(
      frequency = cleanFreq,
      band = band,
      callSign = "CUSTOM $cleanFreq",
      currentShow = "Direct Frequency Reception",
      currentTrack = "Broadcasting on $cleanFreq $band"
    )
    val list = _stations.value.toMutableList()
    list[_currentIndex.value] = updated
    _stations.value = list
    player.playStation(updated)
    showWheelNotification("Tuned Direct: $cleanFreq $band")
  }

  fun toggleScan() {
    if (_isScanning.value) {
      _isScanning.value = false
      scanJob?.cancel()
      showWheelNotification("Station Scan Stopped")
    } else {
      _isScanning.value = true
      showWheelNotification("Station Scan Started (Preview 6s)")
      scanJob?.cancel()
      scanJob = viewModelScope.launch {
        while (_isScanning.value) {
          delay(6000)
          playNextStation("Auto Scan")
        }
      }
    }
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
    scanJob?.cancel()
    notificationJob?.cancel()
    player.release()
  }
}
