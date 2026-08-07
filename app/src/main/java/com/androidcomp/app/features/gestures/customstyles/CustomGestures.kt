package com.androidcomp.app.features.gestures.customstyles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

/** 1. Swipe to Dismiss Card — drag horizontally past a threshold to dismiss, with reset. */
@Composable
fun SwipeToDismissCard(dismissed: Boolean, onDismissedChange: (Boolean) -> Unit) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val dismissThresholdPx = 260f

    Column(Modifier.fillMaxWidth()) {
        if (!dismissed) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .graphicsLayer { translationX = offsetX.value }
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFFEFF3FF))
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                scope.launch {
                                    if (abs(offsetX.value) > dismissThresholdPx) {
                                        offsetX.animateTo(
                                            if (offsetX.value > 0) 900f else -900f,
                                            animationSpec = tween(250)
                                        )
                                        onDismissedChange(true)
                                    } else {
                                        offsetX.animateTo(0f, animationSpec = spring())
                                    }
                                }
                            },
                            onHorizontalDrag = { change, dragAmount ->
                                change.consume()
                                scope.launch { offsetX.snapTo(offsetX.value + dragAmount) }
                            }
                        )
                    }
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text("Swipe me left or right", color = Color(0xFF2F4374))
            }
        } else {
            Button(onClick = {
                scope.launch { offsetX.snapTo(0f) }
                onDismissedChange(false)
            }) {
                Text("Reset card")
            }
        }
    }
}

/** 2. Pinch to Zoom — two-finger pinch scales a box, clamped to a min/max range. */
@Composable
fun PinchToZoomBox() {
    var scale by remember { mutableFloatStateOf(1f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF2F2F2))
            .pointerInput(Unit) {
                detectTransformGestures { _, _, zoom, _ ->
                    scale = (scale * zoom).coerceIn(0.6f, 3f)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Outlined.Image,
            contentDescription = null,
            tint = Color(0xFF6C5CE7),
            modifier = Modifier
                .size(56.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale }
        )
    }
}

/** 3. Pull to Refresh — drag down past a threshold to trigger a refreshing indicator. */
@Composable
fun PullToRefreshDemo(refreshing: Boolean, onTriggerRefresh: () -> Unit) {
    val pullOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val pullThresholdPx = 120f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF7F8FA))
            .pointerInput(refreshing) {
                if (refreshing) return@pointerInput
                detectVerticalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (pullOffset.value > pullThresholdPx) {
                                onTriggerRefresh()
                            }
                            pullOffset.animateTo(0f, animationSpec = spring())
                        }
                    },
                    onVerticalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            pullOffset.snapTo((pullOffset.value + dragAmount).coerceIn(0f, 160f))
                        }
                    }
                )
            },
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            Modifier
                .padding(top = 14.dp)
                .graphicsLayer { translationY = pullOffset.value }
        ) {
            if (refreshing) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Text("  Refreshing...", color = Color(0xFF5B6472))
                }
            } else {
                Text("Pull down to refresh", color = Color(0xFF9AA2AF))
            }
        }
    }
}

/** 4. Long Press Context Trigger — long-press scales card down and reveals action row. */
@Composable
fun LongPressContextCard() {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "longPressScale")

    Column(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale }
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFEFEAFB))
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            pressed = true
                            tryAwaitRelease()
                            pressed = false
                        },
                        onLongPress = { pressed = true }
                    )
                }
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text("Long press this card", color = Color(0xFF4B3A82))
        }
        AnimatedVisibility(visible = pressed) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                IconButton(onClick = {}) { Icon(Icons.Outlined.Share, contentDescription = "Share") }
                IconButton(onClick = {}) { Icon(Icons.Outlined.Archive, contentDescription = "Archive") }
                IconButton(onClick = {}) { Icon(Icons.Outlined.Delete, contentDescription = "Delete") }
            }
        }
    }
}

/** 5. Double Tap Like Animation — double tap pops a heart in and fades it back out. */
@Composable
fun DoubleTapLikeBox() {
    var showHeart by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF2C3E50))
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        showHeart = true
                        scope.launch {
                            delay(700)
                            showHeart = false
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = showHeart,
            enter = scaleIn(animationSpec = spring(dampingRatio = 0.4f)) + fadeIn(),
            exit = scaleOut() + fadeOut(animationSpec = tween(400))
        ) {
            Icon(
                Icons.Outlined.Favorite,
                contentDescription = "Liked",
                tint = Color(0xFFE74C3C),
                modifier = Modifier.size(72.dp)
            )
        }
        if (!showHeart) {
            Text("Double tap to like", color = Color(0xFFBFC9D4))
        }
    }
}
