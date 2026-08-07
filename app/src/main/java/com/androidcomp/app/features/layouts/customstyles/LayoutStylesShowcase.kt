package com.androidcomp.app.features.layouts.customstyles

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
fun LayoutStylesShowcase(
    expandedAccordionIndex: Int,
    onToggleAccordion: (Int) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        LayoutStyleRow(
            "1. Collapsing Header",
            "Header height shrinks smoothly as the content beneath it scrolls.",
            { CollapsingHeaderLayout() },
            collapsingHeaderCode
        )
        LayoutStyleRow(
            "2. Staggered Grid",
            "Two-column masonry-style grid with items of varied heights.",
            { StaggeredGridLayout() },
            staggeredGridCode
        )
        LayoutStyleRow(
            "3. Swipeable Card Stack",
            "Drag the top card horizontally to dismiss it and reveal the next.",
            { SwipeableCardStackLayout() },
            swipeableStackCode
        )
        LayoutStyleRow(
            "4. Expandable Accordion",
            "Tap a section header to expand its content; only one section is open at a time.",
            { ExpandableAccordionLayout(expandedAccordionIndex, onToggleAccordion) },
            accordionCode
        )
        LayoutStyleRow(
            "5. Sticky Header List",
            "Grouped list where each section header sticks to the top while its items scroll past.",
            { StickyHeaderListLayout() },
            stickyHeaderCode
        )
    }
}

@Composable
private fun LayoutStyleRow(
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

private val collapsingHeaderCode = """
    var scrollOffsetPx by remember { mutableFloatStateOf(0f) }
    val collapseFraction = (scrollOffsetPx / collapseRangePx).coerceIn(0f, 1f)
    val headerHeight = maxHeaderHeight - (maxHeaderHeight - minHeaderHeight) * collapseFraction

    Box(Modifier.fillMaxWidth().height(headerHeight).background(primary)) {
        Text("Collapsing Header", color = Color.White)
    }
    LazyColumn { items(rowCount) { /* rows; update scrollOffsetPx as user scrolls */ } }
""".trimIndent()

private val staggeredGridCode = """
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(itemCount) { index ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(heights[index].dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors[index % colors.size])
            )
        }
    }
""".trimIndent()

private val swipeableStackCode = """
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(offsetX)
    val rotation = (animatedOffsetX / 20f).coerceIn(-20f, 20f)

    Box(
        Modifier
            .graphicsLayer { translationX = animatedOffsetX; rotationZ = rotation }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetX.absoluteValue > 250f) { /* dismiss + cycle to back */ }
                        offsetX = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount
                    }
                )
            }
    )
""".trimIndent()

private val accordionCode = """
    var expandedIndex by remember { mutableIntStateOf(0) }

    sections.forEachIndexed { index, (title, body) ->
        val expanded = expandedIndex == index
        Column {
            Row(Modifier.clickable { expandedIndex = if (expanded) -1 else index }) {
                Text(title)
                Icon(if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore, null)
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(tween(250)) + fadeIn(),
                exit = shrinkVertically(tween(250)) + fadeOut()
            ) {
                Text(body)
            }
        }
    }
""".trimIndent()

private val stickyHeaderCode = """
    LazyColumn {
        groups.forEach { (header, groupItems) ->
            stickyHeader {
                Box(Modifier.fillMaxWidth().background(primary).padding(16.dp, 8.dp)) {
                    Text(header, color = Color.White)
                }
            }
            items(groupItems) { label -> Text(label, Modifier.padding(16.dp)) }
        }
    }
""".trimIndent()
