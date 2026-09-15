package com.androidcomp.app.features.animations.customstyles2

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import androidx.compose.foundation.Canvas

/** 1. Morphing shape: circle to rounded square via animated corner radius. */
@Composable
fun MorphingShapeDemo(isSquare: Boolean, onToggle: () -> Unit) {
    val cornerPercent by animateFloatAsState(
        targetValue = if (isSquare) 0.16f else 0.5f,
        animationSpec = tween(500, easing = LinearOutSlowInEasing),
        label = "corner"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(percent = (cornerPercent * 100).toInt()))
                .background(MaterialTheme.colorScheme.primary)
        )
        Button(onClick = onToggle, modifier = Modifier.padding(top = 12.dp)) {
            Text(if (isSquare) "Morph to Circle" else "Morph to Square")
        }
    }
}

/** 2. Physics-based bounce: a ball drops and bounces with decaying spring. */
@Composable
fun BouncePhysicsDemo(dropCount: Int, onDrop: () -> Unit) {
    var offsetY by remember { mutableFloatStateOf(0f) }
    LaunchedEffect(dropCount) {
        if (dropCount > 0) {
            offsetY = 0f
        }
    }
    val animatedOffset by animateDpAsState(
        targetValue = if (dropCount > 0) 120.dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "bounce"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.height(140.dp), contentAlignment = Alignment.TopCenter) {
            Box(
                Modifier
                    .padding(top = animatedOffset)
                    .size(28.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.secondary)
            )
        }
        Button(onClick = onDrop) { Text("Drop Ball") }
    }
}

/** 3. Page-curl style flip transition between two cards. */
@Composable
fun PageFlipDemo(pageIndex: Int, onNext: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = (pageIndex % 2) * 180f,
        animationSpec = tween(600, easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)),
        label = "flip"
    )
    val pageLabel = if (pageIndex % 2 == 0) "Page A" else "Page B"
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
                .padding(horizontal = 24.dp)
                .clip(RoundedCornerShape(16.dp)),
            color = MaterialTheme.colorScheme.tertiaryContainer
        ) {
            Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(pageLabel, style = MaterialTheme.typography.titleMedium)
            }
        }
        Text("rotation: ${rotation.toInt()} deg", style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(top = 4.dp))
        Button(onClick = onNext, modifier = Modifier.padding(top = 8.dp)) { Text("Flip Page") }
    }
}

/** 4. Animated route/path drawing on a Canvas using an animated path progress. */
@Composable
fun AnimatedPathDrawingDemo(drawn: Boolean, onToggle: () -> Unit) {
    val progress by animateFloatAsState(
        targetValue = if (drawn) 1f else 0f,
        animationSpec = tween(1200, easing = LinearOutSlowInEasing),
        label = "path-progress"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(Modifier.fillMaxWidth().height(80.dp).padding(horizontal = 16.dp)) {
            val path = Path().apply {
                moveTo(0f, size.height)
                cubicTo(size.width * 0.25f, 0f, size.width * 0.5f, size.height, size.width, 0f)
            }
            val measure = androidx.compose.ui.graphics.PathMeasure()
            measure.setPath(path, false)
            val out = Path()
            measure.getSegment(0f, measure.length * progress, out, true)
            drawPath(out, color = Color(0xFF2E7D32), style = Stroke(width = 6f))
        }
        Button(onClick = onToggle, modifier = Modifier.padding(top = 8.dp)) {
            Text(if (drawn) "Reset Path" else "Draw Path")
        }
    }
}

/** 5. Elastic list-item insertion with spring-based scale/slide entrance. */
@Composable
fun ElasticListInsertionDemo(items: List<String>, onInsert: () -> Unit, onReset: () -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        items.forEach { item ->
            ElasticRow(item)
        }
        Row(Modifier.padding(top = 8.dp)) {
            Button(onClick = onInsert, modifier = Modifier.padding(end = 8.dp)) { Text("Insert Item") }
            Button(onClick = onReset) { Text("Reset") }
        }
    }
}

@Composable
private fun ElasticRow(label: String) {
    var visible by remember(label) { mutableStateOf(false) }
    LaunchedEffect(label) {
        delay(16)
        visible = true
    }
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.6f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "elastic-scale"
    )
    val offsetX by animateDpAsState(
        targetValue = if (visible) 0.dp else 40.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "elastic-offset"
    )
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .padding(start = offsetX)
            .scale(scale)
            .clip(RoundedCornerShape(12.dp)),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Box(Modifier.padding(12.dp)) {
            Text(label, modifier = Modifier, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
