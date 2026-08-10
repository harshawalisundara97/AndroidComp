package com.androidcomp.app.features.progress.customstyles

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.sin

private val trackColor = Color(0xFFEDEEF2)
private val activeColor = Color(0xFF2F6FED)

/** 1. Dotted Step Progress — Next/Back advance which dots are filled. */
@Composable
fun DottedStepProgress(step: Int, totalSteps: Int, onNext: () -> Unit, onBack: () -> Unit) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            repeat(totalSteps) { index ->
                val filled = index < step
                val color by animateColorAsState(if (filled) activeColor else trackColor, label = "dottedStepColor$index")
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }
        Row(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onBack, enabled = step > 0) { Text("Back") }
            Button(onClick = onNext, enabled = step < totalSteps) { Text("Next") }
        }
    }
}

/** 2. Circular Percentage Ring with Center Label — Canvas ring, animated center number. */
@Composable
fun CircularPercentageRing(percent: Int, onIncrease: () -> Unit) {
    val animatedPercent by animateIntAsState(percent, label = "circularRingPercent")
    val sweep by animateFloatAsState(percent / 100f, label = "circularRingSweep")

    Column(horizontalAlignment = Alignment.Start) {
        Box(modifier = Modifier.size(120.dp), contentAlignment = Alignment.Center) {
            Canvas(modifier = Modifier.size(120.dp)) {
                val stroke = 12.dp.toPx()
                drawArc(
                    color = trackColor,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
                drawArc(
                    color = activeColor,
                    startAngle = -90f,
                    sweepAngle = 360f * sweep,
                    useCenter = false,
                    style = Stroke(width = stroke, cap = StrokeCap.Round)
                )
            }
            Text("$animatedPercent%", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        }
        Button(onClick = onIncrease, modifier = Modifier.padding(top = 16.dp)) { Text("+10%") }
    }
}

/** 3. Skeleton Shimmer Loading — gray placeholder bars with a moving shimmer sweep. */
@Composable
fun SkeletonShimmerLoading() {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val shimmerX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "shimmerX"
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFE4E6EC), Color(0xFFF6F7FA), Color(0xFFE4E6EC)),
        start = Offset(shimmerX, 0f),
        end = Offset(shimmerX + 200f, 200f)
    )

    Column {
        repeat(3) { index ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(shimmerBrush)
                )
                Column(modifier = Modifier.padding(start = 12.dp)) {
                    Box(
                        modifier = Modifier
                            .width(if (index == 1) 140.dp else 180.dp)
                            .height(12.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(shimmerBrush)
                    )
                    Box(
                        modifier = Modifier
                            .padding(top = 6.dp)
                            .width(100.dp)
                            .height(10.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(shimmerBrush)
                    )
                }
            }
        }
    }
}

/** 4. Segmented Multi-Step Bar — a bar divided into equal segments, filled left-to-right. */
@Composable
fun SegmentedMultiStepBar(step: Int, totalSteps: Int, onAdvance: () -> Unit, onReset: () -> Unit) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
            repeat(totalSteps) { index ->
                val filled = index < step
                val color by animateColorAsState(if (filled) activeColor else trackColor, label = "segmentedStepColor$index")
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color)
                )
            }
        }
        Row(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onReset, enabled = step > 0) { Text("Reset") }
            Button(onClick = onAdvance, enabled = step < totalSteps) { Text("Complete Step") }
        }
    }
}

/** 5. Animated Wave/Liquid Progress — a rounded container with a rising, drifting sine-wave fill. */
@Composable
fun AnimatedWaveProgress(percent: Int, onIncrease: () -> Unit) {
    val fillLevel by animateFloatAsState(percent / 100f, label = "waveFillLevel")
    val transition = rememberInfiniteTransition(label = "waveDriftTransition")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing), repeatMode = RepeatMode.Restart),
        label = "wavePhase"
    )

    Column(horizontalAlignment = Alignment.Start) {
        Box(
            modifier = Modifier
                .size(140.dp, 140.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color(0xFFEDEEF2))
        ) {
            Canvas(modifier = Modifier.size(140.dp, 140.dp)) {
                val waveHeight = 8.dp.toPx()
                val baseY = size.height * (1f - fillLevel)
                val path = Path().apply {
                    moveTo(0f, size.height)
                    lineTo(0f, baseY)
                    var x = 0f
                    while (x <= size.width) {
                        val y = baseY + sin((x / size.width) * 4 * Math.PI + phase).toFloat() * waveHeight
                        lineTo(x, y)
                        x += 4f
                    }
                    lineTo(size.width, size.height)
                    close()
                }
                drawPath(path, color = activeColor)
            }
            Text(
                "$percent%",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Button(onClick = onIncrease, modifier = Modifier.padding(top = 16.dp)) { Text("+10%") }
    }
}
