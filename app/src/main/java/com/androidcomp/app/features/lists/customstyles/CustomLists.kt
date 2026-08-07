package com.androidcomp.app.features.lists.customstyles

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/** 1. Swipe to Delete — drag a row left past a threshold to reveal + trigger delete. */
@Composable
fun SwipeToDeleteList(items: List<SwipeItem>, onDelete: (Int) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        items.forEach { item ->
            SwipeToDeleteRow(item = item, onDelete = { onDelete(item.id) })
        }
    }
}

@Composable
private fun SwipeToDeleteRow(item: SwipeItem, onDelete: () -> Unit) {
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(offsetX, label = "swipeDeleteOffset")
    val deleteThreshold = -180f
    val revealFraction = (-animatedOffsetX / -deleteThreshold).coerceIn(0f, 1f)

    Box(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .height(56.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFE74C3C)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.weight(1f))
            Icon(
                Icons.Outlined.Delete,
                contentDescription = "Delete",
                tint = Color.White.copy(alpha = revealFraction),
                modifier = Modifier.padding(end = 20.dp)
            )
        }
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .graphicsLayer { translationX = animatedOffsetX }
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFF4F5F7))
                .pointerInput(item.id) {
                    detectHorizontalDragGestures(
                        onDragEnd = {
                            if (offsetX < deleteThreshold) {
                                onDelete()
                            } else {
                                offsetX = 0f
                            }
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            offsetX = (offsetX + dragAmount).coerceAtMost(0f)
                        }
                    )
                }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(item.label, fontSize = androidx.compose.ui.unit.TextUnit(14f, androidx.compose.ui.unit.TextUnitType.Sp))
        }
    }
}

/** 2. Expandable Row — tap to reveal extra detail content, size animates smoothly. */
@Composable
fun ExpandableRowList(items: List<ExpandableRow>, expandedRowId: Int?, onToggle: (Int) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        items.forEach { row ->
            val expanded = expandedRowId == row.id
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF4F5F7))
                    .clickable { onToggle(row.id) }
                    .animateContentSize(tween(250))
                    .padding(16.dp)
            ) {
                Text(row.title, fontSize = androidx.compose.ui.unit.TextUnit(15f, androidx.compose.ui.unit.TextUnitType.Sp))
                if (expanded) {
                    Text(
                        row.detail,
                        fontSize = androidx.compose.ui.unit.TextUnit(13f, androidx.compose.ui.unit.TextUnitType.Sp),
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

/** 3. Drag to Reorder — up/down controls swap item position with an animated slide. */
@Composable
fun DragToReorderList(items: List<ReorderItem>, onMove: (Int, Int) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF4F5F7))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${index + 1}.", color = Color(0xFF9CA3AF), modifier = Modifier.width(24.dp))
                Text(item.label, modifier = Modifier.weight(1f))
                IconButton(onClick = { if (index > 0) onMove(index, index - 1) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Outlined.KeyboardArrowUp, contentDescription = "Move up")
                }
                IconButton(onClick = { if (index < items.lastIndex) onMove(index, index + 1) }, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Outlined.KeyboardArrowDown, contentDescription = "Move down")
                }
            }
        }
    }
}

/** 4. Grouped Sectioned List — items grouped under labeled sections with distinct tinting. */
@Composable
fun GroupedSectionedList() {
    val groups = listOf(
        "Today" to listOf("Design review", "Ship changelog"),
        "This Week" to listOf("Sprint planning", "1:1 with mentor", "Update roadmap"),
        "Later" to listOf("Research competitors")
    )
    val sectionColors = listOf(Color(0xFFEFF6FF), Color(0xFFF0FDF4), Color(0xFFFFF7ED))

    Column(Modifier.fillMaxWidth()) {
        groups.forEachIndexed { groupIndex, group ->
            val (header, groupItems) = group
            Text(
                header,
                color = Color(0xFF6B7280),
                fontSize = androidx.compose.ui.unit.TextUnit(12f, androidx.compose.ui.unit.TextUnitType.Sp),
                modifier = Modifier.padding(top = if (groupIndex == 0) 0.dp else 12.dp, bottom = 6.dp, start = 4.dp)
            )
            Column(
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(sectionColors[groupIndex % sectionColors.size])
            ) {
                groupItems.forEachIndexed { itemIndex, label ->
                    Text(
                        label,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        fontSize = androidx.compose.ui.unit.TextUnit(14f, androidx.compose.ui.unit.TextUnitType.Sp)
                    )
                }
            }
        }
    }
}

/** 5. Timeline List Item — rows connected by a vertical line with a dot marker per row. */
@Composable
fun TimelineListItem() {
    val events = listOf(
        "Order Placed" to Color(0xFF2ECC71),
        "Packed" to Color(0xFF2ECC71),
        "Out for Delivery" to Color(0xFF2F6FED),
        "Delivered" to Color(0xFFD1D5DB)
    )
    Column(Modifier.fillMaxWidth()) {
        events.forEachIndexed { index, event ->
            val (label, dotColor) = event
            Row(Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.width(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(dotColor)
                    )
                    if (index != events.lastIndex) {
                        Box(
                            Modifier
                                .width(2.dp)
                                .height(40.dp)
                                .background(Color(0xFFE5E7EB))
                        )
                    }
                }
                Text(
                    label,
                    modifier = Modifier
                        .padding(start = 12.dp, bottom = 16.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}
