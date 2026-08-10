package com.androidcomp.app.features.progress.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

private const val DOTTED_STEP_COUNT = 5
private const val SEGMENTED_STEP_COUNT = 5

@HiltViewModel
class ProgressStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ProgressStylesState())
    val state: StateFlow<ProgressStylesState> = _state.asStateFlow()

    fun advanceDottedStep() {
        val next = (_state.value.dottedStep + 1).coerceAtMost(DOTTED_STEP_COUNT)
        _state.value = _state.value.copy(dottedStep = next)
    }

    fun retreatDottedStep() {
        val prev = (_state.value.dottedStep - 1).coerceAtLeast(0)
        _state.value = _state.value.copy(dottedStep = prev)
    }

    fun setCircularPercent(percent: Int) {
        _state.value = _state.value.copy(circularPercent = percent.coerceIn(0, 100))
    }

    fun increaseCircularPercent() {
        setCircularPercent(_state.value.circularPercent + 10)
    }

    fun advanceSegmentedStep() {
        val next = (_state.value.segmentedStep + 1).coerceAtMost(SEGMENTED_STEP_COUNT)
        _state.value = _state.value.copy(segmentedStep = next)
    }

    fun resetSegmentedStep() {
        _state.value = _state.value.copy(segmentedStep = 0)
    }

    fun setWavePercent(percent: Int) {
        _state.value = _state.value.copy(wavePercent = percent.coerceIn(0, 100))
    }

    fun increaseWavePercent() {
        setWavePercent(_state.value.wavePercent + 10)
    }
}
