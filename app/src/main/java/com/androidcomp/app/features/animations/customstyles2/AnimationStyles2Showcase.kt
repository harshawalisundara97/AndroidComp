package com.androidcomp.app.features.animations.customstyles2

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
fun AnimationStyles2Showcase(state: AnimationStyles2State, viewModel: AnimationStyles2ViewModel) {
    Column(Modifier.fillMaxWidth()) {
        AnimationStyleRow(
            "1. Morphing Shape",
            "Animates a shape's corner radius from circle to rounded square.",
            { MorphingShapeDemo(state.isSquare, viewModel::toggleMorph) },
            morphingShapeCode
        )
        AnimationStyleRow(
            "2. Physics-Based Bounce",
            "A spring animation simulates a ball dropping and bouncing.",
            { BouncePhysicsDemo(state.ballDropCount, viewModel::dropBall) },
            bouncePhysicsCode
        )
        AnimationStyleRow(
            "3. Page-Curl Flip Transition",
            "Rotates a card 180 degrees to simulate a page flip between two views.",
            { PageFlipDemo(state.pageIndex, viewModel::nextPage) },
            pageFlipCode
        )
        AnimationStyleRow(
            "4. Animated Route/Path Drawing",
            "A Canvas path is progressively revealed using PathMeasure and animated progress.",
            { AnimatedPathDrawingDemo(state.pathProgress, viewModel::toggleDrawPath) },
            pathDrawingCode
        )
        AnimationStyleRow(
            "5. Elastic List-Item Insertion",
            "New list items spring into place with scale and slide entrance.",
            { ElasticListInsertionDemo(state.insertedItems, viewModel::insertItem, viewModel::resetItems) },
            elasticInsertionCode
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

private val morphingShapeCode = """
    val cornerPercent by animateFloatAsState(
        targetValue = if (isSquare) 0.16f else 0.5f,
        animationSpec = tween(500, easing = LinearOutSlowInEasing)
    )
    Box(
        Modifier.size(80.dp)
            .clip(RoundedCornerShape(percent = (cornerPercent * 100).toInt()))
            .background(color)
    )
""".trimIndent()

private val bouncePhysicsCode = """
    val animatedOffset by animateDpAsState(
        targetValue = if (dropped) 120.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Box(Modifier.padding(top = animatedOffset).size(28.dp).clip(CircleShape).background(color))
""".trimIndent()

private val pageFlipCode = """
    val rotation by animateFloatAsState(
        targetValue = (pageIndex % 2) * 180f,
        animationSpec = tween(600, easing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f))
    )
    Surface(Modifier.height(90.dp)) { Text(if (pageIndex % 2 == 0) "Page A" else "Page B") }
""".trimIndent()

private val pathDrawingCode = """
    val progress by animateFloatAsState(targetValue = if (drawn) 1f else 0f, animationSpec = tween(1200))
    Canvas(Modifier.fillMaxWidth().height(80.dp)) {
        val path = Path().apply { cubicTo(...) }
        val measure = PathMeasure().apply { setPath(path, false) }
        val out = Path()
        measure.getSegment(0f, measure.length * progress, out, true)
        drawPath(out, color = accentColor, style = Stroke(width = 6f))
    }
""".trimIndent()

private val elasticInsertionCode = """
    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.6f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )
    val offsetX by animateDpAsState(targetValue = if (visible) 0.dp else 40.dp)
    Surface(Modifier.padding(start = offsetX).scale(scale)) { Text(label) }
""".trimIndent()
