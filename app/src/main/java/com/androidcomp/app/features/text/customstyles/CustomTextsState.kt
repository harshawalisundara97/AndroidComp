package com.androidcomp.app.features.text.customstyles

data class CustomTextsState(
    val gradientVariant: Int = 0,
    val expanded: Boolean = false,
    val counterTarget: Int = 0,
    val highlighted: Boolean = false
)
