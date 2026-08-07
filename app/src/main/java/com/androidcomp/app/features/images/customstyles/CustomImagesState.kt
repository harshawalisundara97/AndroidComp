package com.androidcomp.app.features.images.customstyles

enum class AvatarStatus { ONLINE, AWAY, OFFLINE }

data class CustomImagesState(
    val zoomed: Boolean = false,
    val avatarStatus: AvatarStatus = AvatarStatus.ONLINE,
    val comparisonFraction: Float = 0.5f
)
