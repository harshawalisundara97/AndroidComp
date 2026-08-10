package com.androidcomp.app.features.textinputs.customstyles

data class CustomTextInputsState(
    val floatingLabelValue: String = "",
    val searchValue: String = "",
    val otpValue: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val underlineValue: String = ""
)
