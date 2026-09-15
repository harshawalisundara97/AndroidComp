package com.androidcomp.app.features.layouts.customstyles2

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun LayoutStyles2Showcase(state: LayoutStyles2State, viewModel: LayoutStyles2ViewModel) {
    Column(Modifier.fillMaxWidth()) {
        LayoutStyleRow(
            "1. Drag-to-Reorder Grid",
            "Up/down controls move a row, animating it into its new slot.",
            {
                DragToReorderGridDemo(state.reorderItems, viewModel::moveItem)
            },
            dragReorderCode
        )
        LayoutStyleRow(
            "2. Pinned Master-Detail",
            "A narrow master list drives a wider pinned detail pane beside it.",
            {
                PinnedMasterDetailLayoutDemo(state.masterSelectedIndex, viewModel::selectMasterItem)
            },
            masterDetailCode
        )
        LayoutStyleRow(
            "3. Parallax Scrolling Header",
            "The header shrinks and fades as the content beneath it scrolls.",
            {
                ParallaxScrollingHeaderDemo()
            },
            parallaxCode
        )
        LayoutStyleRow(
            "4. Tabbed Content Switcher",
            "Content slides horizontally in the direction of the newly selected tab.",
            {
                TabbedContentSwitcherDemo(state.selectedTabIndex, viewModel::selectTab)
            },
            tabbedSwitcherCode
        )
        LayoutStyleRow(
            "5. Adaptive Card-Flow Wrap",
            "Cards wrap onto new lines to fill the available width, like a tag cloud.",
            {
                Button(onClick = viewModel::addWrapCard) { Text("Add") }
                Button(onClick = viewModel::removeWrapCard) { Text("Remove") }
                AdaptiveCardFlowWrapDemo(state.wrapCardCount)
            },
            cardFlowCode
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

private val dragReorderCode = """
    items.forEachIndexed { index, label ->
        Row {
            Text(label)
            IconButton(onClick = { onMove(index, index - 1) }) { Icon(ArrowUp) }
            IconButton(onClick = { onMove(index, index + 1) }) { Icon(ArrowDown) }
        }
    }
""".trimIndent()

private val masterDetailCode = """
    Row {
        Column(Modifier.width(90.dp)) {
            items.forEachIndexed { i, label ->
                Text(label, Modifier.clickable { onSelect(i) })
            }
        }
        Surface(Modifier.fillMaxWidth()) {
            Text("Detail for ${'$'}{items[selectedIndex]}")
        }
    }
""".trimIndent()

private val parallaxCode = """
    val scrollState = rememberScrollState()
    val progress = scrollState.value.toFloat() / scrollState.maxValue
    Column(Modifier.verticalScroll(scrollState)) {
        Box(
            Modifier.graphicsLayer {
                alpha = 1f - progress * 0.7f
                scaleX = 1f - progress * 0.15f
                scaleY = 1f - progress * 0.15f
            }
        ) { Text("Header") }
        // scrollable content...
    }
""".trimIndent()

private val tabbedSwitcherCode = """
    AnimatedContent(
        targetState = selectedTabIndex,
        transitionSpec = {
            val forward = targetState > initialState
            slideInHorizontally { if (forward) it else -it } togetherWith
                slideOutHorizontally { if (forward) -it else it }
        }
    ) { index -> Text(tabs[index]) }
""".trimIndent()

private val cardFlowCode = """
    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(cardCount) { index ->
            Surface(Modifier.size(64.dp), shape = RoundedCornerShape(14.dp)) {
                Text("${'$'}{index + 1}")
            }
        }
    }
""".trimIndent()
