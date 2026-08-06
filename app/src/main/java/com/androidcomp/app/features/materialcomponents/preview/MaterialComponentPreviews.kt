package com.androidcomp.app.features.materialcomponents.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** material-card: a real elevated Card with title and body text. */
@Composable
fun CardPreview() {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Card Title", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Text(
                "This is a real Material 3 Card composable demonstrating body text below a title.",
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/** material-chip: a Row of independently toggleable real FilterChips. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterChipPreview() {
    val labels = listOf("Kotlin", "Compose", "Material3")
    val selectedStates = labels.map { remember { mutableStateOf(false) } }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        labels.forEachIndexed { index, label ->
            var selected by selectedStates[index]
            FilterChip(
                selected = selected,
                onClick = { selected = !selected },
                label = { Text(label) }
            )
        }
    }
}

/** material-badge: a real BadgedBox wrapping an Icon, with an Increment button. */
@Composable
fun BadgePreview() {
    var count by remember { mutableIntStateOf(3) }

    Column {
        BadgedBox(
            badge = { Badge { Text("$count") } }
        ) {
            Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
        }
        Button(onClick = { count++ }, modifier = Modifier.padding(top = 12.dp)) {
            Text("Increment")
        }
    }
}
