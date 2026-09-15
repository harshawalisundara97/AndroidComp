package com.androidcomp.app.features.images.customstyles2

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fullscreen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/** 1. Ken Burns zoom-pan effect — a slow continuous scale+translate loop over a static image. */
@Composable
fun KenBurnsImageDemo(playing: Boolean) {
    val transition = rememberInfiniteTransition(label = "kenBurns")
    val scale by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse),
        label = "scale"
    )
    val offsetX by transition.animateFloat(
        initialValue = -12f,
        targetValue = 12f,
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing), RepeatMode.Reverse),
        label = "offsetX"
    )
    Box(
        Modifier
            .size(160.dp, 110.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF6C5CE7), Color(0xFF00B894))))
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(110.dp)
                .graphicsLayer {
                    val s = if (playing) scale else 1f
                    val tx = if (playing) offsetX else 0f
                    scaleX = s
                    scaleY = s
                    translationX = tx
                    transformOrigin = TransformOrigin(0.5f, 0.5f)
                }
                .background(Brush.radialGradient(listOf(Color(0x66FFFFFF), Color.Transparent), radius = 220f, center = Offset(80f, 40f)))
        )
    }
}

/** 2. Image with a color-filter picker — cycles a tint overlay to simulate filter presets. */
@Composable
fun ColorFilterPickerImageDemo(filterIndex: Int) {
    val filters = listOf("Original" to Color.Transparent, "Grayscale" to Color(0x99000000), "Warm" to Color(0x55FF8A00), "Cool" to Color(0x554A90E2))
    val (label, overlay) = filters[filterIndex % filters.size]
    Column {
        Box(
            Modifier
                .size(140.dp, 100.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Brush.linearGradient(listOf(Color(0xFFFF7675), Color(0xFFFAB1A0))))
        ) {
            Box(Modifier.fillMaxWidth().height(100.dp).background(overlay))
        }
        Text(label, style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(top = 6.dp))
    }
}

/** 3. Masonry thumbnail grid with tap-to-expand — varied-height tiles expand inline when tapped. */
@Composable
fun MasonryTapToExpandGridDemo(expandedIndex: Int?, onExpand: (Int?) -> Unit) {
    val heights = listOf(70.dp, 100.dp, 85.dp)
    val colors = listOf(Color(0xFF74B9FF), Color(0xFFA29BFE), Color(0xFF55EFC4))
    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        heights.forEachIndexed { index, baseHeight ->
            val expanded = expandedIndex == index
            Box(
                Modifier
                    .width(70.dp)
                    .animateContentSize()
                    .height(if (expanded) 140.dp else baseHeight)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors[index])
                    .clickable { onExpand(if (expanded) null else index) },
                contentAlignment = Alignment.BottomEnd
            ) {
                if (expanded) {
                    Icon(
                        Icons.Outlined.Fullscreen,
                        contentDescription = "Expanded",
                        tint = Color.White,
                        modifier = Modifier.padding(6.dp).size(18.dp)
                    )
                }
            }
        }
    }
}

/** 4. Image carousel with page indicator dots — swaps a featured image and animates the active dot. */
@Composable
fun ImageCarouselDotsDemo(page: Int, onPageChange: (Int) -> Unit) {
    val pages = listOf(Color(0xFFFFA502), Color(0xFF2ED573), Color(0xFF1E90FF))
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(160.dp, 100.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(pages[page % pages.size])
                .clickable { onPageChange((page + 1) % pages.size) },
            contentAlignment = Alignment.Center
        ) {
            Text("Tap to advance", color = Color.White, style = MaterialTheme.typography.labelSmall)
        }
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            pages.indices.forEach { i ->
                val active = i == page % pages.size
                val dotWidth by animateFloatAsState(if (active) 18f else 6f, label = "dotWidth")
                Box(
                    Modifier
                        .height(6.dp)
                        .width(dotWidth.dp)
                        .clip(CircleShape)
                        .background(if (active) MaterialTheme.colorScheme.primary else Color(0xFFDFE6E9))
                )
            }
        }
    }
}

/** 5. Blurred-placeholder-to-sharp loading transition — animates a blur radius down to zero. */
@Composable
fun BlurToSharpRevealDemo(revealed: Boolean) {
    val blurRadius by animateFloatAsState(if (revealed) 0f else 18f, animationSpec = tween(700), label = "blurRadius")
    Box(
        Modifier
            .size(150.dp, 100.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Brush.linearGradient(listOf(Color(0xFFFD79A8), Color(0xFFFDCB6E))))
            .blur(blurRadius.dp)
    )
}
