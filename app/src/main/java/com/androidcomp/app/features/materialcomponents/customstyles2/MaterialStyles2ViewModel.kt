package com.androidcomp.app.features.materialcomponents.customstyles2

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MaterialStyles2ViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MaterialStyles2State())
    val state: StateFlow<MaterialStyles2State> = _state.asStateFlow()

    fun selectDate(day: Int) {
        _state.value = _state.value.copy(selectedDate = day)
    }

    fun selectHour(hour: Int) {
        _state.value = _state.value.copy(selectedHour = hour)
    }

    fun selectRailIndex(index: Int) {
        _state.value = _state.value.copy(selectedRailIndex = index)
    }

    fun showSnackbar() {
        _state.value = _state.value.copy(snackbarVisible = true)
    }

    fun dismissSnackbar() {
        _state.value = _state.value.copy(snackbarVisible = false)
    }

    fun toggleDropdown() {
        _state.value = _state.value.copy(dropdownExpanded = !_state.value.dropdownExpanded)
    }

    fun selectOption(option: String) {
        _state.value = _state.value.copy(selectedOption = option, dropdownExpanded = false)
    }
}
