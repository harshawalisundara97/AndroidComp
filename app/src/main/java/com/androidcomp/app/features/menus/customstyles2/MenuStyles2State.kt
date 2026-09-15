package com.androidcomp.app.features.menus.customstyles2

data class MenuStyles2State(
    val searchQuery: String = "",
    val searchMenuExpanded: Boolean = false,
    val quickActionsExpanded: Boolean = false,
    val breadcrumbPath: List<String> = listOf("Home"),
    val toolbarMenuExpanded: Boolean = false,
    val toolbarBadgeCount: Int = 3,
    val carouselMenuIndex: Int = 0
)
