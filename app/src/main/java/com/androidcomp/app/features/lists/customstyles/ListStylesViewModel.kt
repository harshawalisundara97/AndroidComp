package com.androidcomp.app.features.lists.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ListStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(ListStylesState())
    val state: StateFlow<ListStylesState> = _state.asStateFlow()

    fun deleteSwipeItem(id: Int) {
        _state.value = _state.value.copy(
            swipeItems = _state.value.swipeItems.filterNot { it.id == id }
        )
    }

    fun toggleExpandedRow(id: Int) {
        _state.value = _state.value.copy(
            expandedRowId = if (_state.value.expandedRowId == id) null else id
        )
    }

    fun moveReorderItem(fromIndex: Int, toIndex: Int) {
        val items = _state.value.reorderItems.toMutableList()
        if (fromIndex !in items.indices || toIndex !in items.indices) return
        val item = items.removeAt(fromIndex)
        items.add(toIndex, item)
        _state.value = _state.value.copy(reorderItems = items)
    }
}
