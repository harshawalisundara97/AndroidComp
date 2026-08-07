package com.androidcomp.app.features.text.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CustomTextsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CustomTextsState())
    val state: StateFlow<CustomTextsState> = _state.asStateFlow()

    fun cycleGradient() {
        _state.value = _state.value.copy(gradientVariant = (_state.value.gradientVariant + 1) % 3)
    }

    fun toggleExpanded() {
        _state.value = _state.value.copy(expanded = !_state.value.expanded)
    }

    fun triggerCounter() {
        val next = if (_state.value.counterTarget == 0) 2847 else 0
        _state.value = _state.value.copy(counterTarget = next)
    }

    fun toggleHighlight() {
        _state.value = _state.value.copy(highlighted = !_state.value.highlighted)
    }
}
