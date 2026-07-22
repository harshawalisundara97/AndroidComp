package com.androidcomp.app.core.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

data class AppColors(
    val success: Color,
    val warning: Color,
    val info: Color,
    val border: Color
)

val LightAppColors = AppColors(
    success = SuccessGreen,
    warning = WarningOrange,
    info = InfoBlue,
    border = BorderLight
)

val DarkAppColors = AppColors(
    success = SuccessGreenDark,
    warning = WarningOrangeDark,
    info = InfoBlueDark,
    border = BorderDark
)

val LocalAppColors = staticCompositionLocalOf { LightAppColors }
