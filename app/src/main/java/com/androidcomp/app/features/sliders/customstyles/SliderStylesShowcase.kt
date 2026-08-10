package com.androidcomp.app.features.sliders.customstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun SliderStylesShowcase(
    state: SliderStylesState,
    viewModel: SliderStylesViewModel
) {
    Column(Modifier.fillMaxWidth()) {
        SliderStyleRow(
            "1. Vertical Slider",
            "A top-to-bottom volume control that fills upward and glides with a spring.",
            { VerticalVolumeSlider(state.verticalVolume, viewModel::setVerticalVolume) },
            verticalVolumeCode
        )
        SliderStyleRow(
            "2. Range Slider with Labeled Bubble",
            "Two-thumb range slider that pops an animated value bubble above each thumb while dragging.",
            {
                BubbleRangeSlider(state.rangeLow, state.rangeHigh) { low, high ->
                    viewModel.setRange(low, high)
                }
            },
            bubbleRangeCode
        )
        SliderStyleRow(
            "3. Stepped/Discrete Slider with Ticks",
            "Five visible tick marks; the thumb snaps to the nearest one with a bouncy animation.",
            { SteppedTickSlider(state.steppedValue, viewModel::setSteppedValue) },
            steppedTickCode
        )
        SliderStyleRow(
            "4. Gradient Track Slider",
            "Cool-to-warm gradient track with a custom-drawn white thumb.",
            { GradientTrackSlider(state.gradientValue, viewModel::setGradientValue) },
            gradientTrackCode
        )
        SliderStyleRow(
            "5. Circular/Dial Slider",
            "Drag around a ring to change a 0-100 value; the arc fills proportionally.",
            { CircularDialSlider(state.dialValue, viewModel::setDialValue) },
            circularDialCode
        )
    }
}

@Composable
private fun SliderStyleRow(
    title: String,
    description: String,
    slider: @Composable () -> Unit,
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
        Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            slider()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val verticalVolumeCode = """
    val thumbOffset by animateDpAsState(
        targetValue = trackHeight * (1f - value) - (thumbSize / 2),
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy)
    )
    Box(
        Modifier
            .width(48.dp)
            .height(trackHeight)
            .background(Color(0xFFE4E4E7), RoundedCornerShape(24.dp))
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val fraction = 1f - (change.position.y / size.height).coerceIn(0f, 1f)
                    onValueChange(fraction)
                }
            }
    ) {
        Box(Modifier.offset(y = thumbOffset).size(24.dp).background(Color.White, CircleShape))
    }
""".trimIndent()

private val bubbleRangeCode = """
    RangeSlider(
        value = low..high,
        onValueChange = { onRangeChange(it.start, it.endInclusive) },
        onValueChangeFinished = { isDragging = false },
        valueRange = 0f..100f
    )

    AnimatedVisibility(visible = isDragging, enter = fadeIn(), exit = fadeOut()) {
        Box(Modifier.size(28.dp).background(Color(0xFF3949AB), RoundedCornerShape(8.dp))) {
            Text(low.roundToInt().toString(), color = Color.White)
        }
    }
""".trimIndent()

private val steppedTickCode = """
    val animatedFraction by animateFloatAsState(
        targetValue = value / stepCount,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
    )
    Box(
        Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color(0xFFD0D0D0), RoundedCornerShape(2.dp))
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    val fraction = (change.position.x / size.width).coerceIn(0f, 1f)
                    onValueChange((fraction * stepCount).roundToInt().toFloat())
                }
            }
    )
    // tick marks drawn at i / stepCount fractions along the track
    Box(Modifier.offset(x = trackWidth * animatedFraction - 11.dp).size(22.dp).background(Color(0xFF2ECC71), CircleShape))
""".trimIndent()

private val gradientTrackCode = """
    Box(
        Modifier
            .fillMaxWidth()
            .height(10.dp)
            .background(
                Brush.horizontalGradient(
                    listOf(Color(0xFF29B6F6), Color(0xFF66BB6A), Color(0xFFFFA726), Color(0xFFEF5350))
                ),
                RoundedCornerShape(6.dp)
            )
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    onValueChange((change.position.x / size.width).coerceIn(0f, 1f))
                }
            }
    )
    Box(Modifier.offset(x = trackWidth * value - 13.dp).size(26.dp).background(Color.White, CircleShape))
""".trimIndent()

private val circularDialCode = """
    Box(
        Modifier
            .size(140.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val angleRad = atan2(change.position.y - center.y, change.position.x - center.x)
                    var angleDeg = Math.toDegrees(angleRad.toDouble()).toFloat() + 90f
                    if (angleDeg < 0f) angleDeg += 360f
                    onValueChange((angleDeg / 360f) * 100f)
                }
            }
    ) {
        Canvas(Modifier.size(140.dp)) {
            drawArc(trackColor, -90f, 360f, useCenter = false, style = Stroke(12.dp.toPx()))
            drawArc(activeColor, -90f, 360f * (value / 100f), useCenter = false, style = Stroke(12.dp.toPx()))
        }
        Text("${'$'}{value.roundToInt()}")
    }
""".trimIndent()
