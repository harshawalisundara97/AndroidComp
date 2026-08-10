package com.androidcomp.app.features.camera.customstyles

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FlashAuto
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ViewfinderColor = Color(0xFF16181D)

/** 1. Capture Button with Press Ring — shutter button with an expanding/fading ring on press. */
@Composable
fun CaptureButtonWithPressRing(pressed: Boolean, onPressedChange: (Boolean) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val ringScale by animateFloatAsState(
        targetValue = if (isPressed) 1.7f else 1f,
        animationSpec = tween(350),
        label = "captureRingScale"
    )
    val ringAlpha by animateFloatAsState(
        targetValue = if (isPressed) 0f else 0.55f,
        animationSpec = tween(350),
        label = "captureRingAlpha"
    )
    val buttonScale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "captureButtonScale"
    )

    Box(
        modifier = Modifier
            .size(96.dp)
            .background(ViewfinderColor, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Expanding/fading press ring
        Box(
            modifier = Modifier
                .size(66.dp * ringScale)
                .border(2.dp, Color.White.copy(alpha = ringAlpha), CircleShape)
        )
        // Outer white ring
        Box(
            modifier = Modifier
                .size(66.dp)
                .border(3.dp, Color.White, CircleShape)
                .padding(6.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) { onPressedChange(!pressed) },
            contentAlignment = Alignment.Center
        ) {
            // Inner shutter disc that scales down while pressed
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .then(Modifier.size(48.dp * buttonScale))
                    .background(Color.White, CircleShape)
            )
        }
    }
}

/** 2. Viewfinder Grid Overlay — rule-of-thirds grid drawn on a mock viewfinder, toggled with a fade. */
@Composable
fun ViewfinderGridOverlay(gridVisible: Boolean, onToggle: () -> Unit) {
    val gridAlpha by animateFloatAsState(
        targetValue = if (gridVisible) 0.7f else 0f,
        animationSpec = tween(350),
        label = "viewfinderGridAlpha"
    )
    Box(
        modifier = Modifier
            .width(220.dp)
            .height(140.dp)
            .background(ViewfinderColor, RoundedCornerShape(12.dp))
            .clickable { onToggle() }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val thirdW = size.width / 3f
            val thirdH = size.height / 3f
            val lineColor = Color.White.copy(alpha = gridAlpha)
            for (i in 1..2) {
                drawLine(
                    color = lineColor,
                    start = Offset(thirdW * i, 0f),
                    end = Offset(thirdW * i, size.height),
                    strokeWidth = 1.5.dp.toPx()
                )
                drawLine(
                    color = lineColor,
                    start = Offset(0f, thirdH * i),
                    end = Offset(size.width, thirdH * i),
                    strokeWidth = 1.5.dp.toPx()
                )
            }
        }
        Text(
            if (gridVisible) "Grid ON" else "Grid OFF",
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp)
        )
    }
}

/** 3. Flash Mode Toggle — 3-state Off/Auto/On cycling control with icon crossfade. */
@Composable
fun FlashModeToggle(flashMode: Int, onCycle: () -> Unit) {
    val label = when (flashMode) {
        1 -> "Auto"
        2 -> "On"
        else -> "Off"
    }
    Box(
        modifier = Modifier
            .size(width = 96.dp, height = 48.dp)
            .background(ViewfinderColor, RoundedCornerShape(24.dp))
            .clickable { onCycle() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            AnimatedContent(targetState = flashMode, label = "flashModeIcon") { mode ->
                Icon(
                    imageVector = when (mode) {
                        1 -> Icons.Outlined.FlashAuto
                        2 -> Icons.Outlined.FlashOn
                        else -> Icons.Outlined.FlashOff
                    },
                    contentDescription = "Flash mode: $label",
                    tint = if (mode == 2) Color(0xFFFFC107) else Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
            Text(label, color = Color.White, style = MaterialTheme.typography.bodySmall)
        }
    }
}

/** 4. Photo/Video Mode Switcher — segmented control with a sliding indicator. */
@Composable
fun PhotoVideoModeSwitcher(mode: Int, onModeChange: (Int) -> Unit) {
    val segmentWidth = 84.dp
    val indicatorOffset by animateDpAsState(
        targetValue = if (mode == 0) 0.dp else segmentWidth,
        animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium),
        label = "modeSwitcherIndicator"
    )
    Box(
        modifier = Modifier
            .width(segmentWidth * 2)
            .height(40.dp)
            .background(ViewfinderColor, RoundedCornerShape(20.dp))
            .padding(3.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(segmentWidth - 6.dp)
                .height(34.dp)
                .background(Color.White, RoundedCornerShape(18.dp))
        )
        Row(Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .width(segmentWidth)
                    .fillMaxSize()
                    .clickable { onModeChange(0) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Photo",
                    color = if (mode == 0) Color.Black else Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (mode == 0) FontWeight.SemiBold else FontWeight.Normal
                )
            }
            Box(
                modifier = Modifier
                    .width(segmentWidth)
                    .fillMaxSize()
                    .clickable { onModeChange(1) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Video",
                    color = if (mode == 1) Color.Black else Color.White,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = if (mode == 1) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}

/** 5. Shutter Countdown UI — tap a timer icon to run a 3-2-1 animated countdown, then a capture flash. */
@Composable
fun ShutterCountdownUi(countdownValue: Int?, justCaptured: Boolean, onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp)
            .background(ViewfinderColor, RoundedCornerShape(16.dp))
            .clickable(enabled = countdownValue == null && !justCaptured) { onStart() },
        contentAlignment = Alignment.Center
    ) {
        when {
            justCaptured -> {
                Icon(
                    imageVector = Icons.Outlined.Check,
                    contentDescription = "Captured",
                    tint = Color(0xFF2ECC71),
                    modifier = Modifier.size(40.dp)
                )
            }
            countdownValue != null -> {
                AnimatedContent(
                    targetState = countdownValue,
                    transitionSpec = {
                        (scaleIn(initialScale = 0.4f, animationSpec = tween(250)) + fadeIn(tween(250))) togetherWith
                            (scaleOut(targetScale = 1.6f, animationSpec = tween(250)) + fadeOut(tween(250)))
                    },
                    label = "shutterCountdown"
                ) { value ->
                    Text(
                        text = value.toString(),
                        color = Color.White,
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            else -> {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Timer,
                        contentDescription = "Start countdown",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                    Text("Tap to start", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}
