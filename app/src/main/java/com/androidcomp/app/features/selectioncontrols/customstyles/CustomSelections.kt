package com.androidcomp.app.features.selectioncontrols.customstyles

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.IconButton
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp

/** 1. Card-Style Selectable Option — plan-picker cards with an animated border/background highlight. */
@Composable
fun CardStyleSelectableOption(selectedPlan: String, onSelect: (String) -> Unit) {
    val plans = listOf("Basic", "Pro", "Premium")
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        plans.forEach { plan ->
            val selected = plan == selectedPlan
            val borderColor by animateColorAsState(
                if (selected) MaterialTheme.colorScheme.primary else Color(0xFFE5E7EB),
                label = "planBorderColor$plan"
            )
            val backgroundColor by animateColorAsState(
                if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.06f) else Color.Transparent,
                label = "planBackgroundColor$plan"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(backgroundColor)
                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                    .clickable { onSelect(plan) }
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(plan, color = MaterialTheme.colorScheme.onSurface)
                if (selected) {
                    Icon(Icons.Outlined.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}

/** 2. Star Rating Selector — tap or drag across 5 stars to set the rating. */
@Composable
fun StarRatingSelector(rating: Int, onRatingChange: (Int) -> Unit) {
    var starWidth by remember { mutableStateOf(1f) }

    Row(
        modifier = Modifier
            .onSizeChanged { size: IntSize -> starWidth = size.width / 5f }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change, _ ->
                    val index = (change.position.x / starWidth).toInt().coerceIn(0, 4)
                    onRatingChange(index + 1)
                }
            }
    ) {
        repeat(5) { index ->
            val filled = index < rating
            val tint by animateColorAsState(
                if (filled) Color(0xFFFFB020) else Color(0xFFD8DBE2),
                label = "starTint$index"
            )
            val scale by animateFloatAsState(if (filled) 1.1f else 1f, label = "starScale$index")
            Icon(
                imageVector = if (filled) Icons.Outlined.Star else Icons.Outlined.StarBorder,
                contentDescription = "Star ${index + 1}",
                tint = tint,
                modifier = Modifier
                    .size(32.dp)
                    .clickable { onRatingChange(index + 1) }
                    .graphicsLayer { scaleX = scale; scaleY = scale }
            )
        }
    }
}

/** 3. Color Swatch Selector — circular swatches; selected one shows a checkmark and scales up. */
@Composable
fun ColorSwatchSelector(selectedIndex: Int, onSelect: (Int) -> Unit) {
    val colors = listOf(
        Color(0xFF2F6FED), Color(0xFF2ECC71), Color(0xFFFFB020),
        Color(0xFFE74C3C), Color(0xFF8B5CF6), Color(0xFF00B4D8)
    )
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        colors.forEachIndexed { index, color ->
            val selected = index == selectedIndex
            val scale by animateFloatAsState(if (selected) 1.15f else 1f, label = "swatchScale$index")
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .clip(CircleShape)
                    .background(color)
                    .clickable { onSelect(index) },
                contentAlignment = Alignment.Center
            ) {
                if (selected) {
                    Icon(Icons.Outlined.Check, contentDescription = "Selected", tint = Color.White, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

/** 4. Segmented Toggle Button Group — single-select pill row with an animated sliding background. */
@Composable
fun SegmentedToggleButtonGroup(selectedIndex: Int, onSelect: (Int) -> Unit) {
    val options = listOf("Day", "Week", "Month")
    val segmentWidth = 84.dp
    val indicatorOffset by animateDpAsState(segmentWidth * selectedIndex, label = "segmentedIndicatorOffset")

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFEDEEF2))
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .offset(x = indicatorOffset)
                .width(segmentWidth)
                .height(36.dp)
                .clip(RoundedCornerShape(50))
                .background(MaterialTheme.colorScheme.primary)
        )
        Row {
            options.forEachIndexed { index, option ->
                val selected = index == selectedIndex
                val textColor by animateColorAsState(
                    if (selected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    label = "segmentedTextColor$index"
                )
                Box(
                    modifier = Modifier
                        .width(segmentWidth)
                        .height(36.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable { onSelect(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(option, color = textColor)
                }
            }
        }
    }
}

/** 5. Stepper Selector — horizontal +/- stepper with a centered animated number display. */
@Composable
fun StepperSelector(count: Int, onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        IconButton(onClick = onDecrement) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEDEEF2)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Remove, contentDescription = "Decrease")
            }
        }
        AnimatedContent(targetState = count, label = "stepperCount") { value ->
            Text(value.toString(), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
        }
        IconButton(onClick = onIncrement) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Add, contentDescription = "Increase", tint = Color.White)
            }
        }
    }
}
