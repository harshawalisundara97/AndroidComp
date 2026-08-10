package com.androidcomp.app.features.animations.customstyles

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
fun AnimationStylesShowcase(
    staggerReplayKey: Int,
    onReplayStagger: () -> Unit,
    expandCardExpanded: Boolean,
    onExpandCardExpandedChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        AnimationStyleRow(
            "1. Staggered List Entrance",
            "Tap Replay to see each row fade and slide in with an increasing per-item delay.",
            {
                Column {
                    androidx.compose.material3.Button(onClick = onReplayStagger) { Text("Replay") }
                    androidx.compose.foundation.layout.Spacer(Modifier.padding(top = 10.dp))
                    StaggeredListEntrance(replayKey = staggerReplayKey)
                }
            },
            staggeredListCode
        )
        AnimationStyleRow(
            "2. Bounce/Spring Button Feedback",
            "An exaggerated spring overshoot scales the button up and settles back on tap.",
            { BounceSpringButton() },
            bounceSpringCode
        )
        AnimationStyleRow(
            "3. Confetti Burst",
            "Tapping the button spawns colored particles that fly outward and fade.",
            { ConfettiBurstButton() },
            confettiBurstCode
        )
        AnimationStyleRow(
            "4. Shared-Element-Style Expand",
            "Tap the card to smoothly grow it in place and reveal extra detail content.",
            { ExpandInPlaceCard(expanded = expandCardExpanded, onExpandedChange = onExpandCardExpandedChange) },
            expandInPlaceCode
        )
        AnimationStyleRow(
            "5. Loading Skeleton Shimmer",
            "A gradient sweeps continuously across placeholder rows using an infinite transition.",
            { ShimmerSkeletonRows() },
            shimmerSkeletonCode
        )
    }
}

@Composable
private fun AnimationStyleRow(
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

private val staggeredListCode = """
    val alpha = remember(replayKey) { Animatable(0f) }
    val offsetX = remember(replayKey) { Animatable(40f) }

    LaunchedEffect(replayKey) {
        delay(index * 80L)
        launch { alpha.animateTo(1f, tween(300)) }
        launch { offsetX.animateTo(0f, tween(300)) }
    }

    Row(
        Modifier.graphicsLayer { this.alpha = alpha.value; translationX = offsetX.value }
    ) {
        Text(label)
    }
""".trimIndent()

private val bounceSpringCode = """
    val scale by animateFloatAsState(
        targetValue = if (bounceTrigger % 2 == 0) 1f else 1.3f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable { bounceTrigger++; scope.launch { delay(180); bounceTrigger++ } }
    ) {
        Text("Tap for Bounce")
    }
""".trimIndent()

private val confettiBurstCode = """
    val progressAnimatables = remember { particles.map { Animatable(0f) } }

    particles.forEachIndexed { i, particle ->
        val progress = progressAnimatables[i].value
        val radians = Math.toRadians(particle.angle.toDouble())
        Box(
            Modifier.graphicsLayer {
                translationX = (cos(radians) * 70f * progress).toFloat()
                translationY = (sin(radians) * 70f * progress).toFloat()
                alpha = 1f - progress
            }
        )
    }

    Button(onClick = {
        scope.launch {
            progressAnimatables.forEach { it.snapTo(0f) }
            progressAnimatables.forEach { anim -> launch { anim.animateTo(1f, tween(700)) } }
        }
    }) { Text("Burst!") }
""".trimIndent()

private val expandInPlaceCode = """
    Column(
        Modifier
            .animateContentSize(tween(300))
            .clickable { expanded = !expanded }
    ) {
        Text("Tap to ${'$'}{if (expanded) "collapse" else "expand"}")
        if (expanded) {
            Text("Extra detail content that grows the card in place.")
        }
    }
""".trimIndent()

private val shimmerSkeletonCode = """
    val transition = rememberInfiniteTransition()
    val shimmerX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing), RepeatMode.Restart)
    )

    val brush = Brush.linearGradient(
        colors = listOf(Color(0xFFE6E6E6), Color(0xFFF6F6F6), Color(0xFFE6E6E6)),
        start = Offset(shimmerX, 0f),
        end = Offset(shimmerX + 200f, 200f)
    )

    Box(Modifier.background(brush))
""".trimIndent()
