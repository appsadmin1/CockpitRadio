package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.util.Log
import com.example.model.RadioStation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RadioAudioPlayer(
  private val context: Context,
  private val onNextStation: () -> Unit,
  private val onPreviousStation: () -> Unit
) {
  private val TAG = "RadioAudioPlayer"

  @Volatile
  private var mediaPlayer: MediaPlayer? = null
  private var mediaSession: MediaSession? = null

  private val _isPlaying = MutableStateFlow(false)
  val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

  private val _isBuffering = MutableStateFlow(false)
  val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

  private var currentStation: RadioStation? = null
  private val scope = CoroutineScope(Dispatchers.Main + Job())
  private var prepareJob: Job? = null

  init {
    setupMediaSession()
  }

  private fun setupMediaSession() {
    try {
      mediaSession = MediaSession(context, "CockpitRadioSession").apply {
        setCallback(object : MediaSession.Callback() {
          override fun onPlay() {
            resume()
          }

          override fun onPause() {
            pause()
          }

          override fun onSkipToNext() {
            onNextStation()
          }

          override fun onSkipToPrevious() {
            onPreviousStation()
          }

          override fun onStop() {
            pause()
          }
        })
        updatePlaybackState(PlaybackState.STATE_PAUSED)
        isActive = true
      }
    } catch (e: Exception) {
      Log.e(TAG, "Error setting up MediaSession", e)
    }
  }

  private fun updatePlaybackState(state: Int) {
    try {
      val stateBuilder = PlaybackState.Builder()
        .setActions(
          PlaybackState.ACTION_PLAY or
            PlaybackState.ACTION_PAUSE or
            PlaybackState.ACTION_PLAY_PAUSE or
            PlaybackState.ACTION_SKIP_TO_NEXT or
            PlaybackState.ACTION_SKIP_TO_PREVIOUS or
            PlaybackState.ACTION_STOP
        )
        .setState(state, PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1.0f)
      mediaSession?.setPlaybackState(stateBuilder.build())
    } catch (e: Exception) {
      Log.w(TAG, "Error updating playback state", e)
    }
  }

  fun playStation(station: RadioStation) {
    currentStation = station
    _isBuffering.value = true
    _isPlaying.value = true
    updatePlaybackState(PlaybackState.STATE_BUFFERING)

    prepareJob?.cancel()
    prepareJob = scope.launch {
      // Release previous player on background thread so UI never freezes
      val oldPlayer = mediaPlayer
      mediaPlayer = null
      withContext(Dispatchers.IO) {
        try {
          oldPlayer?.stop()
          oldPlayer?.release()
        } catch (_: Exception) {}
      }

      // Initialize new player on background thread
      withContext(Dispatchers.IO) {
        try {
          val newPlayer = MediaPlayer().apply {
            setAudioAttributes(
              AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build()
            )
            setDataSource(station.streamUrl)

            setOnPreparedListener { mp ->
              scope.launch(Dispatchers.Main) {
                _isBuffering.value = false
                _isPlaying.value = true
                try {
                  mp.start()
                  updatePlaybackState(PlaybackState.STATE_PLAYING)
                } catch (e: Exception) {
                  Log.e(TAG, "Failed starting media player", e)
                }
              }
            }

            setOnErrorListener { _, what, extra ->
              Log.w(TAG, "MediaPlayer stream error: what=$what extra=$extra")
              scope.launch(Dispatchers.Main) {
                _isBuffering.value = false
                // Keep playing state indicator so UI reflects user intent
                updatePlaybackState(PlaybackState.STATE_PLAYING)
              }
              true
            }

            setOnCompletionListener {
              scope.launch(Dispatchers.Main) {
                if (_isPlaying.value && currentStation?.id == station.id) {
                  playStation(station)
                }
              }
            }

            prepareAsync()
          }
          mediaPlayer = newPlayer
        } catch (e: Exception) {
          Log.e(TAG, "Error setting up stream: ${station.streamUrl}", e)
          scope.launch(Dispatchers.Main) {
            _isBuffering.value = false
          }
        }
      }
    }
  }

  fun togglePlayPause(currentStationSupplier: () -> RadioStation) {
    if (_isPlaying.value) {
      pause()
    } else {
      currentStation?.let {
        resume()
      } ?: run {
        playStation(currentStationSupplier())
      }
    }
  }

  fun pause() {
    _isPlaying.value = false
    _isBuffering.value = false
    prepareJob?.cancel()
    scope.launch(Dispatchers.IO) {
      try {
        mediaPlayer?.pause()
      } catch (e: Exception) {
        Log.e(TAG, "Error pausing player", e)
      }
    }
    updatePlaybackState(PlaybackState.STATE_PAUSED)
  }

  fun resume() {
    _isPlaying.value = true
    val p = mediaPlayer
    if (p != null) {
      scope.launch(Dispatchers.IO) {
        try {
          p.start()
          withContext(Dispatchers.Main) {
            updatePlaybackState(PlaybackState.STATE_PLAYING)
          }
        } catch (e: Exception) {
          Log.e(TAG, "Error resuming player", e)
          withContext(Dispatchers.Main) {
            currentStation?.let { playStation(it) }
          }
        }
      }
    } else {
      currentStation?.let { playStation(it) }
    }
  }

  fun release() {
    prepareJob?.cancel()
    val p = mediaPlayer
    mediaPlayer = null
    scope.launch(Dispatchers.IO) {
      try {
        p?.stop()
        p?.release()
      } catch (_: Exception) {}
    }
    try {
      mediaSession?.apply {
        isActive = false
        release()
      }
      mediaSession = null
    } catch (_: Exception) {}
  }
}
