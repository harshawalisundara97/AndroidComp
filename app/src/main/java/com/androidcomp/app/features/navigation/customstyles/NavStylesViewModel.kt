package com.androidcomp.app.features.navigation.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NavStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(NavStylesState())
    val state: StateFlow<NavStylesState> = _state.asStateFlow()

    fun selectMorphing(index: Int) {
        _state.value = _state.value.copy(morphingSelectedIndex = index)
    }

    fun selectUnderline(index: Int) {
        _state.value = _state.value.copy(underlineSelectedIndex = index)
    }

    fun selectSegmented(index: Int) {
        _state.value = _state.value.copy(segmentedSelectedIndex = index)
    }

    fun selectPill(index: Int) {
        _state.value = _state.value.copy(pillSelectedIndex = index)
    }

    fun selectRail(index: Int) {
        _state.value = _state.value.copy(railSelectedIndex = index)
    }
}
