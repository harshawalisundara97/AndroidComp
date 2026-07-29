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

    val all: List<ComponentSpec> = listOf(exoPlayer, mediaSession)
}
