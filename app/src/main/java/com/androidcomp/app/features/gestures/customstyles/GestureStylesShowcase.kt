package com.androidcomp.app.features.gestures.customstyles

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
fun GestureStylesShowcase(
    swipeCardDismissed: Boolean,
    onSwipeCardDismissedChange: (Boolean) -> Unit,
    pullToRefreshRefreshing: Boolean,
    onTriggerPullToRefresh: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        GestureStyleRow(
            "1. Swipe to Dismiss Card",
            "Drag the card left or right past a threshold to fling it off-screen; tap reset to bring it back.",
            { SwipeToDismissCard(dismissed = swipeCardDismissed, onDismissedChange = onSwipeCardDismissedChange) },
            swipeToDismissCode
        )
        GestureStyleRow(
            "2. Pinch to Zoom",
            "Use a two-finger pinch to scale the icon, clamped between 0.6x and 3x.",
            { PinchToZoomBox() },
            pinchToZoomCode
        )
        GestureStyleRow(
            "3. Pull to Refresh",
            "Drag down past the threshold to trigger a refreshing indicator that auto-resets.",
            { PullToRefreshDemo(refreshing = pullToRefreshRefreshing, onTriggerRefresh = onTriggerPullToRefresh) },
            pullToRefreshCode
        )
        GestureStyleRow(
            "4. Long Press Context Trigger",
            "Long-press the card to scale it down slightly and reveal a contextual action row.",
            { LongPressContextCard() },
            longPressContextCode
        )
        GestureStyleRow(
            "5. Double Tap Like Animation",
            "Double tap the placeholder image for a classic Instagram-style heart pop animation.",
            { DoubleTapLikeBox() },
            doubleTapLikeCode
        )
    }
}

@Composable
private fun GestureStyleRow(
    title: String,
    description: String,
    content: @Composable () -> Unit,
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
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val swipeToDismissCode = """
    val offsetX = remember { Animatable(0f) }

    Box(
        Modifier
            .graphicsLayer { translationX = offsetX.value }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (abs(offsetX.value) > 260f) {
                                offsetX.animateTo(if (offsetX.value > 0) 900f else -900f, tween(250))
                                dismissed = true
                            } else {
                                offsetX.animateTo(0f, spring())
                            }
                        }
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        scope.launch { offsetX.snapTo(offsetX.value + dragAmount) }
                    }
                )
            }
    )
""".trimIndent()

private val pinchToZoomCode = """
    var scale by remember { mutableFloatStateOf(1f) }

    Box(
        Modifier.pointerInput(Unit) {
            detectTransformGestures { _, _, zoom, _ ->
                scale = (scale * zoom).coerceIn(0.6f, 3f)
            }
        }
    ) {
        Icon(
            Icons.Outlined.Image,
            contentDescription = null,
            modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale }
        )
    }
""".trimIndent()

private val pullToRefreshCode = """
    val pullOffset = remember { Animatable(0f) }

    Box(
        Modifier.pointerInput(refreshing) {
            if (refreshing) return@pointerInput
            detectVerticalDragGestures(
                onDragEnd = {
                    scope.launch {
                        if (pullOffset.value > 120f) onTriggerRefresh()
                        pullOffset.animateTo(0f, spring())
                    }
                },
                onVerticalDrag = { change, dragAmount ->
                    change.consume()
                    scope.launch { pullOffset.snapTo((pullOffset.value + dragAmount).coerceIn(0f, 160f)) }
                }
            )
        }
    ) {
        if (refreshing) CircularProgressIndicator() else Text("Pull down to refresh")
    }
""".trimIndent()

private val longPressContextCode = """
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f)

    Box(
        Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { pressed = true; tryAwaitRelease(); pressed = false },
                    onLongPress = { pressed = true }
                )
            }
    ) {
        Text("Long press this card")
    }
    AnimatedVisibility(visible = pressed) {
        Row {
            IconButton(onClick = {}) { Icon(Icons.Outlined.Share, contentDescription = "Share") }
            IconButton(onClick = {}) { Icon(Icons.Outlined.Archive, contentDescription = "Archive") }
            IconButton(onClick = {}) { Icon(Icons.Outlined.Delete, contentDescription = "Delete") }
        }
    }
""".trimIndent()

private val doubleTapLikeCode = """
    var showHeart by remember { mutableStateOf(false) }

    Box(
        Modifier.pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    showHeart = true
                    scope.launch { delay(700); showHeart = false }
                }
            )
        }
    ) {
        AnimatedVisibility(
            visible = showHeart,
            enter = scaleIn(spring(dampingRatio = 0.4f)) + fadeIn(),
            exit = scaleOut() + fadeOut(tween(400))
        ) {
            Icon(Icons.Outlined.Favorite, contentDescription = "Liked", tint = Color(0xFFE74C3C))
        }
    }
""".trimIndent()
