package com.androidcomp.app.features.progress.customstyles

data class ProgressStylesState(
    val dottedStep: Int = 0,
    val circularPercent: Int = 40,
    val segmentedStep: Int = 1,
    val wavePercent: Int = 35
)
