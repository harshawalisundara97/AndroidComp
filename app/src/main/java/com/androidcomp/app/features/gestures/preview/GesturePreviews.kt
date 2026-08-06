package com.androidcomp.app.features.gestures.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/** gesture-tap: a Box that reports which tap gesture was just detected. */
@Composable
fun TapGesturePreview() {
    var message by remember { mutableStateOf("Tap, double-tap, or long-press me") }

    Box(
        modifier = Modifier
            .size(200.dp)
            .background(Color(0xFF6C5CE7), RoundedCornerShape(24.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { message = "Tapped!" },
                    onDoubleTap = { message = "Double-tapped!" },
                    onLongPress = { message = "Long-pressed!" }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            message,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.size(160.dp).wrapContentAlign()
        )
    }
}

private fun Modifier.wrapContentAlign(): Modifier = this

/** gesture-drag: a draggable circle clamped within a bounded box. */
@Composable
fun DragGesturePreview() {
    val boxSizeDp = 200.dp
    val circleSizeDp = 48.dp
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .size(boxSizeDp)
            .background(Color(0xFFF2F2F2), RoundedCornerShape(24.dp))
    ) {
        val density = androidx.compose.ui.platform.LocalDensity.current
        val maxOffsetPx = with(density) { (boxSizeDp - circleSizeDp).toPx() }

        Box(
            modifier = Modifier
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                .size(circleSizeDp)
                .background(Color(0xFF00B4D8), CircleShape)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        val newX = (offset.x + dragAmount.x).coerceIn(0f, maxOffsetPx)
                        val newY = (offset.y + dragAmount.y).coerceIn(0f, maxOffsetPx)
                        offset = Offset(newX, newY)
                    }
                }
        )
    }
}
