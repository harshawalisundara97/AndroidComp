package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object MediaComponentCatalog {

    private val exoPlayer = ComponentSpec(
        id = "media-exoplayer",
        category = ComponentCategory.MEDIA,
        title = "Media3 ExoPlayer + PlayerView",
        overview = "Media3's `ExoPlayer` handles audio/video playback (streaming or local), " +
            "while `PlayerView` renders the video surface and default playback controls; " +
            "embedded into Compose via `AndroidView` interop.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun VideoPlayer(mediaUrl: String, modifier: Modifier = Modifier) {
                    val context = LocalContext.current
                    val exoPlayer = remember {
                        ExoPlayer.Builder(context).build().apply {
                            setMediaItem(MediaItem.fromUri(mediaUrl))
                            prepare()
                            playWhenReady = true
                        }
                    }

                    DisposableEffect(Unit) {
                        onDispose { exoPlayer.release() }
                    }

                    AndroidView(
                        modifier = modifier,
                        factory = { ctx ->
                            PlayerView(ctx).apply { player = exoPlayer }
                        }
                    )
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <androidx.media3.ui.PlayerView
                    android:id="@+id/playerView"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    app:use_controller="true" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onPlayPauseClicked() {
                if (player.isPlaying) player.pause() else player.play()
            }
            // ViewModel can hold playback position/state via SavedStateHandle for
            // process-death restoration, while the ExoPlayer instance itself is
            // typically owned close to the UI due to Surface/lifecycle coupling.
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("playWhenReady", "Boolean", "false", "Whether playback starts automatically once media is buffered/ready."),
            ComponentProperty("repeatMode", "Int", "REPEAT_MODE_OFF", "Controls looping: OFF, ONE, or ALL."),
            ComponentProperty("useController", "Boolean", "true", "Whether PlayerView shows the default playback control overlay."),
            ComponentProperty("resizeMode", "Int", "RESIZE_MODE_FIT", "How video content is scaled/cropped within the PlayerView bounds.")
        ),
        events = listOf(
            "Player.Listener.onPlaybackStateChanged — fired on IDLE/BUFFERING/READY/ENDED transitions.",
            "Player.Listener.onIsPlayingChanged — fired when play/pause state changes.",
            "Player.Listener.onPlayerError — fired when playback fails (network, decoding, etc.)."
        ),
        bestPractices = listOf(
            "Always call `player.release()` in `onDispose`/`onStop` to free decoder and surface resources promptly.",
            "Persist and restore `player.currentPosition` across configuration changes or process death for a seamless resume experience."
        ),
        commonMistakes = listOf(
            "Creating a new ExoPlayer instance on every recomposition instead of hoisting it with `remember`, causing playback restarts and resource leaks.",
            "Forgetting to release the player when the composable leaves composition, leaking native decoder resources."
        ),
        accessibilityNotes = listOf(
            "PlayerView's default controller already exposes accessible play/pause/seek controls, but custom overlays must add their own contentDescriptions.",
            "Provide captions/subtitles (via Media3's subtitle configuration) for users who are deaf or hard of hearing."
        ),
        performanceNotes = listOf(
            "Use adaptive streaming formats (DASH/HLS) with Media3's `DefaultTrackSelector` to adjust quality to network conditions instead of a single fixed-bitrate file.",
            "Release the player and detach the surface when the app is backgrounded to avoid unnecessary decoder/battery usage."
        ),
        relatedComponentIds = listOf("media-session"),
        minApi = 21
    )

    private val mediaSession = ComponentSpec(
        id = "media-session",
        category = ComponentCategory.MEDIA,
        title = "MediaSession / MediaController",
        overview = "Media3's `MediaSession` exposes a player's transport controls (play, pause, " +
            "seek, skip) to system surfaces like the lock screen, notification, and Bluetooth/" +
            "Android Auto, while `MediaController` lets other components (or another process) " +
            "send commands to that session.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                // In a MediaSessionService (not a composable):
                class PlaybackService : MediaSessionService() {
                    private lateinit var player: ExoPlayer
                    private lateinit var mediaSession: MediaSession

                    override fun onCreate() {
                        super.onCreate()
                        player = ExoPlayer.Builder(this).build()
                        mediaSession = MediaSession.Builder(this, player).build()
                    }

                    override fun onGetSession(
                        controllerInfo: MediaSession.ControllerInfo
                    ): MediaSession = mediaSession
                }

                // In the UI layer, connect a controller:
                val controllerFuture = MediaController.Builder(
                    context,
                    SessionToken(context, ComponentName(context, PlaybackService::class.java))
                ).buildAsync()
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            fun onSkipNextClicked() {
                mediaController?.seekToNextMediaItem()
            }
            // ViewModel holds a reference to the connected MediaController and exposes
            // playback state as a StateFlow for the UI to observe.
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("player", "Player", "required", "The underlying Player (typically ExoPlayer) the session controls."),
            ComponentProperty("id", "String", "\"\"", "Unique session identifier, needed when hosting multiple sessions."),
            ComponentProperty("customLayout", "List<CommandButton>", "emptyList()", "Custom action buttons shown on the media notification."),
            ComponentProperty("sessionToken", "SessionToken", "n/a", "Token used by MediaController.Builder to locate and connect to a running session.")
        ),
        events = listOf(
            "MediaSession.Callback.onConnect — fired when a controller attempts to connect.",
            "Player.Listener.onMediaMetadataChanged — fired when track metadata (title, artist, artwork) updates, refreshing the system notification."
        ),
        bestPractices = listOf(
            "Run playback in a foreground `MediaSessionService` so playback and the session survive Activity destruction and app backgrounding.",
            "Populate `MediaMetadata` (title, artist, artwork) so lock-screen and notification surfaces render correctly."
        ),
        commonMistakes = listOf(
            "Hosting the MediaSession inside an Activity/ViewModel instead of a Service, causing playback to stop when the Activity is destroyed.",
            "Not declaring the required foreground service type (`mediaPlayback`) in the manifest, causing a crash on newer Android versions when starting the foreground service."
        ),
        accessibilityNotes = listOf(
            "System-rendered notification and lock-screen controls from MediaSession are already accessible by default via NotificationCompat's MediaStyle — avoid duplicating custom, less-accessible controls there.",
            "Ensure media metadata content descriptions (track title/artist) are meaningful text, not placeholder IDs, since screen readers announce them directly."
        ),
        performanceNotes = listOf(
            "A single shared MediaSession/Player avoids redundant decoder instances when multiple UI surfaces (widget, notification, in-app) need playback state.",
            "Debounce rapid `onMediaMetadataChanged` updates before pushing new notification content to avoid excessive system notification updates."
        ),
        relatedComponentIds = listOf("media-exoplayer"),
        minApi = 21
    )

    private val customStyles = ComponentSpec(
        id = "media-custom-styles",
        category = ComponentCategory.MEDIA,
        title = "Custom Media Player Styles",
        overview = "Five fully custom-designed media UI patterns beyond a bare PlayerView — an " +
            "audio player card with a waveform, a video thumbnail with a play overlay, a mini " +
            "player bottom bar, a draggable playback scrubber, and a queue list item — each with " +
            "genuine Compose animation and simulated (not real) playback state. Tap or drag each " +
            "one below and copy its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun AudioPlayerCard(isPlaying: Boolean, elapsedSeconds: Int, totalSeconds: Int, onTogglePlay: () -> Unit) {
                    val progress = elapsedSeconds.toFloat() / totalSeconds
                    Row(Modifier.clip(RoundedCornerShape(24.dp)).background(surfaceVariant).padding(16.dp)) {
                        Box(Modifier.size(44.dp).clip(CircleShape).background(Color(0xFF2F6FED)).clickable { onTogglePlay() }) {
                            AnimatedContent(isPlaying) { playing ->
                                Icon(if (playing) Icons.Outlined.Pause else Icons.Outlined.PlayArrow, tint = Color.White)
                            }
                        }
                        // waveform bars + elapsed/total labels driven by `progress`
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("isPlaying / progress", "Boolean / Float", "n/a", "Drives icon crossfade, waveform fill, and animated progress lines/thumbs across the five designs."),
            ComponentProperty("onTogglePlay / onProgressChanged", "() -> Unit / (Float) -> Unit", "required", "Callbacks that mutate ViewModel state; playback itself is simulated via a coroutine ticker, not real media."),
            ComponentProperty("MutableInteractionSource", "InteractionSource", "n/a", "Used by the video thumbnail's play button to derive a press-scale animation without a full state field.")
        ),
        events = listOf(
            "onTogglePlay — fired when the audio player or mini player's play/pause control is tapped.",
            "onProgressChanged — fired continuously while dragging the playback scrubber's thumb.",
            "onQueueItemSelected — fired when a queue row is tapped, updating the now-playing highlight."
        ),
        bestPractices = listOf(
            "Drive simulated playback with a cancellable coroutine loop (delay-based ticker) scoped to viewModelScope so it stops cleanly when playback is paused or the screen leaves composition.",
            "Prefer graphicsLayer-based scale/offset animations (as used for the thumbnail press and scrubber thumb) over animating layout-affecting properties, to keep drags and presses smooth."
        ),
        commonMistakes = listOf(
            "Reading real-time drag position without coercing it into the track's bounds, letting the scrubber thumb travel past the start/end of the track.",
            "Forgetting to cancel a running playback-simulation coroutine when the user pauses, causing two competing tickers to advance the same elapsed-time state."
        ),
        accessibilityNotes = listOf(
            "Custom-drawn play/pause icon buttons and the queue row's tap target must be at least 48x48dp and carry a contentDescription that reflects the current state (e.g. \"Pause\" vs \"Play\").",
            "The playback scrubber is a custom drag surface, not a Slider — add semantics (progressBarRangeInfo) so screen readers can announce and adjust position."
        ),
        performanceNotes = listOf(
            "The waveform and scrubber track are drawn once per frame via Canvas; keep the bar/segment count fixed rather than recomputing a new random layout on every recomposition.",
            "animateColorAsState/animateDpAsState on the queue item's highlight are cheap for a short list; for a long queue, drive the now-playing highlight from a single shared index comparison rather than per-item derived state."
        ),
        relatedComponentIds = listOf("media-exoplayer", "media-session"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(exoPlayer, mediaSession, customStyles)
}
