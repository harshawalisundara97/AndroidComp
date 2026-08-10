package com.androidcomp.app.features.animations.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AnimationStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AnimationStylesState())
    val state: StateFlow<AnimationStylesState> = _state.asStateFlow()

    fun replayStagger() {
        _state.value = _state.value.copy(staggerReplayKey = _state.value.staggerReplayKey + 1)
    }

    fun setExpandCardExpanded(expanded: Boolean) {
        _state.value = _state.value.copy(expandCardExpanded = expanded)
    }
}
