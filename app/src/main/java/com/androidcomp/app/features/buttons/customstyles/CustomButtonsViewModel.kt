package com.androidcomp.app.features.buttons.customstyles

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
class CustomButtonsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CustomButtonsState())
    val state: StateFlow<CustomButtonsState> = _state.asStateFlow()

    fun startLoadingDemo() {
        if (_state.value.loadState != ButtonLoadState.IDLE) return
        viewModelScope.launch {
            _state.value = _state.value.copy(loadState = ButtonLoadState.LOADING)
            delay(1500)
            _state.value = _state.value.copy(loadState = ButtonLoadState.SUCCESS)
            delay(1200)
            _state.value = _state.value.copy(loadState = ButtonLoadState.IDLE)
        }
    }
}
