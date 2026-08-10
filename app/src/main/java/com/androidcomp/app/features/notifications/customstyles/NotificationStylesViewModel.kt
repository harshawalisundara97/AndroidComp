package com.androidcomp.app.features.notifications.customstyles

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NotificationStylesViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(NotificationStylesState())
    val state: StateFlow<NotificationStylesState> = _state.asStateFlow()

    fun toggleExpandable() {
        _state.value = _state.value.copy(expandableExpanded = !_state.value.expandableExpanded)
    }

    fun showSnackbar() {
        _state.value = _state.value.copy(snackbarVisible = true)
    }

    fun dismissSnackbar() {
        _state.value = _state.value.copy(snackbarVisible = false)
    }

    fun incrementBadge() {
        _state.value = _state.value.copy(badgeCount = _state.value.badgeCount + 1)
    }

    fun resetBadge() {
        _state.value = _state.value.copy(badgeCount = 0)
    }

    fun toggleStack() {
        _state.value = _state.value.copy(stackExpanded = !_state.value.stackExpanded)
    }

    fun showBanner() {
        _state.value = _state.value.copy(bannerVisible = true)
    }

    fun dismissBanner() {
        _state.value = _state.value.copy(bannerVisible = false)
    }
}
