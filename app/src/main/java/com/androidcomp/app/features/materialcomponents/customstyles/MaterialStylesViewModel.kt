package com.androidcomp.app.features.materialcomponents.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MaterialStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MaterialStylesState())
    val state: StateFlow<MaterialStylesState> = _state.asStateFlow()

    fun toggleChip(label: String) {
        val current = _state.value.selectedChips
        _state.value = _state.value.copy(
            selectedChips = if (current.contains(label)) current - label else current + label
        )
    }

    fun toggleSpeedDial() {
        _state.value = _state.value.copy(speedDialExpanded = !_state.value.speedDialExpanded)
    }

    fun showBottomSheet() {
        _state.value = _state.value.copy(bottomSheetVisible = true)
    }

    fun hideBottomSheet() {
        _state.value = _state.value.copy(bottomSheetVisible = false)
    }

    fun toggleBadgeUnread() {
        _state.value = _state.value.copy(badgeUnread = !_state.value.badgeUnread)
    }

    fun selectSegment(index: Int) {
        _state.value = _state.value.copy(selectedSegment = index)
    }
}
