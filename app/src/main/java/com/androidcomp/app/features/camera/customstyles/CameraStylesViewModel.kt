package com.androidcomp.app.features.camera.customstyles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CameraStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CameraStylesState())
    val state: StateFlow<CameraStylesState> = _state.asStateFlow()

    fun setShutterPressed(pressed: Boolean) {
        _state.value = _state.value.copy(shutterPressed = pressed)
    }

    fun toggleGrid() {
        _state.value = _state.value.copy(gridVisible = !_state.value.gridVisible)
    }

    fun cycleFlashMode() {
        _state.value = _state.value.copy(flashMode = (_state.value.flashMode + 1) % 3)
    }

    fun setCaptureMode(mode: Int) {
        _state.value = _state.value.copy(captureMode = mode)
    }

    fun startCountdown() {
        if (_state.value.countdownValue != null) return
        viewModelScope.launch {
            for (value in 3 downTo 1) {
                _state.value = _state.value.copy(countdownValue = value, justCaptured = false)
                delay(700)
            }
            _state.value = _state.value.copy(countdownValue = null, justCaptured = true)
            delay(900)
            _state.value = _state.value.copy(justCaptured = false)
        }
    }
}
