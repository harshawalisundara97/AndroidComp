package com.androidcomp.app.features.sliders.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SliderStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SliderStylesState())
    val state: StateFlow<SliderStylesState> = _state.asStateFlow()

    fun setVerticalVolume(value: Float) {
        _state.value = _state.value.copy(verticalVolume = value.coerceIn(0f, 1f))
    }

    fun setRange(low: Float, high: Float) {
        _state.value = _state.value.copy(rangeLow = low, rangeHigh = high)
    }

    fun setSteppedValue(value: Float) {
        _state.value = _state.value.copy(steppedValue = value)
    }

    fun setGradientValue(value: Float) {
        _state.value = _state.value.copy(gradientValue = value.coerceIn(0f, 1f))
    }

    fun setDialValue(value: Float) {
        _state.value = _state.value.copy(dialValue = value.coerceIn(0f, 100f))
    }
}
