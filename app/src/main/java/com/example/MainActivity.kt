package com.example

import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.model.CockpitScreen
import com.example.ui.components.BottomAutomotiveDock
import com.example.ui.components.DirectTuneDialog
import com.example.ui.components.SteeringWheelHudBanner
import com.example.ui.components.TopCockpitBar
import com.example.ui.screens.NowPlayingScreen
import com.example.ui.screens.PresetsScreen
import com.example.ui.theme.CockpitRadioTheme
import com.example.viewmodel.RadioViewModel

class MainActivity : ComponentActivity() {

  private val viewModel: RadioViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val isDaylightMode by viewModel.isDaylightMode.collectAsState()
      val currentScreen by viewModel.currentScreen.collectAsState()
      val currentBand by viewModel.currentBand.collectAsState()
      val stations by viewModel.stations.collectAsState()
      val currentStation by viewModel.currentStation.collectAsState()
      val isPlaying by viewModel.isPlaying.collectAsState()
      val isBuffering by viewModel.isBuffering.collectAsState()
      val steeringWheelNotification by viewModel.steeringWheelNotification.collectAsState()
      val isDirectTuneOpen by viewModel.isDirectTuneOpen.collectAsState()
      val isScanning by viewModel.isScanning.collectAsState()

      // Handle Back button to return from Now Playing to Presets
      BackHandler(enabled = currentScreen == CockpitScreen.NOW_PLAYING) {
        viewModel.setScreen(CockpitScreen.PRESETS)
      }

      CockpitRadioTheme(isDaylight = isDaylightMode) {
        Scaffold(
          modifier = Modifier
            .fillMaxSize()
            .testTag("cockpit_root_scaffold"),
          topBar = {
            TopCockpitBar(
              currentBand = currentBand,
              isDaylightMode = isDaylightMode,
              isNowPlayingScreen = currentScreen == CockpitScreen.NOW_PLAYING,
              onToggleDaylight = { viewModel.toggleDaylightMode() },
              onSelectBand = { band ->
                viewModel.setBand(band)
                viewModel.setScreen(CockpitScreen.PRESETS)
              },
              onOpenPresets = { viewModel.setScreen(CockpitScreen.PRESETS) },
              onOpenNowPlaying = { viewModel.setScreen(CockpitScreen.NOW_PLAYING) },
              onOpenDirectTune = { viewModel.openDirectTune() }
            )
          },
          bottomBar = {
            BottomAutomotiveDock(
              isNowPlayingScreen = currentScreen == CockpitScreen.NOW_PLAYING,
              isPlaying = isPlaying,
              isBuffering = isBuffering,
              isScanning = isScanning,
              onPresetsClick = { viewModel.setScreen(CockpitScreen.PRESETS) },
              onNowPlayingClick = { viewModel.setScreen(CockpitScreen.NOW_PLAYING) },
              onSeekDown = { viewModel.seekDown() },
              onPrevPreset = { viewModel.playPreviousStation("Control Dock") },
              onTogglePlay = { viewModel.togglePlayPause() },
              onNextPreset = { viewModel.playNextStation("Control Dock") },
              onSeekUp = { viewModel.seekUp() },
              onScanClick = { viewModel.toggleScan() }
            )
          }
        ) { innerPadding ->
          Column(
            modifier = Modifier
              .fillMaxSize()
              .padding(innerPadding)
          ) {
            // Steering Wheel HUD & quick interactive test bar
            SteeringWheelHudBanner(
              notification = steeringWheelNotification,
              onWheelNext = { viewModel.playNextStation("Steering Wheel") },
              onWheelPrev = { viewModel.playPreviousStation("Steering Wheel") },
              onWheelPlayPause = { viewModel.togglePlayPause() }
            )

            // Main Cockpit Viewport Switcher
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
            ) {
              when (currentScreen) {
                CockpitScreen.PRESETS -> {
                  PresetsScreen(
                    stations = stations,
                    currentStation = currentStation,
                    isPlaying = isPlaying,
                    onStationSelected = { station ->
                      viewModel.selectStation(station, autoPlay = true, navigateToNowPlaying = true)
                    },
                    onToggleFavorite = { stationId ->
                      viewModel.toggleFavorite(stationId)
                    }
                  )
                }
                CockpitScreen.NOW_PLAYING -> {
                  NowPlayingScreen(
                    station = currentStation,
                    isPlaying = isPlaying,
                    onToggleFavorite = {
                      viewModel.toggleFavorite(currentStation.id)
                    },
                    onSelectMulticast = { index ->
                      viewModel.selectMulticast(index)
                    }
                  )
                }
              }
            }
          }
        }

        // Direct Frequency Keypad Dialog
        if (isDirectTuneOpen) {
          DirectTuneDialog(
            onDismiss = { viewModel.closeDirectTune() },
            onTune = { freq, band ->
              viewModel.tuneDirectFrequency(freq, band)
            }
          )
        }
      }
    }
  }

  /**
   * Handle physical steering wheel buttons, Bluetooth car head unit controls,
   * CAN-bus media keys, and external automotive keypads.
   */
  override fun dispatchKeyEvent(event: KeyEvent): Boolean {
    if (event.action == KeyEvent.ACTION_DOWN) {
      when (event.keyCode) {
        KeyEvent.KEYCODE_MEDIA_NEXT,
        KeyEvent.KEYCODE_CHANNEL_UP,
        KeyEvent.KEYCODE_PAGE_DOWN -> {
          viewModel.playNextStation("Car Wheel [NEXT]")
          return true
        }
        KeyEvent.KEYCODE_MEDIA_PREVIOUS,
        KeyEvent.KEYCODE_CHANNEL_DOWN,
        KeyEvent.KEYCODE_PAGE_UP -> {
          viewModel.playPreviousStation("Car Wheel [PREV]")
          return true
        }
        KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE,
        KeyEvent.KEYCODE_HEADSETHOOK -> {
          viewModel.togglePlayPause()
          viewModel.onSteeringWheelEvent("PLAY/MUTE")
          return true
        }
        KeyEvent.KEYCODE_MEDIA_PLAY -> {
          viewModel.player.resume()
          viewModel.onSteeringWheelEvent("PLAY")
          return true
        }
        KeyEvent.KEYCODE_MEDIA_PAUSE,
        KeyEvent.KEYCODE_MEDIA_STOP -> {
          viewModel.player.pause()
          viewModel.onSteeringWheelEvent("MUTE")
          return true
        }
        KeyEvent.KEYCODE_MEDIA_FAST_FORWARD -> {
          viewModel.seekUp()
          return true
        }
        KeyEvent.KEYCODE_MEDIA_REWIND -> {
          viewModel.seekDown()
          return true
        }
      }
    }
    return super.dispatchKeyEvent(event)
  }
}
