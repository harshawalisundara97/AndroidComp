package com.androidcomp.app.features.menus.customstyles2

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MenuStyles2ViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(MenuStyles2State())
    val state: StateFlow<MenuStyles2State> = _state.asStateFlow()

    fun updateSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
    }

    fun toggleSearchMenu() {
        _state.value = _state.value.copy(searchMenuExpanded = !_state.value.searchMenuExpanded)
    }

    fun closeSearchMenu() {
        _state.value = _state.value.copy(searchMenuExpanded = false)
    }

    fun toggleQuickActions() {
        _state.value = _state.value.copy(quickActionsExpanded = !_state.value.quickActionsExpanded)
    }

    fun closeQuickActions() {
        _state.value = _state.value.copy(quickActionsExpanded = false)
    }

    fun pushBreadcrumb(segment: String) {
        _state.value = _state.value.copy(breadcrumbPath = _state.value.breadcrumbPath + segment)
    }

    fun popBreadcrumbTo(index: Int) {
        _state.value = _state.value.copy(breadcrumbPath = _state.value.breadcrumbPath.take(index + 1))
    }

    fun toggleToolbarMenu() {
        _state.value = _state.value.copy(toolbarMenuExpanded = !_state.value.toolbarMenuExpanded)
    }

    fun closeToolbarMenu() {
        _state.value = _state.value.copy(toolbarMenuExpanded = false)
    }

    fun clearToolbarBadge() {
        _state.value = _state.value.copy(toolbarBadgeCount = 0, toolbarMenuExpanded = false)
    }

    fun setCarouselIndex(index: Int) {
        _state.value = _state.value.copy(carouselMenuIndex = index)
    }
}
