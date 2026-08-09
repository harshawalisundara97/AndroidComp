package com.androidcomp.app.features.materialcomponents.customstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
fun MaterialStylesShowcase(state: MaterialStylesState, viewModel: MaterialStylesViewModel) {
    Column(Modifier.fillMaxWidth()) {
        MaterialStyleRow(
            "1. Custom Chip Group",
            "Multi-select filter chips with an animated checkmark and color fade.",
            {
                CustomChipGroup(state.selectedChips, viewModel::toggleChip)
            },
            chipGroupCode
        )
        MaterialStyleRow(
            "2. FAB with Speed Dial",
            "Tap to expand 3 staggered mini-FABs above the main action button.",
            {
                SpeedDialFab(state.speedDialExpanded, viewModel::toggleSpeedDial)
            },
            speedDialCode
        )
        MaterialStyleRow(
            "3. Custom Bottom Sheet Handle",
            "A pill-shaped grab handle; drag it down far enough to dismiss.",
            {
                DraggableBottomSheetDemo(state.bottomSheetVisible, viewModel::showBottomSheet, viewModel::hideBottomSheet)
            },
            bottomSheetHandleCode
        )
        MaterialStyleRow(
            "4. Animated Badge on Icon",
            "A notification badge dot that pulses via an infinite scale transition while unread.",
            {
                PulsingNotificationBadge(state.badgeUnread, viewModel::toggleBadgeUnread)
            },
            pulsingBadgeCode
        )
        MaterialStyleRow(
            "5. Segmented Button Group",
            "An iOS-style segmented selector with a sliding highlight behind the active segment.",
            {
                SlidingSegmentedButtons(state.selectedSegment, viewModel::selectSegment)
            },
            segmentedButtonsCode
        )
    }
}

@Composable
private fun MaterialStyleRow(
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

private val chipGroupCode = """
    val bgColor by animateColorAsState(
        if (selected) Color(0xFF3949AB) else Color(0xFFF2F2F2)
    )
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .clickable { onToggle(label) }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        AnimatedVisibility(visible = selected, enter = fadeIn(), exit = fadeOut()) {
            Icon(Icons.Filled.Check, contentDescription = null)
        }
        Text(label)
    }
""".trimIndent()

private val speedDialCode = """
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        actions.forEachIndexed { index, (icon, desc) ->
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(tween(150, delayMillis = index * 60)) +
                    scaleIn(tween(150, delayMillis = index * 60)),
                exit = fadeOut() + scaleOut()
            ) {
                SmallFloatingActionButton(onClick = onToggle) { Icon(icon, desc) }
            }
        }
        FloatingActionButton(onClick = onToggle) {
            Icon(Icons.Outlined.Add, contentDescription = null)
        }
    }
""".trimIndent()

private val bottomSheetHandleCode = """
    var dragOffset by remember { mutableFloatStateOf(0f) }
    Box(
        Modifier
            .offset { IntOffset(0, dragOffset.toInt()) }
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
            }
    ) {
        Box(Modifier.width(36.dp).height(4.dp).clip(RoundedCornerShape(50)).background(handleColor))
    }
""".trimIndent()

private val pulsingBadgeCode = """
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (unread) 1.4f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box {
        Icon(Icons.Filled.Notifications, contentDescription = null)
        if (unread) {
            Box(
                Modifier
                    .align(Alignment.TopEnd)
                    .size(10.dp)
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.error)
            )
        }
    }
""".trimIndent()

private val segmentedButtonsCode = """
    val offsetX by animateDpAsState(segmentWidth * selectedIndex)
    Box(Modifier.clip(RoundedCornerShape(14.dp)).background(Color(0xFFF2F2F2)).padding(4.dp)) {
        Box(
            Modifier
                .offset(x = offsetX)
                .width(segmentWidth)
                .height(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
        )
        Row {
            labels.forEachIndexed { index, label ->
                Box(Modifier.width(segmentWidth).clickable { onSelect(index) }) {
                    Text(label)
                }
            }
        }
    }
""".trimIndent()
