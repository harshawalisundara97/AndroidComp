package com.androidcomp.app.features.buttons.customstyles

enum class ButtonLoadState { IDLE, LOADING, SUCCESS }

data class CustomButtonsState(
    val loadState: ButtonLoadState = ButtonLoadState.IDLE
)
