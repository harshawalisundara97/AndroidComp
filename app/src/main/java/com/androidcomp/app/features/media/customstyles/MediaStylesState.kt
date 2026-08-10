package com.androidcomp.app.features.media.customstyles

/**
 * Local UI state for the 5 custom media designs. Designs 1 (Audio Player Card) and
 * 3 (Mini Player) simulate playback progress; design 4 (Scrubber) tracks a drag position;
 * design 5 (Queue Item) tracks which row is "now playing". Designs 2 (Video Thumbnail) only
 * need transient press-animation state via MutableInteractionSource, so they have no field here.
 */
data class MediaStylesState(
    val audioPlayerIsPlaying: Boolean = false,
    val audioPlayerElapsedSeconds: Int = 0,
    val miniPlayerIsPlaying: Boolean = false,
    val miniPlayerProgress: Float = 0f,
    val scrubberProgress: Float = 0.35f,
    val nowPlayingQueueItemId: String? = "queue-2"
) {
    companion object {
        const val AUDIO_PLAYER_TOTAL_SECONDS = 217 // 3:37
        const val SCRUBBER_TOTAL_SECONDS = 240 // 4:00
    }
}
