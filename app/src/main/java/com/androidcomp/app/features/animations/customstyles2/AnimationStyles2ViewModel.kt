package com.androidcomp.app.features.animations.customstyles2

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AnimationStyles2ViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AnimationStyles2State())
    val state: StateFlow<AnimationStyles2State> = _state.asStateFlow()

    fun toggleMorph() {
        _state.value = _state.value.copy(isSquare = !_state.value.isSquare)
    }

    fun dropBall() {
        _state.value = _state.value.copy(ballDropCount = _state.value.ballDropCount + 1)
    }

    fun nextPage() {
        _state.value = _state.value.copy(pageIndex = _state.value.pageIndex + 1)
    }

    fun toggleDrawPath() {
        _state.value = _state.value.copy(pathProgress = !_state.value.pathProgress)
    }

    fun insertItem() {
        val size = _state.value.insertedItems.size + 1
        val next = "Item $size"
        _state.value = _state.value.copy(insertedItems = _state.value.insertedItems + next)
    }

    fun resetItems() {
        _state.value = _state.value.copy(insertedItems = listOf("Item 1", "Item 2", "Item 3"))
    }
}
