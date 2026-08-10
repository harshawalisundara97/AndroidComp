package com.androidcomp.app.features.images.customstyles

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
fun CustomImagesShowcase(
    state: CustomImagesState,
    onToggleZoom: () -> Unit,
    onCycleAvatarStatus: () -> Unit,
    onComparisonFractionChange: (Float) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        ImageStyleRow(
            "1. Shimmer Loading Placeholder",
            "An animated gradient sweep simulating an image loading state.",
            { ShimmerLoadingPlaceholder() },
            shimmerCode
        )
        ImageStyleRow(
            "2. Zoom on Tap",
            "Tap the placeholder to scale it up; tap again to reset.",
            { ZoomOnTapImage(zoomed = state.zoomed, onToggle = onToggleZoom) },
            zoomOnTapCode
        )
        ImageStyleRow(
            "3. Avatar with Online Status Dot",
            "Tap the avatar to cycle its status dot through online, away, and offline.",
            { AvatarWithStatusDot(status = state.avatarStatus, onCycleStatus = onCycleAvatarStatus) },
            avatarStatusCode
        )
        ImageStyleRow(
            "4. Gradient Scrim + Caption Overlay",
            "A bottom gradient scrim with a caption text composited over an image placeholder.",
            { GradientScrimCaptionImage() },
            scrimCode
        )
        ImageStyleRow(
            "5. Before/After Comparison Slider",
            "Drag horizontally to reveal more of the \"before\" or \"after\" placeholder.",
            { BeforeAfterComparisonSlider(fraction = state.comparisonFraction, onFractionChange = onComparisonFractionChange) },
            comparisonSliderCode
        )
    }
}

@Composable
private fun ImageStyleRow(
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
        Column(Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val shimmerCode = """
    val transition = rememberInfiniteTransition()
    val shimmerOffset by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing))
    )

    Box(
        Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFE9EAEE), Color(0xFFF6F7F9), Color(0xFFE9EAEE)),
                    start = Offset(shimmerOffset * 300f, 0f),
                    end = Offset(shimmerOffset * 300f + 300f, 300f)
                )
            )
    )
""".trimIndent()

private val zoomOnTapCode = """
    var zoomed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (zoomed) 1.3f else 1f)

    Box(Modifier.clickable { zoomed = !zoomed }) {
        Icon(
            Icons.Outlined.Image,
            contentDescription = null,
            modifier = Modifier.graphicsLayer { scaleX = scale; scaleY = scale }
        )
    }
""".trimIndent()

private val avatarStatusCode = """
    var status by remember { mutableStateOf(AvatarStatus.ONLINE) }
    val statusColor by animateColorAsState(
        when (status) {
            AvatarStatus.ONLINE -> Color(0xFF2ECC71)
            AvatarStatus.AWAY -> Color(0xFFF5A623)
            AvatarStatus.OFFLINE -> Color(0xFF9CA3AF)
        }
    )

    Box(Modifier.size(64.dp).clickable { status = status.next() }) {
        Box(Modifier.clip(CircleShape).background(Color(0xFFD8DCE3)))
        Box(
            Modifier.align(Alignment.BottomEnd).size(16.dp).clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface).padding(2.dp)
                .clip(CircleShape).background(statusColor)
        )
    }
""".trimIndent()

private val scrimCode = """
    Box(Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xFF3A3D46))) {
        Box(
            Modifier.fillMaxSize().background(
                Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f)))
            )
        )
        Text(
            "Golden Hour, Mount Fuji",
            color = Color.White,
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
        )
    }
""".trimIndent()

private val comparisonSliderCode = """
    var fraction by remember { mutableFloatStateOf(0.5f) }

    BoxWithConstraints(Modifier.clip(RoundedCornerShape(20.dp))) {
        val widthPx = with(LocalDensity.current) { maxWidth.toPx() }

        Box(Modifier.fillMaxSize().background(Color(0xFF11998E)))          // "After"
        Box(Modifier.fillMaxHeight().fillMaxWidth(fraction).background(Color(0xFFFF6B6B)))  // "Before"

        Box(
            Modifier
                .offset { IntOffset((widthPx * fraction).toInt() - 4, 0) }
                .fillMaxHeight().width(4.dp).background(Color.White)
        )

        Box(
            Modifier.fillMaxSize().pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    change.consume()
                    fraction = (change.position.x / widthPx).coerceIn(0f, 1f)
                }
            }
        )
    }
""".trimIndent()
