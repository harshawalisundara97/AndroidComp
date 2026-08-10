package com.androidcomp.app.features.sliders.customstyles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Canvas
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/** 1. Vertical Slider — draggable volume control laid out top-to-bottom, thumb glides with a spring. */
@Composable
fun VerticalVolumeSlider(value: Float, onValueChange: (Float) -> Unit) {
    val trackHeight = 180.dp
    val thumbSize = 24.dp
    var trackHeightPx by remember { mutableStateOf(1f) }

    val thumbOffset by animateDpAsState(
        targetValue = trackHeight * (1f - value) - (thumbSize / 2),
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium),
        label = "verticalVolumeThumb"
    )
    val fillFraction by animateFloatAsState(targetValue = value, label = "verticalVolumeFill")

    Box(
        modifier = Modifier
            .width(48.dp)
            .height(trackHeight)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFE4E4E7))
            .pointerInput(Unit) {
                trackHeightPx = size.height.toFloat()
                detectDragGestures { change, _ ->
                    val fraction = 1f - (change.position.y / trackHeightPx).coerceIn(0f, 1f)
                    onValueChange(fraction)
                }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(trackHeight * fillFraction)
                .background(Color(0xFF3949AB), RoundedCornerShape(24.dp))
        )
        Box(
            modifier = Modifier
                .offset(y = thumbOffset)
                .size(thumbSize)
                .background(Color.White, CircleShape)
                .border(2.dp, Color(0xFF3949AB), CircleShape)
        )
    }
}

/** 2. Range Slider with Labeled Bubble — Material3 RangeSlider with animated value bubbles over each thumb. */
@Composable
fun BubbleRangeSlider(low: Float, high: Float, onRangeChange: (Float, Float) -> Unit) {
    var isDragging by remember { mutableStateOf(false) }
    val valueRange = 0f..100f

    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val trackWidth = maxWidth
        val lowFraction = (low - valueRange.start) / (valueRange.endInclusive - valueRange.start)
        val highFraction = (high - valueRange.start) / (valueRange.endInclusive - valueRange.start)
        val lowBubbleOffset by animateDpAsState(targetValue = trackWidth * lowFraction - 14.dp, label = "lowBubble")
        val highBubbleOffset by animateDpAsState(targetValue = trackWidth * highFraction - 14.dp, label = "highBubble")

        Box(modifier = Modifier.padding(top = 36.dp)) {
            RangeSlider(
                value = low..high,
                onValueChange = {
                    isDragging = true
                    onRangeChange(it.start, it.endInclusive)
                },
                onValueChangeFinished = { isDragging = false },
                valueRange = valueRange
            )
        }

        AnimatedVisibility(
            visible = isDragging,
            modifier = Modifier.offset(x = lowBubbleOffset, y = 0.dp),
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(150))
        ) {
            ValueBubble(low.roundToInt())
        }
        AnimatedVisibility(
            visible = isDragging,
            modifier = Modifier.offset(x = highBubbleOffset, y = 0.dp),
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(150))
        ) {
            ValueBubble(high.roundToInt())
        }
    }
}

@Composable
private fun ValueBubble(value: Int) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .background(Color(0xFF3949AB), RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(value.toString(), style = MaterialTheme.typography.labelSmall, color = Color.White)
    }
}

/** 3. Stepped/Discrete Slider with Ticks — 5 visible tick marks, thumb animates a snap to the nearest one. */
@Composable
fun SteppedTickSlider(value: Float, onValueChange: (Float) -> Unit) {
    val stepCount = 4 // 5 positions: 0..4
    val thumbSize = 22.dp
    var trackWidthPx by remember { mutableStateOf(1f) }

    val animatedFraction by animateFloatAsState(
        targetValue = value / stepCount,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMedium),
        label = "steppedSnap"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
    ) {
        val trackWidth = maxWidth
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .align(Alignment.CenterStart)
                .background(Color(0xFFD0D0D0), RoundedCornerShape(2.dp))
                .pointerInput(Unit) {
                    trackWidthPx = size.width.toFloat()
                    detectHorizontalDragGestures { change, _ ->
                        val fraction = (change.position.x / trackWidthPx).coerceIn(0f, 1f)
                        val nearestStep = (fraction * stepCount).roundToInt().coerceIn(0, stepCount)
                        onValueChange(nearestStep.toFloat())
                    }
                }
        )
        for (i in 0..stepCount) {
            Box(
                modifier = Modifier
                    .offset(x = trackWidth * (i.toFloat() / stepCount) - 2.dp)
                    .align(Alignment.CenterStart)
                    .size(4.dp)
                    .background(Color(0xFF9AA0AC), CircleShape)
            )
        }
        Box(
            modifier = Modifier
                .offset(x = trackWidth * animatedFraction - (thumbSize / 2))
                .align(Alignment.CenterStart)
                .size(thumbSize)
                .background(Color(0xFF2ECC71), CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
    }
}

/** 4. Gradient Track Slider — cool-to-warm horizontal gradient track with a custom-drawn white thumb. */
@Composable
fun GradientTrackSlider(value: Float, onValueChange: (Float) -> Unit) {
    val thumbSize = 26.dp
    var trackWidthPx by remember { mutableStateOf(1f) }
    val thumbOffsetFraction by animateFloatAsState(targetValue = value, label = "gradientThumb")

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
    ) {
        val trackWidth = maxWidth
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .align(Alignment.CenterStart)
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF29B6F6), Color(0xFF66BB6A), Color(0xFFFFA726), Color(0xFFEF5350))
                    ),
                    RoundedCornerShape(6.dp)
                )
                .pointerInput(Unit) {
                    trackWidthPx = size.width.toFloat()
                    detectHorizontalDragGestures { change, _ ->
                        val fraction = (change.position.x / trackWidthPx).coerceIn(0f, 1f)
                        onValueChange(fraction)
                    }
                }
        )
        Box(
            modifier = Modifier
                .offset(x = trackWidth * thumbOffsetFraction - (thumbSize / 2))
                .align(Alignment.CenterStart)
                .size(thumbSize)
                .background(Color.White, CircleShape)
                .border(3.dp, Color(0xFF3949AB), CircleShape)
        )
    }
}

/** 5. Circular/Dial Slider — drag around a ring to change a 0-100 value, drawn as a filling arc. */
@Composable
fun CircularDialSlider(value: Float, onValueChange: (Float) -> Unit) {
    val diameter = 140.dp
    val animatedValue by animateFloatAsState(
        targetValue = value,
        animationSpec = tween(200),
        label = "dialValue"
    )

    Box(
        modifier = Modifier
            .size(diameter)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val touch = change.position
                    val angleRad = atan2(touch.y - center.y, touch.x - center.x)
                    // shift so 0 degrees is at the top (12 o'clock) and increases clockwise
                    var angleDeg = Math.toDegrees(angleRad.toDouble()).toFloat() + 90f
                    if (angleDeg < 0f) angleDeg += 360f
                    val fraction = (angleDeg / 360f).coerceIn(0f, 1f)
                    onValueChange(fraction * 100f)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(diameter)) {
            val strokeWidth = 12.dp.toPx()
            drawArc(
                color = Color(0xFFE4E4E7),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = Color(0xFF3949AB),
                startAngle = -90f,
                sweepAngle = 360f * (animatedValue / 100f),
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
            val angleRad = ((animatedValue / 100f) * 360f - 90f) * (PI / 180f).toFloat()
            val radius = (diameter.toPx() - strokeWidth) / 2f
            val knobCenter = Offset(
                x = center.x + radius * cos(angleRad),
                y = center.y + radius * sin(angleRad)
            )
            drawCircle(color = Color.White, radius = 10.dp.toPx(), center = knobCenter)
            drawCircle(
                color = Color(0xFF3949AB),
                radius = 10.dp.toPx(),
                center = knobCenter,
                style = Stroke(width = 3.dp.toPx())
            )
        }
        Text(
            text = "${animatedValue.roundToInt()}",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
