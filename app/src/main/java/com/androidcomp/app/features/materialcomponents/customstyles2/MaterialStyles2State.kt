package com.androidcomp.app.features.materialcomponents.customstyles2

data class MaterialStyles2State(
    val selectedDate: Int = 15,
    val selectedHour: Int = 9,
    val selectedRailIndex: Int = 0,
    val snackbarVisible: Boolean = false,
    val dropdownExpanded: Boolean = false,
    val selectedOption: String = "Newest"
)
