package com.androidcomp.app.features.networking.customstyles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NetworkingStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(NetworkingStylesState())
    val state: StateFlow<NetworkingStylesState> = _state.asStateFlow()

    private var retryAttempt = 0

    fun toggleConnection() {
        _state.value = _state.value.copy(isOnline = !_state.value.isOnline)
    }

    fun retry() {
        if (_state.value.retryLoading) return
        viewModelScope.launch {
            _state.value = _state.value.copy(retryLoading = true)
            delay(900)
            retryAttempt++
            val succeeded = retryAttempt % 2 == 0
            _state.value = _state.value.copy(retryLoading = false, retryFailed = !succeeded)
        }
    }

    fun simulateResponseArrive() {
        if (_state.value.shimmerContentLoaded) return
        viewModelScope.launch {
            delay(1200)
            _state.value = _state.value.copy(shimmerContentLoaded = true)
        }
    }

    fun resetShimmer() {
        _state.value = _state.value.copy(shimmerContentLoaded = false)
    }

    fun sendRequest() {
        if (_state.value.requestPhase != RequestPhase.IDLE && _state.value.requestPhase != RequestPhase.DONE) return
        viewModelScope.launch {
            _state.value = _state.value.copy(requestPhase = RequestPhase.SENDING)
            delay(700)
            _state.value = _state.value.copy(requestPhase = RequestPhase.AWAITING)
            delay(900)
            _state.value = _state.value.copy(requestPhase = RequestPhase.DONE)
            delay(1500)
            _state.value = _state.value.copy(requestPhase = RequestPhase.IDLE)
        }
    }

    fun cycleSignal() {
        val next = when (_state.value.signalLevel) {
            SignalLevel.EXCELLENT -> SignalLevel.GOOD
            SignalLevel.GOOD -> SignalLevel.POOR
            SignalLevel.POOR -> SignalLevel.EXCELLENT
        }
        _state.value = _state.value.copy(signalLevel = next)
    }
}
