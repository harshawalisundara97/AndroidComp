package com.androidcomp.app.features.camera.customstyles

data class CameraStylesState(
    val shutterPressed: Boolean = false,
    val gridVisible: Boolean = true,
    val flashMode: Int = 0, // 0 = Off, 1 = Auto, 2 = On
    val captureMode: Int = 0, // 0 = Photo, 1 = Video
    val countdownValue: Int? = null,
    val justCaptured: Boolean = false
)
