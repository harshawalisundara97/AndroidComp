package com.androidcomp.app.features.images.customstyles

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 1. Shimmer Loading Placeholder — animated gradient sweep simulating an image loading state. */
@Composable
fun ShimmerLoadingPlaceholder() {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val shimmerOffset by transition.animateFloat(
        initialValue = -1f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "shimmerOffset"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(Color(0xFFE9EAEE), Color(0xFFF6F7F9), Color(0xFFE9EAEE)),
                    start = Offset(shimmerOffset * 300f, 0f),
                    end = Offset(shimmerOffset * 300f + 300f, 300f)
                )
            )
    )
}

/** 2. Zoom on Tap — placeholder scales up when tapped, back to normal on second tap. */
@Composable
fun ZoomOnTapImage(zoomed: Boolean, onToggle: () -> Unit) {
    val scale by animateFloatAsState(if (zoomed) 1.3f else 1f, label = "zoomScale")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFE0E7FF))
            .clickable { onToggle() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Outlined.Image,
            contentDescription = null,
            tint = Color(0xFF6C5CE7),
            modifier = Modifier
                .size(48.dp)
                .graphicsLayer { scaleX = scale; scaleY = scale }
        )
    }
}

/** 3. Avatar with Online Status Dot — circular placeholder avatar with a colored status dot. */
@Composable
fun AvatarWithStatusDot(status: AvatarStatus, onCycleStatus: () -> Unit) {
    val statusColor by animateColorAsState(
        when (status) {
            AvatarStatus.ONLINE -> Color(0xFF2ECC71)
            AvatarStatus.AWAY -> Color(0xFFF5A623)
            AvatarStatus.OFFLINE -> Color(0xFF9CA3AF)
        },
        label = "avatarStatusColor"
    )

    Box(
        modifier = Modifier
            .size(64.dp)
            .clickable { onCycleStatus() }
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFD8DCE3)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Person, contentDescription = null, tint = Color(0xFF6B7280), modifier = Modifier.size(32.dp))
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(16.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .padding(2.dp)
                .clip(CircleShape)
                .background(statusColor)
        )
    }
}

/** 4. Gradient Scrim + Caption Overlay — bottom gradient scrim with a caption overlaid. */
@Composable
fun GradientScrimCaptionImage() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF3A3D46))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.75f))
                    )
                )
        )
        Text(
            "Golden Hour, Mount Fuji",
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

/** 5. Before/After Comparison Slider — real horizontal drag reveal between two placeholders. */
@Composable
fun BeforeAfterComparisonSlider(fraction: Float, onFractionChange: (Float) -> Unit) {
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        val widthPx = with(density) { maxWidth.toPx() }

        // "After" layer, full width
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF11998E)),
            contentAlignment = Alignment.Center
        ) {
            Text("After", color = Color.White, fontSize = 16.sp)
        }

        // "Before" layer, clipped to the drag fraction
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction)
                .background(Color(0xFFFF6B6B)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text("Before", color = Color.White, fontSize = 16.sp, modifier = Modifier.padding(start = 16.dp))
        }

        // Draggable divider handle
        Box(
            modifier = Modifier
                .offset { IntOffset((widthPx * fraction).toInt() - 4, 0) }
                .fillMaxHeight()
                .width(4.dp)
                .background(Color.White)
        )

        // Transparent drag-capture layer covering the whole slider
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, _ ->
                        change.consume()
                        onFractionChange(change.position.x / widthPx)
                    }
                }
        )
    }
}
