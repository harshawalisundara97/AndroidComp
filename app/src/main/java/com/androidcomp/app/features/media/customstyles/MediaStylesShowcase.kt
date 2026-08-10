package com.androidcomp.app.features.media.customstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun MediaStylesShowcase(
    state: MediaStylesState,
    onToggleAudioPlayer: () -> Unit,
    onToggleMiniPlayer: () -> Unit,
    onScrubberProgressChanged: (Float) -> Unit,
    onQueueItemSelected: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        MediaStyleRow(
            "1. Audio Player Card with Waveform",
            "Play/pause crossfades icons; a static waveform fills in and elapsed time advances while \"playing\".",
            {
                AudioPlayerCard(
                    isPlaying = state.audioPlayerIsPlaying,
                    elapsedSeconds = state.audioPlayerElapsedSeconds,
                    totalSeconds = MediaStylesState.AUDIO_PLAYER_TOTAL_SECONDS,
                    onTogglePlay = onToggleAudioPlayer
                )
            },
            audioPlayerCardCode
        )
        MediaStyleRow(
            "2. Video Thumbnail with Play Overlay",
            "Placeholder thumbnail with a centered play button that scales down on press, plus a duration badge.",
            { VideoThumbnailCard(durationLabel = "12:47") },
            videoThumbnailCode
        )
        MediaStyleRow(
            "3. Mini Player (Bottom Bar)",
            "Compact now-playing bar with a thin progress line that animates filling while \"playing\".",
            {
                MiniPlayerBar(
                    title = "Midnight Drive",
                    isPlaying = state.miniPlayerIsPlaying,
                    progress = state.miniPlayerProgress,
                    onTogglePlay = onToggleMiniPlayer
                )
            },
            miniPlayerCode
        )
        MediaStyleRow(
            "4. Playback Progress Scrubber",
            "Custom draggable scrubber — drag the thumb to update the elapsed/remaining time labels live.",
            {
                PlaybackScrubber(
                    progress = state.scrubberProgress,
                    totalSeconds = MediaStylesState.SCRUBBER_TOTAL_SECONDS,
                    onProgressChanged = onScrubberProgressChanged
                )
            },
            scrubberCode
        )
        MediaStyleRow(
            "5. Media Queue List Item",
            "A queued-track row with a decorative drag handle; tap a row to animate its now-playing highlight.",
            {
                Column(Modifier.fillMaxWidth()) {
                    MediaQueueItem(
                        title = "Golden Hour",
                        artist = "JVKE",
                        durationLabel = "3:29",
                        isNowPlaying = state.nowPlayingQueueItemId == "queue-1",
                        onClick = { onQueueItemSelected("queue-1") }
                    )
                    MediaQueueItem(
                        title = "As It Was",
                        artist = "Harry Styles",
                        durationLabel = "2:47",
                        isNowPlaying = state.nowPlayingQueueItemId == "queue-2",
                        onClick = { onQueueItemSelected("queue-2") }
                    )
                    MediaQueueItem(
                        title = "Flowers",
                        artist = "Miley Cyrus",
                        durationLabel = "3:20",
                        isNowPlaying = state.nowPlayingQueueItemId == "queue-3",
                        onClick = { onQueueItemSelected("queue-3") }
                    )
                }
            },
            queueItemCode
        )
    }
}

@Composable
private fun MediaStyleRow(
    title: String,
    description: String,
    media: @Composable () -> Unit,
    code: String
) {
    Column(Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Text(
            description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
        )
        Column(Modifier.padding(bottom = 10.dp)) {
            media()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val audioPlayerCardCode = """
    val progress = elapsedSeconds.toFloat() / totalSeconds

    Row(Modifier.clip(RoundedCornerShape(24.dp)).background(surfaceVariant).padding(16.dp)) {
        Box(
            Modifier.size(44.dp).clip(CircleShape).background(Color(0xFF2F6FED))
                .clickable { onTogglePlay() }
        ) {
            AnimatedContent(isPlaying) { playing ->
                Icon(if (playing) Icons.Outlined.Pause else Icons.Outlined.PlayArrow, tint = Color.White)
            }
        }
        Column {
            Row(verticalAlignment = Alignment.Bottom) {
                barHeights.forEachIndexed { index, height ->
                    val played = index.toFloat() / barHeights.size < progress
                    Box(
                        Modifier.width(3.dp).height(height.dp).clip(RoundedCornerShape(2.dp))
                            .background(if (played) Color(0xFF2F6FED) else outlineVariant)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                Text(formatSeconds(elapsedSeconds))
                Text(formatSeconds(totalSeconds))
            }
        }
    }

    // Simulated playback ticker in the ViewModel:
    viewModelScope.launch {
        while (elapsedSeconds < totalSeconds) {
            delay(1000)
            elapsedSeconds++
        }
    }
""".trimIndent()

private val videoThumbnailCode = """
    val pressed by interactionSource.collectIsPressedAsState()
    val playButtonScale by animateFloatAsState(if (pressed) 0.85f else 1f)

    Box(
        Modifier
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1F2937))
            .clickable(interactionSource, indication = null) { onClick() }
    ) {
        Box(
            Modifier
                .align(Alignment.Center)
                .graphicsLayer { scaleX = playButtonScale; scaleY = playButtonScale }
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.28f))
        ) {
            Icon(Icons.Outlined.PlayArrow, tint = Color.White)
        }
        Box(
            Modifier
                .align(Alignment.BottomEnd)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Text(durationLabel, color = Color.White, fontSize = 11.sp)
        }
    }
""".trimIndent()

private val miniPlayerCode = """
    val animatedProgress by animateFloatAsState(progress, tween(200))

    Column(Modifier.clip(RoundedCornerShape(18.dp)).background(surfaceVariant)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF6C5CE7)))
            Text(title, modifier = Modifier.weight(1f))
            Box(
                Modifier.size(36.dp).clip(CircleShape).background(surface)
                    .clickable { onTogglePlay() }
            ) {
                AnimatedContent(isPlaying) { playing ->
                    Icon(if (playing) Icons.Outlined.Pause else Icons.Outlined.PlayArrow)
                }
            }
        }
        Box(Modifier.fillMaxWidth().height(3.dp)) {
            Box(Modifier.fillMaxHeight().fillMaxWidth(animatedProgress).background(Color(0xFF6C5CE7)))
        }
    }
""".trimIndent()

private val scrubberCode = """
    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val thumbScale by animateFloatAsState(if (isDragging) 1.4f else 1f)

    Box(
        Modifier
            .height(28.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { isDragging = true },
                    onDragEnd = { isDragging = false }
                ) { change, _ ->
                    change.consume()
                    val fraction = (change.position.x / trackWidthPx).coerceIn(0f, 1f)
                    onProgressChanged(fraction)
                }
            }
    ) {
        Canvas(Modifier.fillMaxWidth().height(4.dp)) {
            trackWidthPx = size.width
            drawLine(Color(0xFFE0E0E0), Offset(0f, size.height / 2f), Offset(size.width, size.height / 2f), size.height)
            drawLine(Color(0xFF2F6FED), Offset(0f, size.height / 2f), Offset(size.width * progress, size.height / 2f), size.height)
        }
        Box(
            Modifier
                .padding(start = with(LocalDensity.current) { (trackWidthPx * progress).toDp() })
                .graphicsLayer { scaleX = thumbScale; scaleY = thumbScale; translationX = -14f }
                .size(14.dp)
                .clip(CircleShape)
                .background(Color(0xFF2F6FED))
        )
    }
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Text(formatSeconds(elapsedSeconds))
        Text("-" + formatSeconds(remainingSeconds))
    }
""".trimIndent()

private val queueItemCode = """
    val backgroundColor by animateColorAsState(
        if (isNowPlaying) Color(0xFF2F6FED).copy(alpha = 0.12f) else Color.Transparent
    )
    val indicatorWidth by animateDpAsState(if (isNowPlaying) 3.dp else 0.dp)

    Row(
        Modifier
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 10.dp)
    ) {
        Box(Modifier.width(indicatorWidth).height(32.dp).background(Color(0xFF2F6FED)))
        Box(Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFF9CA3AF)))
        Column(Modifier.weight(1f)) {
            Text(title, fontWeight = if (isNowPlaying) FontWeight.SemiBold else FontWeight.Normal)
            Text(artist, style = MaterialTheme.typography.bodySmall)
        }
        Text(durationLabel)
        Icon(Icons.Outlined.DragHandle, contentDescription = "Drag to reorder")
    }
""".trimIndent()
