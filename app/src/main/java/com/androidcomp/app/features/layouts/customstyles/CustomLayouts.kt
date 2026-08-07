package com.androidcomp.app.features.layouts.customstyles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

private fun Int.textSp() = TextUnit(this.toFloat(), TextUnitType.Sp)

/** 1. Collapsing Header Layout — header height shrinks as you tap through the list beneath it. */
@Composable
fun CollapsingHeaderLayout() {
    val maxHeaderHeight = 140.dp
    val minHeaderHeight = 56.dp
    var scrollOffsetPx by remember { mutableFloatStateOf(0f) }
    val collapseRangePx = 300f
    val collapseFraction = (scrollOffsetPx / collapseRangePx).coerceIn(0f, 1f)
    val headerHeight = maxHeaderHeight - (maxHeaderHeight - minHeaderHeight) * collapseFraction
    val titleSize by animateFloatAsState(if (collapseFraction > 0.5f) 16f else 22f, label = "collapsingHeaderTitle")

    Column(
        Modifier
            .fillMaxWidth()
            .height(260.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF4F5F7))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .background(Color(0xFF2F6FED)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                "Collapsing Header",
                color = Color.White,
                fontSize = TextUnit(titleSize, TextUnitType.Sp),
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(12) { index ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .clickable {
                            scrollOffsetPx = (scrollOffsetPx + 60f).let {
                                if (it > collapseRangePx) 0f else it
                            }
                        },
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        "Row ${index + 1} (tap any row to scroll)",
                        modifier = Modifier.padding(start = 12.dp),
                        fontSize = 13.textSp()
                    )
                }
            }
        }
    }
}

/** 2. Staggered Grid — a two-column staggered layout with varied box heights. */
@Composable
fun StaggeredGridLayout() {
    val heights = listOf(90, 130, 70, 150, 100, 80, 140, 110)
    val colors = listOf(
        Color(0xFF6C5CE7), Color(0xFF00B4D8), Color(0xFFFF9F43), Color(0xFF2ECC71),
        Color(0xFFEE5A6F), Color(0xFF3A86FF), Color(0xFFFFB703), Color(0xFF8E44AD)
    )
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = Modifier.height(320.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(heights.size) { index ->
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(heights[index].dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(colors[index % colors.size]),
                contentAlignment = Alignment.Center
            ) {
                Text("Item ${index + 1}", color = Color.White, fontSize = 13.textSp())
            }
        }
    }
}

/** 3. Swipeable Card Stack — drag the top card horizontally to dismiss and reveal the next. */
@Composable
fun SwipeableCardStackLayout() {
    val cardColors = remember {
        mutableStateListOf(Color(0xFF2F6FED), Color(0xFF6C5CE7), Color(0xFF00B4D8))
    }
    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(offsetX, label = "cardStackOffset")
    val rotation = (animatedOffsetX / 20f).coerceIn(-20f, 20f)

    Box(
        Modifier
            .fillMaxWidth()
            .height(220.dp),
        contentAlignment = Alignment.Center
    ) {
        cardColors.asReversed().forEachIndexed { reverseIndex, color ->
            val index = cardColors.size - 1 - reverseIndex
            val isTop = index == cardColors.size - 1
            val depthFromTop = cardColors.size - 1 - index
            val scale = 1f - depthFromTop * 0.05f
            val verticalOffset = depthFromTop * 10

            Box(
                modifier = Modifier
                    .size(220.dp, 140.dp)
                    .graphicsLayer {
                        if (isTop) {
                            translationX = animatedOffsetX
                            rotationZ = rotation
                        } else {
                            scaleX = scale
                            scaleY = scale
                        }
                        translationY = verticalOffset.toFloat()
                    }
                    .clip(RoundedCornerShape(20.dp))
                    .background(color)
                    .then(
                        if (isTop) {
                            Modifier.pointerInput(cardColors.size) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        if (offsetX.absoluteValue > 250f) {
                                            val dismissed = cardColors.removeAt(cardColors.lastIndex)
                                            cardColors.add(0, dismissed)
                                        }
                                        offsetX = 0f
                                    },
                                    onHorizontalDrag = { change, dragAmount ->
                                        change.consume()
                                        offsetX += dragAmount
                                    }
                                )
                            }
                        } else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isTop) {
                    Text("Drag me →", color = Color.White, fontSize = 15.textSp())
                }
            }
        }
    }
}

/** 4. Expandable Accordion Section — tap a header to expand its content; only one open at a time. */
@Composable
fun ExpandableAccordionLayout(expandedIndex: Int, onToggle: (Int) -> Unit) {
    val sections = listOf(
        "Shipping Details" to "Orders ship within 2 business days via standard courier.",
        "Return Policy" to "Items can be returned within 30 days in original condition.",
        "Payment Options" to "We accept all major cards, wallets, and bank transfers."
    )
    Column(Modifier.fillMaxWidth()) {
        sections.forEachIndexed { index, pair ->
            val (title, body) = pair
            val expanded = expandedIndex == index
            val rotation by animateFloatAsState(if (expanded) 180f else 0f, label = "accordionChevron")
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF4F5F7))
            ) {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onToggle(index) }
                        .padding(16.dp)
                ) {
                    Text(title, fontSize = 15.textSp(), modifier = Modifier.align(Alignment.CenterStart))
                    Icon(
                        if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .graphicsLayer { rotationZ = rotation }
                    )
                }
                AnimatedVisibility(
                    visible = expanded,
                    enter = expandVertically(tween(250)) + fadeIn(),
                    exit = shrinkVertically(tween(250)) + fadeOut()
                ) {
                    Text(
                        body,
                        fontSize = 13.textSp(),
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    )
                }
            }
        }
    }
}

/** 5. Sticky Header List — grouped sections whose headers stick while items scroll past. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StickyHeaderListLayout() {
    val groups = listOf(
        "Favorites" to listOf("Home Dashboard", "Analytics"),
        "Recent" to listOf("Invoice #1029", "Invoice #1028", "Invoice #1027"),
        "Archived" to listOf("Old Report", "Legacy Draft")
    )
    LazyColumn(Modifier.height(260.dp)) {
        groups.forEach { group ->
            val (header, groupItems) = group
            stickyHeader {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2F6FED))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(header, color = Color.White, fontSize = 13.textSp())
                }
            }
            items(groupItems) { label ->
                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(label, fontSize = 14.textSp())
                }
            }
        }
    }
}
