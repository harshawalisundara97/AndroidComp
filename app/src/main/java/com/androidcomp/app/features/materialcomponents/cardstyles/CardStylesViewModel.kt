package com.androidcomp.app.features.materialcomponents.cardstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CardStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CardStylesState())
    val state: StateFlow<CardStylesState> = _state.asStateFlow()

    fun incrementStat() {
        _state.value = _state.value.copy(statValue = _state.value.statValue + 37)
    }

    fun toggleImageSaved() {
        _state.value = _state.value.copy(imageSaved = !_state.value.imageSaved)
    }
}
