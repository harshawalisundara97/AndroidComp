package com.androidcomp.app.features.buttons.customstyles

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 1. Gradient Glow — vibrant gradient pill, glows and scales down slightly on press. */
@Composable
fun GradientGlowButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f, label = "gradientGlowScale")
    val glowElevation by animateDpAsState(if (pressed) 16.dp else 6.dp, label = "gradientGlowElevation")

    Box(
        modifier = Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(glowElevation, RoundedCornerShape(50), spotColor = Color(0xFF8B5CF6))
            .clip(RoundedCornerShape(50))
            .background(Brush.horizontalGradient(listOf(Color(0xFF6C5CE7), Color(0xFF00B4D8))))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 28.dp, vertical = 14.dp)
    ) {
        Text("Get Started", color = Color.White, fontSize = 15.sp)
    }
}

/** 2. Soft Neumorphic Press — carved-in look, "sinks" into the surface when pressed. */
@Composable
fun NeumorphicPressButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(if (pressed) 0.dp else 6.dp, label = "neumorphicElevation")

    Box(
        modifier = Modifier
            .shadow(elevation, RoundedCornerShape(18.dp), ambientColor = Color(0xFFB8BCC8))
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFEDEEF2))
            .border(1.dp, Color(0xFFDBDEE6), RoundedCornerShape(18.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 28.dp, vertical = 14.dp)
    ) {
        Text("Continue", color = Color(0xFF3A3D46), fontSize = 15.sp)
    }
}

/** 3. Icon Slide — leading icon slides and rotates forward on press. */
@Composable
fun IconSlideButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val iconOffset by animateDpAsState(
        if (pressed) 6.dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.5f),
        label = "iconSlideOffset"
    )
    val iconRotation by animateFloatAsState(if (pressed) 20f else 0f, label = "iconSlideRotation")

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF1F2937))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Send Message", color = Color.White, fontSize = 15.sp)
        Icon(
            Icons.AutoMirrored.Outlined.Send,
            contentDescription = null,
            tint = Color(0xFF60A5FA),
            modifier = Modifier
                .padding(start = 10.dp)
                .size(18.dp)
                .offset(x = iconOffset)
                .graphicsLayer { rotationZ = iconRotation }
        )
    }
}

/** 4. Animated Outline Sweep — outline fills with color left-to-right on press. */
@Composable
fun AnimatedOutlineSweepButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val fillFraction by animateFloatAsState(
        if (pressed) 1f else 0f,
        animationSpec = tween(350),
        label = "outlineSweepFill"
    )
    val textColor by animateColorAsState(
        if (fillFraction > 0.5f) Color.White else Color(0xFF2F6FED),
        label = "outlineSweepText"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .border(2.dp, Color(0xFF2F6FED), RoundedCornerShape(18.dp))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .graphicsLayer {
                    // scale from the left edge, not the center
                    transformOrigin = androidx.compose.ui.graphics.TransformOrigin(0f, 0.5f)
                    scaleX = fillFraction
                }
                .background(Color(0xFF2F6FED))
                .width(140.dp)
        )
        Text(
            "Learn More",
            color = textColor,
            fontSize = 15.sp,
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp)
        )
    }
}

/** 5. Loading State — idle -> spinner -> success checkmark, then resets. */
@Composable
fun LoadingStateButton(loadState: ButtonLoadState, onClick: () -> Unit) {
    val containerColor by animateColorAsState(
        if (loadState == ButtonLoadState.SUCCESS) Color(0xFF2ECC71) else Color(0xFF2F6FED),
        label = "loadingStateColor"
    )

    Box(
        modifier = Modifier
            .height(48.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(containerColor)
            .clickable(enabled = loadState == ButtonLoadState.IDLE) { onClick() }
            .padding(horizontal = 28.dp),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(targetState = loadState, label = "loadingStateContent") { state ->
            when (state) {
                ButtonLoadState.IDLE -> Text("Save Changes", color = Color.White, fontSize = 15.sp)
                ButtonLoadState.LOADING -> CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
                ButtonLoadState.SUCCESS -> Icon(Icons.Outlined.Check, contentDescription = "Saved", tint = Color.White)
            }
        }
    }
}
