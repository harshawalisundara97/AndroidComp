package com.androidcomp.app.features.menus.customstyles2

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
fun MenuStyles2Showcase(state: MenuStyles2State, viewModel: MenuStyles2ViewModel) {
    Column(Modifier.fillMaxWidth()) {
        MenuStyleRow(
            "1. Searchable Dropdown Menu",
            "A trigger that expands into a filterable list of destinations.",
            {
                SearchableDropdownMenuDemo(
                    state.searchMenuExpanded,
                    state.searchQuery,
                    viewModel::toggleSearchMenu,
                    viewModel::updateSearchQuery,
                    viewModel::closeSearchMenu
                )
            },
            searchableDropdownCode
        )
        MenuStyleRow(
            "2. Icon-Grid Quick Actions Menu",
            "A compact grid of icon actions revealed from an overflow trigger.",
            {
                IconGridQuickActionsMenuDemo(
                    state.quickActionsExpanded,
                    viewModel::toggleQuickActions,
                    viewModel::closeQuickActions
                )
            },
            iconGridCode
        )
        MenuStyleRow(
            "3. Cascading Breadcrumb Menu",
            "Tap an option to drill in; tap a breadcrumb segment to jump back.",
            {
                CascadingBreadcrumbMenuDemo(
                    state.breadcrumbPath,
                    viewModel::pushBreadcrumb,
                    viewModel::popBreadcrumbTo
                )
            },
            breadcrumbCode
        )
        MenuStyleRow(
            "4. Toolbar Overflow Menu with Badge",
            "An overflow icon carrying a notification badge that clears on action.",
            {
                ToolbarOverflowBadgeMenuDemo(
                    state.toolbarMenuExpanded,
                    state.toolbarBadgeCount,
                    viewModel::toggleToolbarMenu,
                    viewModel::clearToolbarBadge,
                    viewModel::closeToolbarMenu
                )
            },
            toolbarBadgeCode
        )
        MenuStyleRow(
            "5. Swipeable Carousel Picker Menu",
            "A horizontally scrollable row of sort options acting as a lightweight menu.",
            {
                SwipeableCarouselMenuDemo(state.carouselMenuIndex, viewModel::setCarouselIndex)
            },
            carouselMenuCode
        )
    }
}

@Composable
private fun MenuStyleRow(
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

private val searchableDropdownCode = """
    var expanded by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    Surface(onClick = { expanded = true }) { Text("Jump to page") }
    if (expanded) {
        Column {
            OutlinedTextField(value = query, onValueChange = { query = it })
            items.filter { it.contains(query, ignoreCase = true) }
                .forEach { Text(it, Modifier.clickable { expanded = false }) }
        }
    }
""".trimIndent()

private val iconGridCode = """
    IconButton(onClick = { expanded = true }) { Icon(Icons.Outlined.MoreVert, null) }
    if (expanded) {
        Column {
            actions.chunked(3).forEach { row ->
                Row {
                    row.forEach { action ->
                        Surface(shape = CircleShape) { Icon(action.icon, action.label) }
                    }
                }
            }
        }
    }
""".trimIndent()

private val breadcrumbCode = """
    Row {
        path.forEachIndexed { index, segment ->
            Text(segment, Modifier.clickable { popTo(index) })
            if (index != path.lastIndex) Icon(Icons.Outlined.ChevronRight, null)
        }
    }
    options.forEach { option ->
        Text(option, Modifier.clickable { push(option) })
    }
""".trimIndent()

private val toolbarBadgeCode = """
    BadgedBox(badge = { if (count > 0) Badge { Text("${'$'}count") } }) {
        IconButton(onClick = { expanded = true }) { Icon(Icons.Outlined.MoreVert, null) }
    }
    if (expanded) {
        Column {
            Row(Modifier.clickable { onClear() }) { Text("Clear notifications (${'$'}count)") }
            Row(Modifier.clickable { onDismiss() }) { Text("Settings") }
        }
    }
""".trimIndent()

private val carouselMenuCode = """
    LazyRow {
        items(options.size) { index ->
            Surface(
                onClick = { selected = index },
                color = if (selected == index) primary else surfaceVariant
            ) { Text(options[index]) }
        }
    }
""".trimIndent()
