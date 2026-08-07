package com.androidcomp.app.features.menus.customstyles

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
fun MenuStylesShowcase(
    state: MenuStylesState,
    onContextMenuSelectionChange: (String) -> Unit,
    onSegmentedSelectionChange: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        MenuStyleRow(
            "1. Bottom Sheet Action Menu",
            "Opens a Material 3 modal bottom sheet listing tappable actions with icons.",
            { BottomSheetActionMenu() },
            bottomSheetActionMenuCode
        )
        MenuStyleRow(
            "2. Context Menu with Icons",
            "A dropdown menu whose items carry leading icons; last pick is shown below the trigger.",
            { ContextMenuWithIcons(selection = state.contextMenuSelection, onSelectionChange = onContextMenuSelectionChange) },
            contextMenuWithIconsCode
        )
        MenuStyleRow(
            "3. Radial/Circular Menu",
            "Tap the FAB to reveal action buttons arranged in an arc, springing into place.",
            { RadialMenu() },
            radialMenuCode
        )
        MenuStyleRow(
            "4. Segmented Dropdown",
            "Trigger shows the current selection; the open menu marks it with a checkmark.",
            { SegmentedDropdown(selected = state.segmentedSelection, onSelectedChange = onSegmentedSelectionChange) },
            segmentedDropdownCode
        )
        MenuStyleRow(
            "5. Expandable Nested Submenu",
            "One item expands inline to reveal sub-options without closing the parent menu.",
            { ExpandableNestedSubmenu() },
            expandableNestedSubmenuCode
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
        Column(Modifier.padding(bottom = 10.dp)) {
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val bottomSheetActionMenuCode = """
    var open by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Button(onClick = { open = true }) { Text("Open Actions") }

    if (open) {
        ModalBottomSheet(onDismissRequest = { open = false }, sheetState = sheetState) {
            Column {
                SheetAction(Icons.Outlined.Share, "Share") { open = false }
                SheetAction(Icons.Outlined.ContentCopy, "Duplicate") { open = false }
                SheetAction(Icons.Outlined.Archive, "Archive") { open = false }
                SheetAction(Icons.Outlined.Delete, "Delete") { open = false }
            }
        }
    }
""".trimIndent()

private val contextMenuWithIconsCode = """
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) { Text("Actions") }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Edit") },
                leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                onClick = { expanded = false; onSelectionChange("Edit") }
            )
            // ...Favorite, Share, Delete
        }
    }
    Text("Last selected: ${'$'}selection")
""".trimIndent()

private val radialMenuCode = """
    var open by remember { mutableStateOf(false) }
    val actions = listOf(Icons.Outlined.Share to "Share", Icons.Outlined.Favorite to "Favorite", ...)

    actions.forEachIndexed { index, (icon, label) ->
        val angleDeg = 180.0 - (index * (180.0 / (actions.size - 1)))
        val targetX = (radius * cos(Math.toRadians(angleDeg))).toFloat()
        val targetY = -(radius * sin(Math.toRadians(angleDeg))).toFloat()
        val offsetX by animateFloatAsState(if (open) targetX else 0f, spring(dampingRatio = 0.65f))
        val offsetY by animateFloatAsState(if (open) targetY else 0f, spring(dampingRatio = 0.65f))

        AnimatedVisibility(
            visible = open,
            enter = scaleIn() + fadeIn(),
            exit = scaleOut() + fadeOut(),
            modifier = Modifier.offset(x = offsetX.dp, y = offsetY.dp)
        ) {
            FloatingActionButton(onClick = { open = false }) { Icon(icon, contentDescription = label) }
        }
    }
    FloatingActionButton(onClick = { open = !open }) { Icon(Icons.Outlined.Add, contentDescription = null) }
""".trimIndent()

private val segmentedDropdownCode = """
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Daily", "Weekly", "Monthly", "Yearly")

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected)
            Icon(Icons.Outlined.ExpandMore, contentDescription = null)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    trailingIcon = { if (option == selected) Icon(Icons.Outlined.Check, contentDescription = null) },
                    onClick = { expanded = false; onSelectedChange(option) }
                )
            }
        }
    }
""".trimIndent()

private val expandableNestedSubmenuCode = """
    var expanded by remember { mutableStateOf(false) }
    var submenuOpen by remember { mutableStateOf(false) }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false; submenuOpen = false }) {
        DropdownMenuItem(text = { Text("Rename") }, onClick = { expanded = false })
        DropdownMenuItem(
            text = { Text("Export as…") },
            trailingIcon = { Icon(if (submenuOpen) Icons.Outlined.Close else Icons.Outlined.ExpandMore, contentDescription = null) },
            onClick = { submenuOpen = !submenuOpen }
        )
        AnimatedVisibility(visible = submenuOpen, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
            Column {
                DropdownMenuItem(text = { Text("PDF") }, onClick = { expanded = false; submenuOpen = false })
                DropdownMenuItem(text = { Text("CSV") }, onClick = { expanded = false; submenuOpen = false })
                DropdownMenuItem(text = { Text("Image") }, onClick = { expanded = false; submenuOpen = false })
            }
        }
        DropdownMenuItem(text = { Text("Delete") }, onClick = { expanded = false })
    }
""".trimIndent()
