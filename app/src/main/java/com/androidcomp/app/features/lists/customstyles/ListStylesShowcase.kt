package com.androidcomp.app.features.lists.customstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun ListStylesShowcase(
    swipeItems: List<SwipeItem>,
    onDeleteSwipeItem: (Int) -> Unit,
    expandedRowId: Int?,
    onToggleExpandedRow: (Int) -> Unit,
    reorderItems: List<ReorderItem>,
    onMoveReorderItem: (Int, Int) -> Unit
) {
    val expandableRows = listOf(
        ExpandableRow(1, "Wi-Fi Network", "Connected to \"Home 5G\" — tap to see signal strength and password options."),
        ExpandableRow(2, "Storage", "42.1 GB used of 128 GB. Tap to view a breakdown by app category.")
    )

    Column(Modifier.fillMaxWidth()) {
        ListStyleRow(
            "1. Swipe to Delete",
            "Drag a row left past the threshold to delete it.",
            { SwipeToDeleteList(items = swipeItems, onDelete = onDeleteSwipeItem) },
            swipeToDeleteCode
        )
        ListStyleRow(
            "2. Expandable Row",
            "Tap a row to reveal extra detail content with a smooth size animation.",
            { ExpandableRowList(items = expandableRows, expandedRowId = expandedRowId, onToggle = onToggleExpandedRow) },
            expandableRowCode
        )
        ListStyleRow(
            "3. Drag to Reorder",
            "Use the up/down controls to reorder items; position changes are animated.",
            { DragToReorderList(items = reorderItems, onMove = onMoveReorderItem) },
            dragToReorderCode
        )
        ListStyleRow(
            "4. Grouped Sectioned List",
            "Items grouped under labeled sections, each with its own background tint.",
            { GroupedSectionedList() },
            groupedSectionedCode
        )
        ListStyleRow(
            "5. Timeline List Item",
            "Rows connected by a vertical line with a status dot marker per row.",
            { TimelineListItem() },
            timelineCode
        )
    }
}

@Composable
private fun ListStyleRow(
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
        Column(Modifier.padding(bottom = 10.dp)) {
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val swipeToDeleteCode = """
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(offsetX)

    Box(
        Modifier
            .graphicsLayer { translationX = animatedOffsetX }
            .pointerInput(item.id) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetX < deleteThreshold) onDelete() else offsetX = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount).coerceAtMost(0f)
                    }
                )
            }
    ) {
        Text(item.label)
    }
""".trimIndent()

private val expandableRowCode = """
    var expandedRowId by remember { mutableStateOf<Int?>(null) }

    items.forEach { row ->
        val expanded = expandedRowId == row.id
        Column(
            Modifier
                .clickable { expandedRowId = if (expanded) null else row.id }
                .animateContentSize(tween(250))
        ) {
            Text(row.title)
            if (expanded) Text(row.detail)
        }
    }
""".trimIndent()

private val dragToReorderCode = """
    items.forEachIndexed { index, item ->
        Row {
            Text(item.label, Modifier.weight(1f))
            IconButton(onClick = { if (index > 0) onMove(index, index - 1) }) {
                Icon(Icons.Outlined.KeyboardArrowUp, null)
            }
            IconButton(onClick = { if (index < items.lastIndex) onMove(index, index + 1) }) {
                Icon(Icons.Outlined.KeyboardArrowDown, null)
            }
        }
    }

    fun moveItem(items: MutableList<T>, from: Int, to: Int) {
        val item = items.removeAt(from)
        items.add(to, item)
    }
""".trimIndent()

private val groupedSectionedCode = """
    groups.forEach { (header, groupItems) ->
        Text(header, color = Color.Gray, fontSize = 12.sp)
        Column(
            Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(sectionTint)
        ) {
            groupItems.forEach { label -> Text(label, Modifier.padding(16.dp)) }
        }
    }
""".trimIndent()

private val timelineCode = """
    events.forEachIndexed { index, (label, dotColor) ->
        Row {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(Modifier.size(12.dp).clip(CircleShape).background(dotColor))
                if (index != events.lastIndex) {
                    Box(Modifier.width(2.dp).height(40.dp).background(Color(0xFFE5E7EB)))
                }
            }
            Text(label, Modifier.padding(start = 12.dp))
        }
    }
""".trimIndent()
