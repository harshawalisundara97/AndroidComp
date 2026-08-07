package com.androidcomp.app.features.gestures.customstyles

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
class GestureStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(GestureStylesState())
    val state: StateFlow<GestureStylesState> = _state.asStateFlow()

    fun setSwipeCardDismissed(dismissed: Boolean) {
        _state.value = _state.value.copy(swipeCardDismissed = dismissed)
    }

    fun triggerPullToRefresh() {
        if (_state.value.pullToRefreshRefreshing) return
        viewModelScope.launch {
            _state.value = _state.value.copy(pullToRefreshRefreshing = true)
            delay(1400)
            _state.value = _state.value.copy(pullToRefreshRefreshing = false)
        }
    }
}
