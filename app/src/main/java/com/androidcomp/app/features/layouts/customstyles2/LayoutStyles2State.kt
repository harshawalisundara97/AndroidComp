package com.androidcomp.app.features.layouts.customstyles2

data class LayoutStyles2State(
    val reorderItems: List<String> = listOf("Alpha", "Bravo", "Charlie", "Delta"),
    val draggingIndex: Int? = null,
    val masterSelectedIndex: Int = 0,
    val parallaxScrollOffset: Float = 0f,
    val selectedTabIndex: Int = 0,
    val wrapCardCount: Int = 5
)
