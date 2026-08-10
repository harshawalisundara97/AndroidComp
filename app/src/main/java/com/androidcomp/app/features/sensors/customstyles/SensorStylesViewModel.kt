package com.androidcomp.app.features.sensors.customstyles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SensorStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(SensorStylesState())
    val state: StateFlow<SensorStylesState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(1500)
                _state.value = _state.value.copy(accelerometerAngle = Random.nextFloat() * 360f)
            }
        }
    }

    fun simulateCompassReading() {
        _state.value = _state.value.copy(compassHeading = Random.nextFloat() * 360f)
    }

    fun simulateStep() {
        _state.value = _state.value.copy(stepCount = (_state.value.stepCount + Random.nextInt(20, 60)).coerceAtMost(_state.value.stepGoal))
    }

    fun simulateLightReading() {
        _state.value = _state.value.copy(brightnessLevel = Random.nextFloat())
    }

    fun toggleProximity() {
        _state.value = _state.value.copy(isNear = !_state.value.isNear)
    }
}
