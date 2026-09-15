package com.androidcomp.app.features.animations.customstyles2

data class AnimationStyles2State(
    val isSquare: Boolean = false,
    val ballDropCount: Int = 0,
    val pageIndex: Int = 0,
    val pathProgress: Boolean = false,
    val insertedItems: List<String> = listOf("Item 1", "Item 2", "Item 3")
)
