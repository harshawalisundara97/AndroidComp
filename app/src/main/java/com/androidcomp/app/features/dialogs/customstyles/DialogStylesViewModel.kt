package com.androidcomp.app.features.dialogs.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DialogStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(DialogStylesState())
    val state: StateFlow<DialogStylesState> = _state.asStateFlow()

    fun showBottomSheet() {
        _state.value = _state.value.copy(bottomSheetVisible = true)
    }

    fun hideBottomSheet() {
        _state.value = _state.value.copy(bottomSheetVisible = false)
    }

    fun showIconConfirm() {
        _state.value = _state.value.copy(iconConfirmVisible = true)
    }

    fun hideIconConfirm() {
        _state.value = _state.value.copy(iconConfirmVisible = false)
    }

    fun showSuccessCelebration() {
        _state.value = _state.value.copy(successCelebrationVisible = true)
    }

    fun hideSuccessCelebration() {
        _state.value = _state.value.copy(successCelebrationVisible = false)
    }

    fun showInputDialog() {
        _state.value = _state.value.copy(inputDialogVisible = true)
    }

    fun hideInputDialog() {
        _state.value = _state.value.copy(inputDialogVisible = false)
    }

    fun updateInputText(text: String) {
        _state.value = _state.value.copy(inputText = text)
    }

    fun showFullBleedImage() {
        _state.value = _state.value.copy(fullBleedImageVisible = true)
    }

    fun hideFullBleedImage() {
        _state.value = _state.value.copy(fullBleedImageVisible = false)
    }
}
