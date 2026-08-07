package com.androidcomp.app.features.menus.customstyles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

/** 1. Bottom Sheet Action Menu — button opens a Material 3 ModalBottomSheet of actions. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetActionMenu() {
    var open by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Button(onClick = { open = true }) {
        Text("Open Actions")
    }

    if (open) {
        ModalBottomSheet(onDismissRequest = { open = false }, sheetState = sheetState) {
            Column(Modifier.padding(bottom = 24.dp)) {
                SheetAction(Icons.Outlined.Share, "Share") { open = false }
                SheetAction(Icons.Outlined.ContentCopy, "Duplicate") { open = false }
                SheetAction(Icons.Outlined.Archive, "Archive") { open = false }
                SheetAction(Icons.Outlined.Delete, "Delete") { open = false }
            }
        }
    }
}

@Composable
private fun SheetAction(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(label, modifier = Modifier.padding(start = 16.dp), color = MaterialTheme.colorScheme.onSurface)
    }
}

/** 2. Context Menu with Icons — DropdownMenu whose items have leading icons; shows last pick below. */
@Composable
fun ContextMenuWithIcons(selection: String?, onSelectionChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.Start) {
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Icon(Icons.AutoMirrored.Outlined.List, contentDescription = null, modifier = Modifier.size(18.dp))
                Text("Actions", modifier = Modifier.padding(start = 8.dp))
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    text = { Text("Edit") },
                    leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
                    onClick = { expanded = false; onSelectionChange("Edit") }
                )
                DropdownMenuItem(
                    text = { Text("Favorite") },
                    leadingIcon = { Icon(Icons.Outlined.Favorite, contentDescription = null) },
                    onClick = { expanded = false; onSelectionChange("Favorite") }
                )
                DropdownMenuItem(
                    text = { Text("Share") },
                    leadingIcon = { Icon(Icons.Outlined.Share, contentDescription = null) },
                    onClick = { expanded = false; onSelectionChange("Share") }
                )
                DropdownMenuItem(
                    text = { Text("Delete") },
                    leadingIcon = { Icon(Icons.Outlined.Delete, contentDescription = null) },
                    onClick = { expanded = false; onSelectionChange("Delete") }
                )
            }
        }
        AnimatedVisibility(visible = selection != null) {
            Text(
                "Last selected: ${selection.orEmpty()}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/** 3. Radial/Circular Menu — a FAB that reveals action buttons arranged in an arc when tapped. */
@Composable
fun RadialMenu() {
    var open by remember { mutableStateOf(false) }
    val actions = listOf(
        Icons.Outlined.Share to "Share",
        Icons.Outlined.Favorite to "Favorite",
        Icons.Outlined.Edit to "Edit",
        Icons.Outlined.Delete to "Delete"
    )
    val rotation by animateFloatAsState(if (open) 45f else 0f, label = "radialFabRotation")

    Box(modifier = Modifier.height(180.dp).fillMaxWidth(), contentAlignment = Alignment.BottomCenter) {
        actions.forEachIndexed { index, (icon, label) ->
            // spread across a 180-degree arc above the FAB
            val angleDeg = 180.0 - (index * (180.0 / (actions.size - 1)))
            val radius = 90f
            val targetX = (radius * cos(Math.toRadians(angleDeg))).toFloat()
            val targetY = -(radius * sin(Math.toRadians(angleDeg))).toFloat()
            val offsetX by animateFloatAsState(if (open) targetX else 0f, spring(dampingRatio = 0.65f), label = "radialX$index")
            val offsetY by animateFloatAsState(if (open) targetY else 0f, spring(dampingRatio = 0.65f), label = "radialY$index")

            AnimatedVisibility(
                visible = open,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier.offset(x = offsetX.dp, y = offsetY.dp)
            ) {
                FloatingActionButton(
                    onClick = { open = false },
                    modifier = Modifier.size(44.dp),
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(icon, contentDescription = label, modifier = Modifier.size(18.dp))
                }
            }
        }
        FloatingActionButton(onClick = { open = !open }) {
            Icon(
                Icons.Outlined.Add,
                contentDescription = if (open) "Close menu" else "Open menu",
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer { rotationZ = rotation }
            )
        }
    }
}

/** 4. Segmented Dropdown — trigger shows current selection; menu marks it with a checkmark. */
@Composable
fun SegmentedDropdown(selected: String, onSelectedChange: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val options = listOf("Daily", "Weekly", "Monthly", "Yearly")

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(selected)
            Icon(Icons.Outlined.ExpandMore, contentDescription = null, modifier = Modifier.padding(start = 6.dp).size(18.dp))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    trailingIcon = {
                        if (option == selected) {
                            Icon(Icons.Outlined.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
                        }
                    },
                    onClick = { expanded = false; onSelectedChange(option) }
                )
            }
        }
    }
}

/** 5. Expandable Nested Submenu — tapping "Export as…" reveals sub-options inline without closing the menu. */
@Composable
fun ExpandableNestedSubmenu() {
    var expanded by remember { mutableStateOf(false) }
    var submenuOpen by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text("Options")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false; submenuOpen = false }) {
            DropdownMenuItem(text = { Text("Rename") }, onClick = { expanded = false })
            DropdownMenuItem(
                text = { Text("Export as…") },
                trailingIcon = {
                    Icon(
                        if (submenuOpen) Icons.Outlined.Close else Icons.Outlined.ExpandMore,
                        contentDescription = null
                    )
                },
                onClick = { submenuOpen = !submenuOpen }
            )
            AnimatedVisibility(
                visible = submenuOpen,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(Modifier.padding(start = 16.dp)) {
                    DropdownMenuItem(text = { Text("PDF") }, onClick = { expanded = false; submenuOpen = false })
                    DropdownMenuItem(text = { Text("CSV") }, onClick = { expanded = false; submenuOpen = false })
                    DropdownMenuItem(text = { Text("Image") }, onClick = { expanded = false; submenuOpen = false })
                }
            }
            DropdownMenuItem(text = { Text("Delete") }, onClick = { expanded = false })
        }
    }
}
