package com.androidcomp.app.features.graphics.customstyles

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.dp
import kotlin.math.sin

/** 1. Custom Drawn Progress Ring — Canvas-drawn arc that animates its sweep angle. */
@Composable
fun CustomProgressRing(percent: Float) {
    val animatedPercent by animateFloatAsState(percent, animationSpec = tween(600), label = "progressRingSweep")

    Box(modifier = Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.height(120.dp).fillMaxWidth()) {
            val strokeWidth = 14.dp.toPx()
            val diameter = size.height - strokeWidth
            val topLeft = Offset((size.width - diameter) / 2f, strokeWidth / 2f)
            drawArc(
                color = Color(0xFFEDEDED),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = androidx.compose.ui.geometry.Size(diameter, diameter),
                style = Stroke(width = strokeWidth)
            )
            drawArc(
                color = Color(0xFF2F6FED),
                startAngle = -90f,
                sweepAngle = 360f * (animatedPercent / 100f),
                useCenter = false,
                topLeft = topLeft,
                size = androidx.compose.ui.geometry.Size(diameter, diameter),
                style = Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round)
            )
        }
        Text("${animatedPercent.toInt()}%", color = Color(0xFF2D2D2D))
    }
}

/** 2. Gradient Mesh Background — layered radial gradients behind sample text. */
@Composable
fun GradientMeshBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFFFD3A5), Color(0x00FFD3A5)),
                    center = Offset(0.15f, 0.2f).let { Offset(it.x * 400f, it.y * 400f) },
                    radius = 260f
                )
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFA5C8FF), Color(0x00A5C8FF)),
                    center = Offset(320f, 220f),
                    radius = 260f
                )
            )
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFFC9A5FF), Color(0x00C9A5FF)),
                    center = Offset(180f, 40f),
                    radius = 220f
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Text("Layered Gradient Mesh", color = Color(0xFF2D2D2D))
    }
}

/** 3. Animated Wave Shape — a horizontal sine wave that continuously drifts. */
@Composable
fun AnimatedWaveShape() {
    val transition = rememberInfiniteTransition(label = "waveTransition")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing), RepeatMode.Restart),
        label = "wavePhase"
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0B1E3D))
    ) {
        val amplitude = 16f
        val waveLength = size.width / 2f
        val path = androidx.compose.ui.graphics.Path()
        path.moveTo(0f, size.height / 2f)
        var x = 0f
        while (x <= size.width) {
            val y = size.height / 2f + amplitude * sin((x / waveLength) * 2 * Math.PI + phase).toFloat()
            path.lineTo(x, y)
            x += 4f
        }
        drawPath(path, color = Color(0xFF5CE1E6), style = Stroke(width = 4f))
    }
}

/** 4. Custom Drawn Sparkline Chart — line chart that draws itself in on Redraw. */
@Composable
fun SparklineChart(redrawKey: Int) {
    val values = listOf(4f, 9f, 6f, 13f, 8f, 15f, 10f, 18f, 12f)
    var drawFraction by remember(redrawKey) { mutableFloatStateOf(0f) }

    LaunchedEffect(redrawKey) {
        androidx.compose.animation.core.Animatable(0f).animateTo(
            targetValue = 1f,
            animationSpec = tween(900)
        ) { drawFraction = value }
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF7F8FA))
            .padding(12.dp)
    ) {
        val maxVal = values.max()
        val minVal = values.min()
        val stepX = size.width / (values.size - 1)
        val points = values.mapIndexed { index, v ->
            val x = index * stepX
            val normalized = (v - minVal) / (maxVal - minVal).coerceAtLeast(1f)
            val y = size.height - normalized * size.height
            Offset(x, y)
        }
        val path = androidx.compose.ui.graphics.Path().apply {
            points.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
        }
        clipRect(right = size.width * drawFraction) {
            drawPath(path, color = Color(0xFF00B894), style = Stroke(width = 4f))
        }
    }
}

/** 5. Particle/Dot Pattern Background — static grid of scattered dots behind content. */
@Composable
fun DotPatternBackground() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF1C1C1E))
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(140.dp)) {
            val spacing = 22f
            var y = spacing / 2f
            var row = 0
            while (y < size.height) {
                var x = if (row % 2 == 0) spacing / 2f else spacing
                while (x < size.width) {
                    drawCircle(color = Color(0x33FFFFFF), radius = 2.5f, center = Offset(x, y))
                    x += spacing
                }
                y += spacing
                row++
            }
        }
        Text(
            "Dot Pattern Compositing",
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }
}
