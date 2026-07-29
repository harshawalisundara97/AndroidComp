package com.androidcomp.app.features.selectioncontrols.playground

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectionPlaygroundViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SelectionPlaygroundState())
    val state: StateFlow<SelectionPlaygroundState> = _state.asStateFlow()

    fun setChecked(checked: Boolean) {
        _state.value = _state.value.copy(checked = checked)
    }

    fun setEnabled(enabled: Boolean) {
        _state.value = _state.value.copy(enabled = enabled)
    }
}
