package com.androidcomp.app.features.layouts.customstyles2

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/** 1. Drag-to-reorder grid — up/down controls move rows, animating into their new slot. */
@Composable
fun DragToReorderGridDemo(items: List<String>, onMove: (Int, Int) -> Unit) {
    Column(Modifier.width(220.dp)) {
        items.forEachIndexed { index, label ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF2F2F2))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, style = MaterialTheme.typography.bodyMedium)
                Row {
                    IconButton(onClick = { if (index > 0) onMove(index, index - 1) }, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "Move up")
                    }
                    IconButton(onClick = { if (index < items.lastIndex) onMove(index, index + 1) }, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Filled.KeyboardArrowDown, contentDescription = "Move down")
                    }
                }
            }
        }
    }
}

/** 2. Pinned two-pane master-detail — a narrow master list drives a wider pinned detail pane. */
@Composable
fun PinnedMasterDetailLayoutDemo(selectedIndex: Int, onSelect: (Int) -> Unit) {
    val items = listOf("Inbox", "Drafts", "Sent", "Trash")
    Row(Modifier.height(140.dp)) {
        Column(Modifier.width(90.dp).fillMaxWidth()) {
            items.forEachIndexed { index, label ->
                val selected = index == selectedIndex
                Text(
                    label,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(index) }
                        .background(if (selected) Color(0xFFEFF3FF) else Color.Transparent)
                        .padding(8.dp)
                )
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF7F8FA)
        ) {
            Box(Modifier.padding(16.dp)) {
                Text("Detail for \"${items[selectedIndex]}\"", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

/** 3. Parallax scrolling header — the header shrinks and fades as the content beneath it scrolls. */
@Composable
fun ParallaxScrollingHeaderDemo() {
    val scrollState = rememberScrollState()
    val progress = if (scrollState.maxValue > 0) (scrollState.value.toFloat() / scrollState.maxValue).coerceIn(0f, 1f) else 0f
    Column(Modifier.height(180.dp).verticalScroll(scrollState)) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(80.dp)
                .graphicsLayer {
                    alpha = 1f - progress * 0.7f
                    scaleX = 1f - progress * 0.15f
                    scaleY = 1f - progress * 0.15f
                }
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFF6C5CE7)),
            contentAlignment = Alignment.Center
        ) {
            Text("Header", color = Color.White, style = MaterialTheme.typography.titleMedium)
        }
        repeat(6) { i ->
            Text(
                "Scroll content row $i",
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 4.dp),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

/** 4. Tabbed content switcher with slide transition — content slides horizontally between tabs. */
@Composable
fun TabbedContentSwitcherDemo(selectedTabIndex: Int, onSelect: (Int) -> Unit) {
    val tabs = listOf("Overview", "Specs", "Reviews")
    Column {
        Row {
            tabs.forEachIndexed { index, label ->
                val selected = index == selectedTabIndex
                Text(
                    label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(end = 16.dp, bottom = 8.dp)
                        .clickable { onSelect(index) }
                )
            }
        }
        AnimatedContent(
            targetState = selectedTabIndex,
            transitionSpec = {
                val forward = targetState > initialState
                (slideInHorizontally(tween(300)) { if (forward) it else -it })
                    .togetherWith(slideOutHorizontally(tween(300)) { if (forward) -it else it })
            },
            label = "tabSwitch"
        ) { index ->
            Text("Content for \"${tabs[index]}\"", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

/** 5. Adaptive card-flow wrap layout — cards wrap onto new lines to fill available width. */
@Composable
fun AdaptiveCardFlowWrapDemo(cardCount: Int) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(cardCount) { index ->
            val scale by animateFloatAsState(1f, animationSpec = tween(200), label = "cardScale$index")
            Surface(
                modifier = Modifier.size(64.dp).graphicsLayer { scaleX = scale; scaleY = scale },
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFDFE6FF)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("${index + 1}", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
