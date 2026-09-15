package com.androidcomp.app.features.images.customstyles2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun ImageStyles2Showcase(state: ImageStyles2State, viewModel: ImageStyles2ViewModel) {
    Column(Modifier.fillMaxWidth()) {
        ImageStyleRow(
            "1. Ken Burns Zoom-Pan",
            "A slow continuous scale+translate loop simulating a Ken Burns camera move.",
            {
                Button(onClick = viewModel::toggleKenBurns) { Text(if (state.kenBurnsPlaying) "Pause" else "Play") }
                KenBurnsImageDemo(state.kenBurnsPlaying)
            },
            kenBurnsCode
        )
        ImageStyleRow(
            "2. Color Filter Picker",
            "Cycles a tint overlay across the image to simulate filter presets.",
            {
                Button(onClick = viewModel::nextFilter) { Text("Next Filter") }
                ColorFilterPickerImageDemo(state.filterIndex)
            },
            colorFilterCode
        )
        ImageStyleRow(
            "3. Masonry Tap-to-Expand Grid",
            "Varied-height thumbnails that grow inline when tapped.",
            {
                MasonryTapToExpandGridDemo(state.expandedThumbnail, viewModel::expandThumbnail)
            },
            masonryCode
        )
        ImageStyleRow(
            "4. Carousel with Page Dots",
            "Swaps a featured image and animates the active indicator dot's width.",
            {
                ImageCarouselDotsDemo(state.carouselPage, viewModel::setCarouselPage)
            },
            carouselCode
        )
        ImageStyleRow(
            "5. Blur-to-Sharp Reveal",
            "Animates a blur radius from a heavy placeholder blur down to zero.",
            {
                Button(onClick = viewModel::toggleBlurReveal) { Text(if (state.blurRevealed) "Reset" else "Load") }
                BlurToSharpRevealDemo(state.blurRevealed)
            },
            blurRevealCode
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
        Row(Modifier.padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val kenBurnsCode = """
    val transition = rememberInfiniteTransition(label = "kenBurns")
    val scale by transition.animateFloat(
        1f, 1.18f,
        infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse)
    )
    Box(
        Modifier.graphicsLayer { scaleX = scale; scaleY = scale }
            .background(image)
    )
""".trimIndent()

private val colorFilterCode = """
    val filters = listOf("Original" to Color.Transparent, "Grayscale" to Color(0x99000000), ...)
    val (label, overlay) = filters[filterIndex % filters.size]
    Box {
        Image(painter = painter, contentDescription = null)
        Box(Modifier.matchParentSize().background(overlay))
    }
""".trimIndent()

private val masonryCode = """
    Row {
        thumbnails.forEachIndexed { index, thumb ->
            val expanded = expandedIndex == index
            Box(
                Modifier
                    .animateContentSize()
                    .height(if (expanded) 140.dp else thumb.baseHeight)
                    .clickable { onExpand(if (expanded) null else index) }
            )
        }
    }
""".trimIndent()

private val carouselCode = """
    Box(Modifier.clickable { onPageChange((page + 1) % pageCount) })
    Row {
        repeat(pageCount) { i ->
            val width by animateFloatAsState(if (i == page) 18f else 6f)
            Box(Modifier.width(width.dp).height(6.dp).clip(CircleShape))
        }
    }
""".trimIndent()

private val blurRevealCode = """
    val blurRadius by animateFloatAsState(
        if (loaded) 0f else 18f,
        animationSpec = tween(700)
    )
    Image(
        painter = painter,
        contentDescription = null,
        modifier = Modifier.blur(blurRadius.dp)
    )
""".trimIndent()
