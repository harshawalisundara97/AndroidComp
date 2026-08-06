package com.androidcomp.app.features.lists.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val chipColors = listOf(
    Color(0xFFBBDEFB), Color(0xFFC8E6C9), Color(0xFFFFE0B2), Color(0xFFF8BBD0),
    Color(0xFFD1C4E9), Color(0xFFB2EBF2), Color(0xFFFFF9C4), Color(0xFFCFD8DC)
)

/** list-lazy-column — scrollable LazyColumn with 8 simple rows. */
@Composable
fun LazyColumnPreview() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        items(8) { index ->
            ListItem(headlineContent = { Text("Item ${index + 1}") })
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}

/** list-lazy-row — horizontally scrollable LazyRow with 8 chip-like boxes. */
@Composable
fun LazyRowPreview() {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
    ) {
        items(8) { index ->
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(chipColors[index % chipColors.size], RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("${index + 1}")
            }
        }
    }
}
