package com.androidcomp.app.features.layouts.customstyles2

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LayoutStyles2ViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(LayoutStyles2State())
    val state: StateFlow<LayoutStyles2State> = _state.asStateFlow()

    fun moveItem(from: Int, to: Int) {
        val items = _state.value.reorderItems.toMutableList()
        if (from in items.indices && to in items.indices) {
            val item = items.removeAt(from)
            items.add(to, item)
            _state.value = _state.value.copy(reorderItems = items)
        }
    }

    fun selectMasterItem(index: Int) {
        _state.value = _state.value.copy(masterSelectedIndex = index)
    }

    fun updateParallaxOffset(offset: Float) {
        _state.value = _state.value.copy(parallaxScrollOffset = offset)
    }

    fun selectTab(index: Int) {
        _state.value = _state.value.copy(selectedTabIndex = index)
    }

    fun addWrapCard() {
        _state.value = _state.value.copy(wrapCardCount = (_state.value.wrapCardCount + 1).coerceAtMost(9))
    }

    fun removeWrapCard() {
        _state.value = _state.value.copy(wrapCardCount = (_state.value.wrapCardCount - 1).coerceAtLeast(1))
    }
}
