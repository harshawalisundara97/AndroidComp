package com.androidcomp.app.features.maps.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MapStylesState())
    val state: StateFlow<MapStylesState> = _state.asStateFlow()

    fun triggerPinDrop() {
        _state.value = _state.value.copy(pinDropTrigger = _state.value.pinDropTrigger + 1)
    }

    fun setSatelliteStyle(isSatellite: Boolean) {
        _state.value = _state.value.copy(isSatelliteStyle = isSatellite)
    }
}
