package com.androidcomp.app.features.networking.customstyles

data class NetworkingStylesState(
    val isOnline: Boolean = true,
    val retryFailed: Boolean = false,
    val retryLoading: Boolean = false,
    val shimmerContentLoaded: Boolean = false,
    val requestPhase: RequestPhase = RequestPhase.IDLE,
    val signalLevel: SignalLevel = SignalLevel.EXCELLENT
)

enum class RequestPhase {
    IDLE, SENDING, AWAITING, DONE
}

enum class SignalLevel {
    EXCELLENT, GOOD, POOR
}
