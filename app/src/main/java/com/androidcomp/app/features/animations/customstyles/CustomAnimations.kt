package com.androidcomp.app.features.animations.customstyles

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.foundation.background
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/** 1. Staggered List Entrance — items fade/slide in with increasing per-item delay. */
@Composable
fun StaggeredListEntrance(replayKey: Int) {
    val items = listOf("Design", "Develop", "Test", "Ship", "Celebrate")

    Column(Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, label ->
            StaggeredItemRow(label = label, index = index, replayKey = replayKey)
        }
    }
}

@Composable
private fun StaggeredItemRow(label: String, index: Int, replayKey: Int) {
    val alpha = remember(replayKey) { Animatable(0f) }
    val offsetX = remember(replayKey) { Animatable(40f) }

    LaunchedEffect(replayKey) {
        delay(index * 80L)
        launch { alpha.animateTo(1f, tween(300)) }
        launch { offsetX.animateTo(0f, tween(300)) }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .graphicsLayer { this.alpha = alpha.value; translationX = offsetX.value }
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF2F2F2))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFF6C5CE7), modifier = Modifier.size(18.dp))
        Text("  $label", color = Color(0xFF2D2D2D))
    }
}

/** 2. Bounce/Spring Button Feedback — exaggerated spring overshoot on tap. */
@Composable
fun BounceSpringButton() {
    var bounceTrigger by remember { mutableStateOf(0) }
    val scale by animateFloatAsState(
        targetValue = if (bounceTrigger % 2 == 0) 1f else 1.3f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow),
        label = "bounceSpringScale"
    )
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF00B894))
            .clickable {
                bounceTrigger++
                scope.launch {
                    delay(180)
                    bounceTrigger++
                }
            }
            .padding(horizontal = 28.dp, vertical = 14.dp)
    ) {
        Text("Tap for Bounce", color = Color.White)
    }
}

/** 3. Confetti Burst — a handful of particles fly outward and fade on tap. */
private data class Particle(val angle: Float, val color: Color)

@Composable
fun ConfettiBurstButton() {
    val scope = rememberCoroutineScope()
    val particles = remember {
        List(10) { Particle(angle = it * (360f / 10), color = listOf(Color(0xFFFF6B6B), Color(0xFFFFD93D), Color(0xFF6BCB77), Color(0xFF4D96FF)).random(Random)) }
    }
    val progressAnimatables = remember { particles.map { Animatable(0f) } }

    Box(
        modifier = Modifier.size(180.dp, 120.dp),
        contentAlignment = Alignment.Center
    ) {
        particles.forEachIndexed { i, particle ->
            val progress = progressAnimatables[i].value
            val radians = Math.toRadians(particle.angle.toDouble())
            val distance = 70f * progress
            Box(
                Modifier
                    .graphicsLayer {
                        translationX = (Math.cos(radians) * distance).toFloat()
                        translationY = (Math.sin(radians) * distance).toFloat()
                        alpha = 1f - progress
                    }
                    .size(10.dp)
                    .background(particle.color, CircleShape)
            )
        }
        Button(onClick = {
            scope.launch {
                progressAnimatables.forEach { it.snapTo(0f) }
                progressAnimatables.map { anim ->
                    launch { anim.animateTo(1f, tween(700)) }
                }
            }
        }) {
            Text("Burst!")
        }
    }
}

/** 4. Shared-Element-Style Expand — card expands in place to reveal extra content. */
@Composable
fun ExpandInPlaceCard(expanded: Boolean, onExpandedChange: (Boolean) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(300))
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFEFEAFB))
            .clickable { onExpandedChange(!expanded) }
            .padding(16.dp)
    ) {
        Text("Tap to ${if (expanded) "collapse" else "expand"}", color = Color(0xFF4B3A82))
        if (expanded) {
            Text(
                "This extra detail content grows the card in place, using animateContentSize() " +
                    "to smoothly interpolate the height change instead of popping instantly.",
                color = Color(0xFF6B5B95),
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

/** 5. Loading Skeleton Shimmer — a gradient sweep animates across placeholder rows. */
@Composable
fun ShimmerSkeletonRows() {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val shimmerX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart),
        label = "shimmerX"
    )

    val brush = Brush.linearGradient(
        colors = listOf(Color(0xFFE6E6E6), Color(0xFFF6F6F6), Color(0xFFE6E6E6)),
        start = Offset(shimmerX, 0f),
        end = Offset(shimmerX + 200f, 200f)
    )

    Column(Modifier.fillMaxWidth()) {
        repeat(3) { index ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
                    .height(if (index == 1) 14.dp else 18.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )
        }
    }
}
