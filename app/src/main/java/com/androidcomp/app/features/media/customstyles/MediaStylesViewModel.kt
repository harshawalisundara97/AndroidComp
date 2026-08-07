package com.androidcomp.app.features.media.customstyles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcomp.app.features.media.customstyles.MediaStylesState.Companion.AUDIO_PLAYER_TOTAL_SECONDS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MediaStylesState())
    val state: StateFlow<MediaStylesState> = _state.asStateFlow()

    private var audioPlayerJob: Job? = null
    private var miniPlayerJob: Job? = null

    fun toggleAudioPlayer() {
        val playing = !_state.value.audioPlayerIsPlaying
        _state.value = _state.value.copy(audioPlayerIsPlaying = playing)
        audioPlayerJob?.cancel()
        if (playing) {
            audioPlayerJob = viewModelScope.launch {
                while (_state.value.audioPlayerElapsedSeconds < AUDIO_PLAYER_TOTAL_SECONDS) {
                    delay(1000)
                    _state.value = _state.value.copy(
                        audioPlayerElapsedSeconds = _state.value.audioPlayerElapsedSeconds + 1
                    )
                }
                _state.value = _state.value.copy(audioPlayerIsPlaying = false)
            }
        }
    }

    fun toggleMiniPlayer() {
        val playing = !_state.value.miniPlayerIsPlaying
        _state.value = _state.value.copy(miniPlayerIsPlaying = playing)
        miniPlayerJob?.cancel()
        if (playing) {
            miniPlayerJob = viewModelScope.launch {
                while (_state.value.miniPlayerProgress < 1f) {
                    delay(200)
                    _state.value = _state.value.copy(
                        miniPlayerProgress = (_state.value.miniPlayerProgress + 0.01f).coerceAtMost(1f)
                    )
                }
                _state.value = _state.value.copy(miniPlayerIsPlaying = false)
            }
        }
    }

    fun onScrubberProgressChanged(progress: Float) {
        _state.value = _state.value.copy(scrubberProgress = progress.coerceIn(0f, 1f))
    }

    fun onQueueItemSelected(id: String) {
        _state.value = _state.value.copy(nowPlayingQueueItemId = id)
    }
}
