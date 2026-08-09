package com.androidcomp.app.features.materialcomponents.customstyles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp

/** 1. Custom Chip Group — multi-select filter chips with animated checkmark + color fade. */
@Composable
fun CustomChipGroup(selected: Set<String>, onToggle: (String) -> Unit) {
    val options = listOf("Wireless", "On Sale", "Top Rated", "New")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { label ->
            val isSelected = selected.contains(label)
            val bgColor by androidx.compose.animation.animateColorAsState(
                if (isSelected) Color(0xFF3949AB) else Color(0xFFF2F2F2),
                tween(200),
                label = "chipBg"
            )
            val textColor by androidx.compose.animation.animateColorAsState(
                if (isSelected) Color.White else Color(0xFF444444),
                tween(200),
                label = "chipText"
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(bgColor)
                    .clickable { onToggle(label) }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                AnimatedVisibility(visible = isSelected, enter = fadeIn(), exit = fadeOut()) {
                    Icon(
                        Icons.Filled.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
                if (isSelected) Spacer(Modifier.width(4.dp))
                Text(label, color = textColor, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

/** 2. FAB with Speed Dial — expands into 3 staggered mini-FABs. */
@Composable
fun SpeedDialFab(expanded: Boolean, onToggle: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val actions = listOf(
            Icons.Outlined.Camera to "Camera",
            Icons.Outlined.Mic to "Voice",
            Icons.Outlined.Edit to "Edit"
        )
        actions.forEachIndexed { index, (icon, desc) ->
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(tween(150, delayMillis = index * 60)) +
                    scaleIn(tween(150, delayMillis = index * 60)),
                exit = fadeOut(tween(120)) + scaleOut(tween(120))
            ) {
                SmallFloatingActionButton(
                    onClick = onToggle,
                    containerColor = Color(0xFF3949AB),
                    contentColor = Color.White,
                    modifier = Modifier.padding(bottom = 10.dp)
                ) {
                    Icon(icon, contentDescription = desc)
                }
            }
        }
        val rotation by animateFloatAsState(if (expanded) 45f else 0f, tween(200), label = "fabRotate")
        FloatingActionButton(onClick = onToggle) {
            Icon(
                Icons.Outlined.Add,
                contentDescription = if (expanded) "Close" else "Open actions",
                modifier = Modifier.graphicsLayer { rotationZ = rotation }
            )
        }
    }
}

/** 3. Custom Bottom Sheet Handle — draggable pill handle, drag down far enough dismisses. */
@Composable
fun DraggableBottomSheetDemo(visible: Boolean, onShow: () -> Unit, onDismiss: () -> Unit) {
    Column {
        if (visible) {
            Text(
                "Drag the handle down to dismiss",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        if (!visible) {
            Button(onClick = onShow) { Text("Show Sheet") }
        } else {
            var dragOffset by remember { mutableFloatStateOf(0f) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(0, dragOffset.toInt()) }
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color(0xFFFFFFFF))
                    .border(1.dp, Color(0xFFF2F2F2), RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(vertical = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragEnd = {
                                    if (dragOffset > 80f) onDismiss()
                                    dragOffset = 0f
                                }
                            ) { change, dragAmount ->
                                change.consume()
                                dragOffset = (dragOffset + dragAmount.y).coerceAtLeast(0f)
                            }
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .width(36.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFD0D0D0))
                    )
                    Spacer(Modifier.height(12.dp))
                    Text("Sheet Content", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

/** 4. Animated Badge on Icon — badge dot pulses while unread. */
@Composable
fun PulsingNotificationBadge(unread: Boolean, onToggle: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "badgePulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (unread) 1.4f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badgeScale"
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box {
            Icon(
                Icons.Filled.Notifications,
                contentDescription = "Notifications",
                modifier = Modifier.size(32.dp)
            )
            if (unread) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 4.dp, y = (-2).dp)
                        .size(10.dp)
                        .graphicsLayer { scaleX = scale; scaleY = scale }
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.error)
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        TextButton(onClick = onToggle) {
            Text(if (unread) "Mark as read" else "Mark as unread")
        }
    }
}

/** 5. Segmented Button Group — sliding highlight behind the selected segment. */
@Composable
fun SlidingSegmentedButtons(selectedIndex: Int, onSelect: (Int) -> Unit) {
    val labels = listOf("Day", "Week", "Month", "Year")
    val segmentWidth = 72.dp
    val offsetX by animateDpAsState(segmentWidth * selectedIndex, tween(250), label = "segmentOffset")

    Box(
        modifier = Modifier
            .wrapContentWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF2F2F2))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = offsetX)
                .width(segmentWidth)
                .height(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        )
        Row {
            labels.forEachIndexed { index, label ->
                val isSelected = index == selectedIndex
                Box(
                    modifier = Modifier
                        .width(segmentWidth)
                        .height(36.dp)
                        .clickable { onSelect(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) Color(0xFF111827) else Color(0xFF8B8B93)
                    )
                }
            }
        }
    }
}
