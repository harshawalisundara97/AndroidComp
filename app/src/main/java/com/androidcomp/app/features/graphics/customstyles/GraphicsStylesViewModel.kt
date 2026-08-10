package com.androidcomp.app.features.graphics.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GraphicsStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(GraphicsStylesState())
    val state: StateFlow<GraphicsStylesState> = _state.asStateFlow()

    fun increaseProgressRing() {
        val next = (_state.value.progressRingPercent + 20f).let { if (it > 100f) 0f else it }
        _state.value = _state.value.copy(progressRingPercent = next)
    }

    fun redrawSparkline() {
        _state.value = _state.value.copy(sparklineRedrawKey = _state.value.sparklineRedrawKey + 1)
    }
}
