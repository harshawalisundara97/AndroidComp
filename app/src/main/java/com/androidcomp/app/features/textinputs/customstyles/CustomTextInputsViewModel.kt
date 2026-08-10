package com.androidcomp.app.features.textinputs.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CustomTextInputsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(CustomTextInputsState())
    val state: StateFlow<CustomTextInputsState> = _state.asStateFlow()

    fun setFloatingLabelValue(value: String) {
        _state.value = _state.value.copy(floatingLabelValue = value)
    }

    fun setSearchValue(value: String) {
        _state.value = _state.value.copy(searchValue = value)
    }

    fun setOtpValue(value: String) {
        if (value.length <= 6) {
            _state.value = _state.value.copy(otpValue = value)
        }
    }

    fun setPassword(value: String) {
        _state.value = _state.value.copy(password = value)
    }

    fun togglePasswordVisible() {
        _state.value = _state.value.copy(passwordVisible = !_state.value.passwordVisible)
    }

    fun setUnderlineValue(value: String) {
        _state.value = _state.value.copy(underlineValue = value)
    }
}
