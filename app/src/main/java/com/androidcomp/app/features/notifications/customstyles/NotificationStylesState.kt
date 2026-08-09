package com.androidcomp.app.features.notifications.customstyles

data class NotificationStylesState(
    val expandableExpanded: Boolean = false,
    val snackbarVisible: Boolean = false,
    val badgeCount: Int = 0,
    val stackExpanded: Boolean = false,
    val bannerVisible: Boolean = false
)
