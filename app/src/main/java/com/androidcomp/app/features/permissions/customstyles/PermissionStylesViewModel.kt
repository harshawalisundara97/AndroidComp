package com.androidcomp.app.features.permissions.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class PermissionStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(PermissionStylesState())
    val state: StateFlow<PermissionStylesState> = _state.asStateFlow()

    fun setRationaleGranted(granted: Boolean) {
        _state.value = _state.value.copy(rationaleGranted = granted)
    }

    fun cycleStatusChip() {
        val next = when (_state.value.statusChipState) {
            PermissionGrantState.NOT_REQUESTED -> PermissionGrantState.GRANTED
            PermissionGrantState.GRANTED -> PermissionGrantState.DENIED
            PermissionGrantState.DENIED -> PermissionGrantState.NOT_REQUESTED
        }
        _state.value = _state.value.copy(statusChipState = next)
    }

    fun toggleSettingsBannerDismissed() {
        _state.value = _state.value.copy(settingsBannerDismissed = !_state.value.settingsBannerDismissed)
    }

    fun toggleChecklistItem(label: String) {
        _state.value = _state.value.copy(
            checklist = _state.value.checklist.map {
                if (it.label == label) it.copy(granted = !it.granted) else it
            }
        )
    }

    fun setShieldPromptGranted(granted: Boolean) {
        _state.value = _state.value.copy(shieldPromptGranted = granted)
    }
}
