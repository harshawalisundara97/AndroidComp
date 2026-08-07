package com.androidcomp.app.features.selectioncontrols.customstyles

data class SelectionStylesState(
    val selectedPlan: String = "Pro",
    val starRating: Int = 3,
    val selectedColorIndex: Int = 0,
    val segmentedIndex: Int = 0,
    val stepperCount: Int = 1
)
