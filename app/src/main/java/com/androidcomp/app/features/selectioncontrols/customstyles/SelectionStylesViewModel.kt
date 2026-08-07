package com.androidcomp.app.features.selectioncontrols.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SelectionStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SelectionStylesState())
    val state: StateFlow<SelectionStylesState> = _state.asStateFlow()

    fun selectPlan(plan: String) {
        _state.value = _state.value.copy(selectedPlan = plan)
    }

    fun setStarRating(rating: Int) {
        _state.value = _state.value.copy(starRating = rating.coerceIn(0, 5))
    }

    fun selectColor(index: Int) {
        _state.value = _state.value.copy(selectedColorIndex = index)
    }

    fun selectSegment(index: Int) {
        _state.value = _state.value.copy(segmentedIndex = index)
    }

    fun incrementStepper() {
        _state.value = _state.value.copy(stepperCount = (_state.value.stepperCount + 1).coerceAtMost(99))
    }

    fun decrementStepper() {
        _state.value = _state.value.copy(stepperCount = (_state.value.stepperCount - 1).coerceAtLeast(0))
    }
}
