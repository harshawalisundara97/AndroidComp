package com.androidcomp.app.features.selectioncontrols.toggles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ToggleStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ToggleStylesState())
    val state: StateFlow<ToggleStylesState> = _state.asStateFlow()

    fun setFluidSpring(checked: Boolean) {
        _state.value = _state.value.copy(fluidSpring = checked)
    }

    fun setDayNight(checked: Boolean) {
        _state.value = _state.value.copy(dayNight = checked)
    }

    fun setCyberpunkNeon(checked: Boolean) {
        _state.value = _state.value.copy(cyberpunkNeon = checked)
    }

    fun setNeumorphic(checked: Boolean) {
        _state.value = _state.value.copy(neumorphic = checked)
    }

    fun setElasticPill(checked: Boolean) {
        _state.value = _state.value.copy(elasticPill = checked)
    }
}
