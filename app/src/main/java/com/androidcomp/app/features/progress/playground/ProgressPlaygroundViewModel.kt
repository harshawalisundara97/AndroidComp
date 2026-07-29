package com.androidcomp.app.features.progress.playground

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProgressPlaygroundViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ProgressPlaygroundState())
    val state: StateFlow<ProgressPlaygroundState> = _state.asStateFlow()

    fun setProgress(progress: Float) {
        _state.value = _state.value.copy(progress = progress)
    }
}
