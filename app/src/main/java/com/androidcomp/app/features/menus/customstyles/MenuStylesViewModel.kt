package com.androidcomp.app.features.menus.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MenuStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MenuStylesState())
    val state: StateFlow<MenuStylesState> = _state.asStateFlow()

    fun setContextMenuSelection(selection: String) {
        _state.value = _state.value.copy(contextMenuSelection = selection)
    }

    fun setSegmentedSelection(selection: String) {
        _state.value = _state.value.copy(segmentedSelection = selection)
    }
}
