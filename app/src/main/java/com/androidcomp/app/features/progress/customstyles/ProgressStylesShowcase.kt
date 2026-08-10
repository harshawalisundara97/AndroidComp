package com.androidcomp.app.features.progress.customstyles

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
fun ProgressStylesShowcase(
    state: ProgressStylesState,
    onDottedStepNext: () -> Unit,
    onDottedStepBack: () -> Unit,
    onCircularIncrease: () -> Unit,
    onSegmentedAdvance: () -> Unit,
    onSegmentedReset: () -> Unit,
    onWaveIncrease: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        ProgressStyleRow(
            "1. Dotted Step Progress",
            "Tap Next/Back to advance which dots are filled, with an animated color transition.",
            { DottedStepProgress(step = state.dottedStep, totalSteps = 5, onNext = onDottedStepNext, onBack = onDottedStepBack) },
            dottedStepProgressCode
        )
        ProgressStyleRow(
            "2. Circular Percentage Ring",
            "A Canvas-drawn ring with the percentage animated in the center; tap +10% to advance.",
            { CircularPercentageRing(percent = state.circularPercent, onIncrease = onCircularIncrease) },
            circularPercentageRingCode
        )
        ProgressStyleRow(
            "3. Skeleton Shimmer Loading",
            "Gray placeholder bars with an infinite shimmer sweep, simulating a loading list.",
            { SkeletonShimmerLoading() },
            skeletonShimmerLoadingCode
        )
        ProgressStyleRow(
            "4. Segmented Multi-Step Bar",
            "A bar split into equal segments, filled left-to-right as each step completes.",
            { SegmentedMultiStepBar(step = state.segmentedStep, totalSteps = 5, onAdvance = onSegmentedAdvance, onReset = onSegmentedReset) },
            segmentedMultiStepBarCode
        )
        ProgressStyleRow(
            "5. Animated Wave/Liquid Progress",
            "A rounded container where a wavy 'liquid' fill rises and drifts to represent progress.",
            { AnimatedWaveProgress(percent = state.wavePercent, onIncrease = onWaveIncrease) },
            animatedWaveProgressCode
        )
    }
}

@Composable
private fun ProgressStyleRow(
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

private val dottedStepProgressCode = """
    repeat(totalSteps) { index ->
        val filled = index < step
        val color by animateColorAsState(if (filled) activeColor else trackColor)
        Box(Modifier.size(14.dp).clip(CircleShape).background(color))
    }
    Row {
        OutlinedButton(onClick = onBack, enabled = step > 0) { Text("Back") }
        Button(onClick = onNext, enabled = step < totalSteps) { Text("Next") }
    }
""".trimIndent()

private val circularPercentageRingCode = """
    val animatedPercent by animateIntAsState(percent)
    val sweep by animateFloatAsState(percent / 100f)

    Box(contentAlignment = Alignment.Center) {
        Canvas(Modifier.size(120.dp)) {
            drawArc(trackColor, -90f, 360f, useCenter = false, style = Stroke(12.dp.toPx(), cap = StrokeCap.Round))
            drawArc(activeColor, -90f, 360f * sweep, useCenter = false, style = Stroke(12.dp.toPx(), cap = StrokeCap.Round))
        }
        Text("${'$'}animatedPercent%")
    }
    Button(onClick = onIncrease) { Text("+10%") }
""".trimIndent()

private val skeletonShimmerLoadingCode = """
    val transition = rememberInfiniteTransition()
    val shimmerX by transition.animateFloat(
        initialValue = -300f,
        targetValue = 300f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing))
    )
    val shimmerBrush = Brush.linearGradient(
        colors = listOf(Color(0xFFE4E6EC), Color(0xFFF6F7FA), Color(0xFFE4E6EC)),
        start = Offset(shimmerX, 0f),
        end = Offset(shimmerX + 200f, 200f)
    )
    Box(Modifier.size(44.dp).clip(CircleShape).background(shimmerBrush))
""".trimIndent()

private val segmentedMultiStepBarCode = """
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        repeat(totalSteps) { index ->
            val filled = index < step
            val color by animateColorAsState(if (filled) activeColor else trackColor)
            Box(Modifier.weight(1f).height(8.dp).clip(RoundedCornerShape(4.dp)).background(color))
        }
    }
    Button(onClick = onAdvance, enabled = step < totalSteps) { Text("Complete Step") }
""".trimIndent()

private val animatedWaveProgressCode = """
    val fillLevel by animateFloatAsState(percent / 100f)
    val transition = rememberInfiniteTransition()
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(2500, easing = LinearEasing))
    )

    Canvas(Modifier.size(140.dp)) {
        val baseY = size.height * (1f - fillLevel)
        val path = Path().apply {
            moveTo(0f, size.height)
            lineTo(0f, baseY)
            var x = 0f
            while (x <= size.width) {
                val y = baseY + sin((x / size.width) * 4 * Math.PI + phase).toFloat() * 8.dp.toPx()
                lineTo(x, y)
                x += 4f
            }
            lineTo(size.width, size.height)
            close()
        }
        drawPath(path, color = activeColor)
    }
""".trimIndent()
