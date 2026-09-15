package com.androidcomp.app.features.images.customstyles2

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ImageStyles2ViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ImageStyles2State())
    val state: StateFlow<ImageStyles2State> = _state.asStateFlow()

    fun toggleKenBurns() {
        _state.value = _state.value.copy(kenBurnsPlaying = !_state.value.kenBurnsPlaying)
    }

    fun nextFilter() {
        _state.value = _state.value.copy(filterIndex = (_state.value.filterIndex + 1) % 4)
    }

    fun expandThumbnail(index: Int?) {
        _state.value = _state.value.copy(expandedThumbnail = index)
    }

    fun setCarouselPage(page: Int) {
        _state.value = _state.value.copy(carouselPage = page)
    }

    fun toggleBlurReveal() {
        _state.value = _state.value.copy(blurRevealed = !_state.value.blurRevealed)
    }
}
