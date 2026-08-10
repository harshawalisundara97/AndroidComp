package com.androidcomp.app.features.dialogs.customstyles

data class DialogStylesState(
    val bottomSheetVisible: Boolean = false,
    val iconConfirmVisible: Boolean = false,
    val successCelebrationVisible: Boolean = false,
    val inputDialogVisible: Boolean = false,
    val inputText: String = "",
    val fullBleedImageVisible: Boolean = false
)
