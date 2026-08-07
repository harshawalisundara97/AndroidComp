package com.androidcomp.app.features.layouts.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LayoutStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LayoutStylesState())
    val state: StateFlow<LayoutStylesState> = _state.asStateFlow()

    fun toggleAccordion(index: Int) {
        _state.value = _state.value.copy(
            expandedAccordionIndex = if (_state.value.expandedAccordionIndex == index) -1 else index
        )
    }
}
