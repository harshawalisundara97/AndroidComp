package com.androidcomp.app.features.materialcomponents.customstyles2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * 1. A Material3 DatePicker-styled calendar month grid with an animated
 * selection circle that slides between days.
 */
@Composable
fun CalendarGridPicker(selectedDate: Int, onSelectDate: (Int) -> Unit) {
    val days = (1..30).toList()
    Column(Modifier.fillMaxWidth()) {
        Text(
            "August 2026",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        days.chunked(7).forEach { week ->
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                week.forEach { day ->
                    val selected = day == selectedDate
                    val bgColor by animateColorAsState(
                        if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        label = "dayBg"
                    )
                    val textColor by animateColorAsState(
                        if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                        label = "dayText"
                    )
                    Box(
                        Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(bgColor)
                            .clickable { onSelectDate(day) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(day.toString(), style = MaterialTheme.typography.bodySmall, color = textColor)
                    }
                }
            }
            Spacer(Modifier.height(4.dp))
        }
    }
}

/**
 * 2. A TimePicker-styled analog clock dial. Tapping a number animates the
 * hand's rotation to point at the selected hour.
 */
@Composable
fun ClockDialPicker(selectedHour: Int, onSelectHour: (Int) -> Unit) {
    val radiusDp = 90.dp
    val targetAngle = (selectedHour % 12) * 30f - 90f
    val animatedAngle by animateFloatAsState(targetAngle, label = "clockAngle")
    val trackColor = MaterialTheme.colorScheme.surfaceVariant
    val handColor = MaterialTheme.colorScheme.primary
    val labelColor = MaterialTheme.colorScheme.onSurface

    Box(
        Modifier.size(radiusDp * 2 + 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.size(radiusDp * 2 + 32.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            drawCircle(color = trackColor, radius = size.minDimension / 2f, center = center)
            val angleRad = Math.toRadians(animatedAngle.toDouble())
            val handEnd = Offset(
                center.x + (radiusDp.toPx() - 24.dp.toPx()) * cos(angleRad).toFloat(),
                center.y + (radiusDp.toPx() - 24.dp.toPx()) * sin(angleRad).toFloat()
            )
            drawLine(
                color = handColor,
                start = center,
                end = handEnd,
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )
            drawCircle(color = handColor, radius = 8f, center = center)
        }
        for (hour in 1..12) {
            val angleRad = Math.toRadians((hour % 12) * 30.0 - 90.0)
            val x = (radiusDp.toPx0() * cos(angleRad)).toFloat()
            val y = (radiusDp.toPx0() * sin(angleRad)).toFloat()
            Box(
                Modifier
                    .offset { IntOffset(x.roundToInt(), y.roundToInt()) }
                    .size(28.dp)
                    .clip(CircleShape)
                    .clickable { onSelectHour(hour) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    hour.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (hour == selectedHour) handColor else labelColor
                )
            }
        }
    }
}

private fun androidx.compose.ui.unit.Dp.toPx0(): Float = this.value * 2.75f

/**
 * 3. A vertical NavigationRail with a pill-shaped selection indicator that
 * slides smoothly between destinations.
 */
@Composable
fun AnimatedNavigationRail(selectedIndex: Int, onSelect: (Int) -> Unit) {
    val items = listOf(
        Icons.Outlined.Home to "Home",
        Icons.Outlined.Favorite to "Saved",
        Icons.Outlined.Notifications to "Alerts",
        Icons.Outlined.Person to "Profile"
    )
    val itemHeight = 56.dp
    val indicatorOffset by animateDpAsState(itemHeight * selectedIndex, label = "railIndicator")

    Box(
        Modifier
            .width(72.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
    ) {
        Box(
            Modifier
                .offset(y = indicatorOffset)
                .padding(8.dp)
                .width(56.dp)
                .height(itemHeight - 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Column {
            items.forEachIndexed { index, (icon, label) ->
                val selected = index == selectedIndex
                val tint by animateColorAsState(
                    if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "railTint"
                )
                Column(
                    Modifier
                        .height(itemHeight)
                        .width(72.dp)
                        .clickable { onSelect(index) },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(icon, contentDescription = label, tint = tint, modifier = Modifier.size(22.dp))
                    Text(label, style = MaterialTheme.typography.labelSmall, color = tint)
                }
            }
        }
    }
}

/**
 * 4. A custom Snackbar with an inline action that can be swiped
 * horizontally to dismiss, with the offset and fade driven by an
 * Animatable.
 */
@Composable
fun SwipeToDismissSnackbarDemo(visible: Boolean, onShow: () -> Unit, onDismiss: () -> Unit) {
    Column {
        androidx.compose.material3.Button(onClick = onShow) { Text("Show Snackbar") }
        AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
            val offsetX = remember { Animatable(0f) }
            val scope = rememberCoroutineScope()
            val alpha by animateFloatAsState(1f - (kotlin.math.abs(offsetX.value) / 600f).coerceIn(0f, 1f), label = "snackAlpha")

            Row(
                Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.inverseSurface.copy(alpha = alpha))
                    .padding(horizontal = 16.dp, vertical = 14.dp)
                    .pointerInput(Unit) {
                        detectHorizontalDragGestures(
                            onDragEnd = {
                                scope.launch {
                                    if (kotlin.math.abs(offsetX.value) > 250f) {
                                        offsetX.animateTo(if (offsetX.value > 0) 800f else -800f, tween(200))
                                        onDismiss()
                                        offsetX.snapTo(0f)
                                    } else {
                                        offsetX.animateTo(0f, tween(200))
                                    }
                                }
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            scope.launch { offsetX.snapTo(offsetX.value + dragAmount) }
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Message archived",
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "UNDO",
                    color = MaterialTheme.colorScheme.inversePrimary,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.clickable { onDismiss() }
                )
            }
        }
    }
}

/**
 * 5. An ExposedDropdownMenu-styled select field: tapping it rotates the
 * chevron 180 degrees and expands a list of options.
 */
@Composable
fun AnimatedDropdownSelect(
    expanded: Boolean,
    selected: String,
    onToggle: () -> Unit,
    onSelectOption: (String) -> Unit
) {
    val options = listOf("Newest", "Oldest", "Most Popular", "Price: Low to High")
    val chevronRotation by animateFloatAsState(if (expanded) 180f else 0f, label = "chevronRotation")

    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
                .clickable { onToggle() }
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(selected, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Icon(
                Icons.Outlined.ExpandMore,
                contentDescription = if (expanded) "Collapse" else "Expand",
                modifier = Modifier.rotate(chevronRotation)
            )
        }
        AnimatedVisibility(
            visible = expanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                options.forEach { option ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onSelectOption(option) }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(option, style = MaterialTheme.typography.bodyMedium)
                        if (option == selected) {
                            Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            }
        }
    }
}
