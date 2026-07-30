package com.androidcomp.app.features.selectioncontrols.toggles

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bedtime
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

private val TrackWidth = 56.dp
private val TrackHeight = 32.dp
private val ThumbSize = 24.dp
private val ThumbTravel = TrackWidth - ThumbSize - 8.dp // 4dp padding each side

/** 1. Material You Fluid Spring — indigo track, thumb bounces over with spring physics, X morphs to check. */
@Composable
fun FluidSpringToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF3949AB) else Color(0xFF8B93A8),
        label = "fluidSpringTrack"
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) ThumbTravel else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "fluidSpringThumb"
    )
    Box(
        modifier = Modifier
            .size(TrackWidth, TrackHeight)
            .background(trackColor, RoundedCornerShape(50))
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(ThumbSize)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(targetState = checked, label = "fluidSpringIcon") { isChecked ->
                Icon(
                    if (isChecked) Icons.Outlined.Check else Icons.Outlined.Close,
                    contentDescription = null,
                    tint = Color(0xFF6B7280),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

/** 2. Day & Night Morph — sky-blue to midnight-navy, sun rotates and morphs into a moon. */
@Composable
fun DayNightToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF0D1B4C) else Color(0xFF4FC3F7),
        animationSpec = tween(500),
        label = "dayNightTrack"
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) ThumbTravel else 0.dp,
        animationSpec = tween(500),
        label = "dayNightThumb"
    )
    val rotation by animateFloatAsState(
        targetValue = if (checked) 360f else 0f,
        animationSpec = tween(500),
        label = "dayNightRotation"
    )
    Box(
        modifier = Modifier
            .size(TrackWidth, TrackHeight)
            .background(trackColor, RoundedCornerShape(50))
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(ThumbSize)
                .graphicsLayer { rotationZ = rotation },
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(targetState = checked, label = "dayNightIcon") { isChecked ->
                Icon(
                    if (isChecked) Icons.Outlined.Bedtime else Icons.Outlined.WbSunny,
                    contentDescription = null,
                    tint = if (isChecked) Color(0xFFE0E0E0) else Color(0xFFFFC107),
                    modifier = Modifier.size(ThumbSize)
                )
            }
        }
    }
}

/** 3. Cyberpunk Neon — dark box, glowing cyan border, square thumb snaps instantly (no bounce). */
@Composable
fun CyberpunkNeonToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val glowColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF00E5FF) else Color(0xFF3A3F4B),
        animationSpec = tween(150, easing = LinearEasing),
        label = "cyberpunkGlow"
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) ThumbTravel else 0.dp,
        animationSpec = tween(150, easing = LinearEasing),
        label = "cyberpunkThumb"
    )
    val thumbColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF00E5FF) else Color(0xFF6B7280),
        animationSpec = tween(150, easing = LinearEasing),
        label = "cyberpunkThumbColor"
    )
    Box(
        modifier = Modifier
            .size(TrackWidth, TrackHeight)
            .background(Color(0xFF0A0A0F), RoundedCornerShape(8.dp))
            .border(2.dp, glowColor, RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(ThumbSize)
                .background(thumbColor, RoundedCornerShape(4.dp))
        )
    }
}

/** 4. Soft Neumorphic UI — carved-in track, soft glide, inner LED dot lights up emerald when active. */
@Composable
fun NeumorphicToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) ThumbTravel else 0.dp,
        animationSpec = tween(300),
        label = "neumorphicThumb"
    )
    val ledColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF2ECC71) else Color(0xFF444444),
        label = "neumorphicLed"
    )
    Box(
        modifier = Modifier
            .size(TrackWidth, TrackHeight)
            .background(Color(0xFFE4E4E7), RoundedCornerShape(50))
            .border(1.dp, Color(0xFFCFCFD4), RoundedCornerShape(50))
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(ThumbSize)
                .background(Color(0xFFFAFAFA), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(ledColor, CircleShape)
            )
        }
    }
}

/** 5. Minimalist Elastic Pill — flat gray-to-emerald track, thumb squishes like rubber mid-slide. */
@Composable
fun ElasticPillToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) Color(0xFF2ECC71) else Color(0xFFD0D0D0),
        label = "elasticPillTrack"
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) ThumbTravel else 0.dp,
        animationSpec = tween(300),
        label = "elasticPillThumb"
    )
    val scaleX by animateFloatAsState(
        targetValue = if (checked) 1f else 1f,
        animationSpec = keyframes {
            durationMillis = 300
            1f at 0
            1.6f at 150
            1f at 300
        },
        label = "elasticPillScaleX"
    )
    val scaleY by animateFloatAsState(
        targetValue = if (checked) 1f else 1f,
        animationSpec = keyframes {
            durationMillis = 300
            1f at 0
            0.7f at 150
            1f at 300
        },
        label = "elasticPillScaleY"
    )
    Box(
        modifier = Modifier
            .size(TrackWidth, TrackHeight)
            .background(trackColor, RoundedCornerShape(50))
            .clickable { onCheckedChange(!checked) }
            .padding(4.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = thumbOffset)
                .size(ThumbSize)
                .graphicsLayer { this.scaleX = scaleX; this.scaleY = scaleY }
                .background(Color.White, CircleShape)
        )
    }
}
