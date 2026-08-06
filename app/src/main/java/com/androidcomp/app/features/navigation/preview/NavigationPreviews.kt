package com.androidcomp.app.features.navigation.preview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** nav-bottom-bar — real NavigationBar with 3 items, tapping switches local selection state. */
@Composable
fun BottomNavBarPreview() {
    var selected by remember { mutableIntStateOf(0) }
    val items = listOf(
        Triple("Home", Icons.Outlined.Home, 0),
        Triple("Search", Icons.Outlined.Search, 1),
        Triple("Profile", Icons.Outlined.Person, 2)
    )
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        items.forEach { (label, icon, index) ->
            NavigationBarItem(
                selected = selected == index,
                onClick = { selected = index },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}

/** nav-host — static mock diagram of a navigation graph's destination hierarchy. */
@Composable
fun NavHostPreview() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Screen A →")
                Text("  Screen B →")
                Text("    Screen C")
            }
        }
        Text(
            text = "(NavHost defines the graph — this shows the destination hierarchy, not a live demo)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
