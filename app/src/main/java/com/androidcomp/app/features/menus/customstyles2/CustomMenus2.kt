package com.androidcomp.app.features.menus.customstyles2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Archive
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/** 1. Searchable Dropdown Menu */
@Composable
fun SearchableDropdownMenuDemo(
    expanded: Boolean,
    query: String,
    onToggle: () -> Unit,
    onQueryChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val allItems = listOf("Dashboard", "Analytics", "Reports", "Settings", "Notifications", "Billing", "Team")
    val filtered = allItems.filter { it.contains(query, ignoreCase = true) }

    Column {
        Surface(
            onClick = onToggle,
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
                Text("Jump to page", style = MaterialTheme.typography.bodyMedium)
            }
        }
        AnimatedVisibility(visible = expanded, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
            Card(
                Modifier.fillMaxWidth().padding(top = 8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(Modifier.padding(8.dp)) {
                    OutlinedTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        placeholder = { Text("Search pages...") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(4.dp)
                    )
                    filtered.forEach { item ->
                        Text(
                            item,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onDismiss() }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        )
                    }
                    if (filtered.isEmpty()) {
                        Text(
                            "No matches",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}

/** 2. Icon-Grid Quick Actions Menu */
@Composable
fun IconGridQuickActionsMenuDemo(
    expanded: Boolean,
    onToggle: () -> Unit,
    onDismiss: () -> Unit
) {
    data class Action(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
    val actions = listOf(
        Action("Share", Icons.Outlined.Share),
        Action("Star", Icons.Outlined.Star),
        Action("Archive", Icons.Outlined.Archive),
        Action("Delete", Icons.Outlined.Delete),
        Action("Alerts", Icons.Outlined.Notifications),
        Action("Settings", Icons.Outlined.Settings)
    )
    Column {
        IconButton(onClick = onToggle) {
            Icon(Icons.Outlined.MoreVert, contentDescription = "Quick actions")
        }
        AnimatedVisibility(visible = expanded, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
            Card(shape = RoundedCornerShape(20.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                Column(Modifier.padding(16.dp).width(220.dp)) {
                    actions.chunked(3).forEach { rowActions ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            rowActions.forEach { action ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable { onDismiss() }.padding(8.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primaryContainer,
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(action.icon, contentDescription = action.label, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                                        }
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Text(action.label, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** 3. Cascading Breadcrumb Menu */
@Composable
fun CascadingBreadcrumbMenuDemo(
    path: List<String>,
    onPush: (String) -> Unit,
    onPopTo: (Int) -> Unit
) {
    val nextOptions = mapOf(
        "Home" to listOf("Products", "Orders", "Customers"),
        "Products" to listOf("Electronics", "Apparel", "Home Goods"),
        "Orders" to listOf("Pending", "Shipped", "Cancelled"),
        "Customers" to listOf("Active", "Inactive"),
        "Electronics" to listOf("Phones", "Laptops"),
        "Apparel" to listOf("Men", "Women"),
        "Home Goods" to listOf("Kitchen", "Decor"),
        "Pending" to emptyList(),
        "Shipped" to emptyList(),
        "Cancelled" to emptyList(),
        "Active" to emptyList(),
        "Inactive" to emptyList(),
        "Phones" to emptyList(),
        "Laptops" to emptyList(),
        "Men" to emptyList(),
        "Women" to emptyList(),
        "Kitchen" to emptyList(),
        "Decor" to emptyList()
    )
    val current = path.last()
    val options = nextOptions[current].orEmpty()

    Column(Modifier.animateContentSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            path.forEachIndexed { index, segment ->
                Text(
                    segment,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (index == path.lastIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.clickable { onPopTo(index) }
                )
                if (index != path.lastIndex) {
                    Icon(Icons.Outlined.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        if (options.isNotEmpty()) {
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(Modifier.padding(8.dp)) {
                    options.forEach { option ->
                        Text(
                            option,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onPush(option) }
                                .padding(horizontal = 12.dp, vertical = 10.dp)
                        )
                    }
                }
            }
        } else {
            Text(
                "End of path — no further options",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/** 4. Toolbar Overflow Menu with Badge */
@Composable
fun ToolbarOverflowBadgeMenuDemo(
    expanded: Boolean,
    badgeCount: Int,
    onToggle: () -> Unit,
    onClear: () -> Unit,
    onDismiss: () -> Unit
) {
    Column {
        BadgedBox(badge = { if (badgeCount > 0) Badge { Text(badgeCount.toString()) } }) {
            IconButton(onClick = onToggle) {
                Icon(Icons.Outlined.MoreVert, contentDescription = "Overflow menu")
            }
        }
        AnimatedVisibility(visible = expanded, enter = fadeIn() + expandVertically(), exit = fadeOut() + shrinkVertically()) {
            Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
                Column(Modifier.padding(4.dp).width(220.dp)) {
                    Row(
                        Modifier.fillMaxWidth().clickable(enabled = badgeCount > 0, onClick = onClear).padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Clear notifications ($badgeCount)", style = MaterialTheme.typography.bodyMedium)
                        Icon(Icons.Outlined.Notifications, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                    Row(
                        Modifier.fillMaxWidth().clickable(onClick = onDismiss).padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Settings", style = MaterialTheme.typography.bodyMedium)
                        Icon(Icons.Outlined.Settings, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

/** 5. Swipeable Carousel Picker Menu */
@Composable
fun SwipeableCarouselMenuDemo(
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    val options = listOf("Newest", "Popular", "Price: Low", "Price: High", "Rating")
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        items(options.size) { index ->
            val selected = index == selectedIndex
            Surface(
                onClick = { onSelect(index) },
                shape = RoundedCornerShape(18.dp),
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.animateContentSize()
            ) {
                Text(
                    options[index],
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                )
            }
        }
    }
}
