package com.androidcomp.app.features.sensors.customstyles

data class SensorStylesState(
    val accelerometerAngle: Float = 0f,
    val compassHeading: Float = 0f,
    val stepCount: Int = 0,
    val stepGoal: Int = 10000,
    val brightnessLevel: Float = 0.5f,
    val isNear: Boolean = false
)
