package com.androidcomp.app.features.text.playground

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TextPlaygroundViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(TextPlaygroundState())
    val state: StateFlow<TextPlaygroundState> = _state.asStateFlow()

    fun setText(text: String) {
        _state.value = _state.value.copy(text = text)
    }

    fun setBold(bold: Boolean) {
        _state.value = _state.value.copy(bold = bold)
    }

    fun setItalic(italic: Boolean) {
        _state.value = _state.value.copy(italic = italic)
    }
}
