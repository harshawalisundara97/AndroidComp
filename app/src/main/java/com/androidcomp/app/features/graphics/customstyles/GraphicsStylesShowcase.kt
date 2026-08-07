package com.androidcomp.app.features.graphics.customstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun GraphicsStylesShowcase(
    progressRingPercent: Float,
    onIncreaseProgressRing: () -> Unit,
    sparklineRedrawKey: Int,
    onRedrawSparkline: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        GraphicsStyleRow(
            "1. Custom Drawn Progress Ring",
            "A Canvas-drawn arc animates its sweep angle each time you add 20%.",
            {
                Column {
                    Button(onClick = onIncreaseProgressRing) { Text("+20%") }
                    CustomProgressRing(percent = progressRingPercent)
                }
            },
            progressRingCode
        )
        GraphicsStyleRow(
            "2. Gradient Mesh Background",
            "Overlapping radial gradients composited together create a soft mesh backdrop.",
            { GradientMeshBackground() },
            gradientMeshCode
        )
        GraphicsStyleRow(
            "3. Animated Wave Shape",
            "A sine-wave path continuously drifts using an infinite transition, like a decorative divider.",
            { AnimatedWaveShape() },
            animatedWaveCode
        )
        GraphicsStyleRow(
            "4. Custom Drawn Sparkline Chart",
            "Tap Redraw to watch the line chart draw itself in from a hardcoded set of values.",
            {
                Column {
                    Button(onClick = onRedrawSparkline) { Text("Redraw") }
                    SparklineChart(redrawKey = sparklineRedrawKey)
                }
            },
            sparklineCode
        )
        GraphicsStyleRow(
            "5. Particle/Dot Pattern Background",
            "A static offset grid of small dots drawn behind content, a pure compositing technique.",
            { DotPatternBackground() },
            dotPatternCode
        )
    }
}

@Composable
private fun GraphicsStyleRow(
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

private val progressRingCode = """
    val animatedPercent by animateFloatAsState(percent, tween(600))

    Canvas(Modifier.height(120.dp).fillMaxWidth()) {
        val strokeWidth = 14.dp.toPx()
        drawArc(
            color = Color(0xFFEDEDED),
            startAngle = -90f, sweepAngle = 360f, useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        drawArc(
            color = Color(0xFF2F6FED),
            startAngle = -90f, sweepAngle = 360f * (animatedPercent / 100f), useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
""".trimIndent()

private val gradientMeshCode = """
    Box(
        Modifier
            .background(Brush.radialGradient(listOf(Color(0xFFFFD3A5), Color(0x00FFD3A5)), center = Offset(60f, 80f), radius = 260f))
            .background(Brush.radialGradient(listOf(Color(0xFFA5C8FF), Color(0x00A5C8FF)), center = Offset(320f, 220f), radius = 260f))
            .background(Brush.radialGradient(listOf(Color(0xFFC9A5FF), Color(0x00C9A5FF)), center = Offset(180f, 40f), radius = 220f))
    ) {
        Text("Layered Gradient Mesh")
    }
""".trimIndent()

private val animatedWaveCode = """
    val transition = rememberInfiniteTransition()
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(2200, easing = LinearEasing))
    )

    Canvas(Modifier.fillMaxWidth().height(80.dp)) {
        val path = Path()
        path.moveTo(0f, size.height / 2f)
        var x = 0f
        while (x <= size.width) {
            val y = size.height / 2f + 16f * sin((x / (size.width / 2f)) * 2 * Math.PI + phase).toFloat()
            path.lineTo(x, y)
            x += 4f
        }
        drawPath(path, color = Color(0xFF5CE1E6), style = Stroke(width = 4f))
    }
""".trimIndent()

private val sparklineCode = """
    var drawFraction by remember(redrawKey) { mutableFloatStateOf(0f) }

    LaunchedEffect(redrawKey) {
        Animatable(0f).animateTo(1f, tween(900)) { drawFraction = value }
    }

    Canvas(Modifier.fillMaxWidth().height(100.dp)) {
        val path = Path().apply {
            points.forEachIndexed { i, p -> if (i == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
        }
        clipRect(right = size.width * drawFraction) {
            drawPath(path, color = Color(0xFF00B894), style = Stroke(width = 4f))
        }
    }
""".trimIndent()

private val dotPatternCode = """
    Canvas(Modifier.fillMaxWidth().height(140.dp)) {
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
""".trimIndent()
