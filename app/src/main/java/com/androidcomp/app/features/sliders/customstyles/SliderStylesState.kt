package com.androidcomp.app.features.sliders.customstyles

data class SliderStylesState(
    val verticalVolume: Float = 0.5f,
    val rangeLow: Float = 20f,
    val rangeHigh: Float = 80f,
    val steppedValue: Float = 2f,
    val gradientValue: Float = 0.4f,
    val dialValue: Float = 35f
)
