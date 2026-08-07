package com.androidcomp.app.features.images.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CustomImagesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CustomImagesState())
    val state: StateFlow<CustomImagesState> = _state.asStateFlow()

    fun toggleZoomed() {
        _state.value = _state.value.copy(zoomed = !_state.value.zoomed)
    }

    fun cycleAvatarStatus() {
        val next = when (_state.value.avatarStatus) {
            AvatarStatus.ONLINE -> AvatarStatus.AWAY
            AvatarStatus.AWAY -> AvatarStatus.OFFLINE
            AvatarStatus.OFFLINE -> AvatarStatus.ONLINE
        }
        _state.value = _state.value.copy(avatarStatus = next)
    }

    fun setComparisonFraction(fraction: Float) {
        _state.value = _state.value.copy(comparisonFraction = fraction.coerceIn(0f, 1f))
    }
}
