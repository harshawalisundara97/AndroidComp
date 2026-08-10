package com.androidcomp.app.features.media.customstyles

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DragHandle
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Pause
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/** 1. Audio Player Card with Waveform — play/pause with a static waveform and live elapsed time. */
@Composable
fun AudioPlayerCard(
    isPlaying: Boolean,
    elapsedSeconds: Int,
    totalSeconds: Int,
    onTogglePlay: () -> Unit
) {
    val progress = if (totalSeconds > 0) elapsedSeconds.toFloat() / totalSeconds else 0f
    val barHeights = remember {
        listOf(10, 18, 26, 14, 30, 22, 12, 28, 20, 16, 24, 32, 18, 10, 26, 20, 14, 22, 12, 18)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFF2F6FED))
                .clickable { onTogglePlay() },
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(targetState = isPlaying, label = "audioPlayerIcon") { playing ->
                Icon(
                    imageVector = if (playing) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                    contentDescription = if (playing) "Pause" else "Play",
                    tint = Color.White
                )
            }
        }
        Column(Modifier.padding(start = 14.dp).weight(1f)) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth().height(32.dp)
            ) {
                barHeights.forEachIndexed { index, height ->
                    val played = index.toFloat() / barHeights.size < progress
                    Box(
                        modifier = Modifier
                            .padding(end = 2.dp)
                            .width(3.dp)
                            .height(height.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                if (played) Color(0xFF2F6FED) else MaterialTheme.colorScheme.outlineVariant
                            )
                    )
                }
            }
            Row(
                Modifier.fillMaxWidth().padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(formatSeconds(elapsedSeconds), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(formatSeconds(totalSeconds), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** 2. Video Thumbnail with Play Overlay — placeholder thumbnail, tap-scaled play button, duration badge. */
@Composable
fun VideoThumbnailCard(durationLabel: String, onClick: () -> Unit = {}) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val playButtonScale by animateFloatAsState(if (pressed) 0.85f else 1f, label = "videoPlayScale")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF1F2937))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer { scaleX = playButtonScale; scaleY = playButtonScale }
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.28f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.PlayArrow,
                contentDescription = "Play video",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color.Black.copy(alpha = 0.65f))
                .padding(horizontal = 6.dp, vertical = 3.dp)
        ) {
            Text(durationLabel, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

/** 3. Mini Player (Bottom Bar) — compact now-playing bar with a thin animated progress line. */
@Composable
fun MiniPlayerBar(
    title: String,
    isPlaying: Boolean,
    progress: Float,
    onTogglePlay: () -> Unit
) {
    val animatedProgress by animateFloatAsState(progress, animationSpec = tween(200), label = "miniPlayerProgress")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF6C5CE7)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 12.dp).weight(1f)
            )
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onTogglePlay() },
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(targetState = isPlaying, label = "miniPlayerIcon") { playing ->
                    Icon(
                        imageVector = if (playing) Icons.Outlined.Pause else Icons.Outlined.PlayArrow,
                        contentDescription = if (playing) "Pause" else "Play",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .background(Color(0xFF6C5CE7))
            )
        }
    }
}

/** 4. Playback Progress Scrubber — draggable custom scrubber with live elapsed/remaining labels. */
@Composable
fun PlaybackScrubber(
    progress: Float,
    totalSeconds: Int,
    onProgressChanged: (Float) -> Unit
) {
    var trackWidthPx by remember { mutableFloatStateOf(0f) }
    var isDragging by remember { mutableStateOf(false) }
    val thumbScale by animateFloatAsState(if (isDragging) 1.4f else 1f, label = "scrubberThumbScale")
    val elapsedSeconds = (progress * totalSeconds).roundToInt()
    val remainingSeconds = totalSeconds - elapsedSeconds

    Column(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(28.dp)
                .pointerInput(totalSeconds) {
                    detectDragGestures(
                        onDragStart = { isDragging = true },
                        onDragEnd = { isDragging = false },
                        onDragCancel = { isDragging = false }
                    ) { change, _ ->
                        change.consume()
                        if (trackWidthPx > 0f) {
                            val fraction = (change.position.x / trackWidthPx).coerceIn(0f, 1f)
                            onProgressChanged(fraction)
                        }
                    }
                }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align(Alignment.CenterStart)
            ) {
                trackWidthPx = size.width
                drawLine(
                    color = Color(0xFFE0E0E0),
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width, size.height / 2f),
                    strokeWidth = size.height
                )
                drawLine(
                    color = Color(0xFF2F6FED),
                    start = Offset(0f, size.height / 2f),
                    end = Offset(size.width * progress, size.height / 2f),
                    strokeWidth = size.height
                )
            }
            val thumbOffsetDp = with(LocalDensity.current) { (trackWidthPx * progress).toDp() }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = thumbOffsetDp)
                    .graphicsLayer {
                        scaleX = thumbScale; scaleY = thumbScale
                        translationX = -14f
                    }
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2F6FED))
            )
        }
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(formatSeconds(elapsedSeconds), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text("-${formatSeconds(remainingSeconds)}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** 5. Media Queue List Item — reorderable-looking queue row with an animated now-playing highlight. */
@Composable
fun MediaQueueItem(
    title: String,
    artist: String,
    durationLabel: String,
    isNowPlaying: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (isNowPlaying) Color(0xFF2F6FED).copy(alpha = 0.12f) else Color.Transparent,
        label = "queueItemBackground"
    )
    val indicatorWidth by animateDpAsState(if (isNowPlaying) 3.dp else 0.dp, label = "queueItemIndicator")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .width(indicatorWidth)
                .height(32.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color(0xFF2F6FED))
        )
        Spacer(Modifier.width(if (indicatorWidth > 0.dp) 8.dp else 0.dp))
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF9CA3AF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.MusicNote, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }
        Column(Modifier.padding(start = 12.dp).weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isNowPlaying) Color(0xFF2F6FED) else MaterialTheme.colorScheme.onSurface,
                fontWeight = if (isNowPlaying) FontWeight.SemiBold else FontWeight.Normal
            )
            Text(artist, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Text(
            durationLabel,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(end = 8.dp)
        )
        Icon(
            Icons.Outlined.DragHandle,
            contentDescription = "Drag to reorder",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

private fun formatSeconds(totalSeconds: Int): String {
    val clamped = totalSeconds.coerceAtLeast(0)
    val minutes = clamped / 60
    val seconds = clamped % 60
    return "%d:%02d".format(minutes, seconds)
}
