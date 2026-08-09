package com.androidcomp.app.features.materialcomponents.customstyles

data class MaterialStylesState(
    val selectedChips: Set<String> = setOf("Wireless"),
    val speedDialExpanded: Boolean = false,
    val bottomSheetVisible: Boolean = false,
    val badgeUnread: Boolean = true,
    val selectedSegment: Int = 0
)
