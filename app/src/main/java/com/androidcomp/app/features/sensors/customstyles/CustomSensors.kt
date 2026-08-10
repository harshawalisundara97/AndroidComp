package com.androidcomp.app.features.sensors.customstyles

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DirectionsWalk
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.NearMe
import androidx.compose.material.icons.outlined.NightsStay
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.SensorsOff
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope

/** 1. Live Accelerometer Gauge — needle animates to simulated readings on a timer. */
@Composable
fun AccelerometerGauge(angle: Float) {
    val animatedAngle by animateFloatAsState(
        targetValue = angle,
        animationSpec = tween(700),
        label = "accelAngle"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color(0xFFF6F6F8), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(100.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension / 2f
                drawCircle(Color(0xFFE4E4E7), radius, center, style = Stroke(4f))
                val radians = Math.toRadians(animatedAngle.toDouble())
                val end = Offset(
                    center.x + radius * cos(radians).toFloat(),
                    center.y + radius * sin(radians).toFloat()
                )
                drawLine(Color(0xFF3949AB), center, end, strokeWidth = 6f)
                drawCircle(Color(0xFF3949AB), 6f, center)
            }
        }
        Text(
            "Simulated demo data",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/** 2. Compass Heading Indicator — needle rotates smoothly to a new simulated heading. */
@Composable
fun CompassHeadingIndicator(heading: Float, onSimulate: () -> Unit) {
    val animatedHeading by animateFloatAsState(
        targetValue = heading,
        animationSpec = tween(800),
        label = "compassHeading"
    )
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(110.dp)
                .background(Color(0xFFFFFFFF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(96.dp)) {
                val center = Offset(size.width / 2f, size.height / 2f)
                val radius = size.minDimension / 2f
                drawCircle(Color(0xFFE4E4E7), radius, center, style = Stroke(3f))
                listOf("N" to -90f, "E" to 0f, "S" to 90f, "W" to 180f).forEach { (label, deg) ->
                    val rad = Math.toRadians(deg.toDouble())
                    val pos = Offset(
                        center.x + (radius - 14f) * cos(rad).toFloat(),
                        center.y + (radius - 14f) * sin(rad).toFloat()
                    )
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        pos.x,
                        pos.y,
                        android.graphics.Paint().apply {
                            textSize = 24f
                            color = android.graphics.Color.DKGRAY
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }
            Icon(
                Icons.Outlined.Explore,
                contentDescription = null,
                tint = Color(0xFFE74C3C),
                modifier = Modifier
                    .size(48.dp)
                    .graphicsLayer { rotationZ = animatedHeading }
            )
        }
        Button(onClick = onSimulate, modifier = Modifier.padding(top = 8.dp)) {
            Text("Simulate Reading")
        }
    }
}

/** 3. Step Counter Card — animated count-up plus a progress ring toward a daily goal. */
@Composable
fun StepCounterCard(stepCount: Int, stepGoal: Int, onSimulateStep: () -> Unit) {
    var displayedCount by remember { mutableIntStateOf(stepCount) }
    val scope = rememberCoroutineScope()
    LaunchedEffect(stepCount) {
        val start = displayedCount
        val steps = 12
        repeat(steps) { i ->
            delay(16)
            displayedCount = start + ((stepCount - start) * (i + 1) / steps)
        }
        displayedCount = stepCount
    }
    val progress by animateFloatAsState(
        targetValue = (stepCount.toFloat() / stepGoal).coerceIn(0f, 1f),
        animationSpec = tween(500),
        label = "stepProgress"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(56.dp),
                color = Color(0xFF2ECC71),
                trackColor = Color(0xFFE4E4E7)
            )
            Icon(Icons.Outlined.DirectionsWalk, contentDescription = null, tint = Color(0xFF2ECC71))
        }
        Column(modifier = Modifier.weight(1f).padding(horizontal = 16.dp)) {
            Text("$displayedCount steps", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("Goal: $stepGoal", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Button(onClick = onSimulateStep) { Text("Simulate Step") }
    }
}

/** 4. Light Sensor Brightness Indicator — fill bar animating between dim and bright levels. */
@Composable
fun LightSensorIndicator(brightness: Float, onSimulate: () -> Unit) {
    val animatedBrightness by animateFloatAsState(
        targetValue = brightness,
        animationSpec = tween(600),
        label = "brightness"
    )
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (brightness > 0.5f) Icons.Outlined.LightMode else Icons.Outlined.NightsStay,
                contentDescription = null,
                tint = Color(0xFFF5A623)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
                    .height(10.dp)
                    .background(Color(0xFFE4E4E7), RoundedCornerShape(50))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedBrightness.coerceIn(0f, 1f))
                        .height(10.dp)
                        .background(Color(0xFFF5A623), RoundedCornerShape(50))
                )
            }
            Text("${(brightness * 100).toInt()}%", style = MaterialTheme.typography.bodySmall)
        }
        Button(onClick = onSimulate, modifier = Modifier.padding(top = 8.dp)) {
            Text("Simulate Reading")
        }
    }
}

/** 5. Proximity Status Card — Near/Far status crossfading between icon states. */
@Composable
fun ProximityStatusCard(isNear: Boolean, onToggle: () -> Unit) {
    val bgColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isNear) Color(0xFFE8F8EF) else Color(0xFFEAF2FF),
        label = "proximityBg"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(24.dp))
            .clickable { onToggle() }
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Crossfade(targetState = isNear, label = "proximityIcon") { near ->
            Icon(
                if (near) Icons.Outlined.Sensors else Icons.Outlined.SensorsOff,
                contentDescription = null,
                tint = if (near) Color(0xFF2ECC71) else Color(0xFF4A90D9)
            )
        }
        Column(modifier = Modifier.padding(start = 16.dp)) {
            Crossfade(targetState = isNear, label = "proximityLabel") { near ->
                Text(if (near) "Near" else "Far", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            }
            Text("Tap to toggle simulated proximity", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
