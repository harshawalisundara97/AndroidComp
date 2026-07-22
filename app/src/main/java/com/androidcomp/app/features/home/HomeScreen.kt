package com.androidcomp.app.features.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.theme.AppRadii
import com.androidcomp.app.core.ui.theme.LocalAppColors
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.items

private data class FeatureBanner(val title: String, val subtitle: String, val tint: Color)
private data class NewsItem(val title: String, val snippet: String, val tint: Color)
private data class CategoryTile(val label: String, val icon: ImageVector, val tint: Color)

@Composable
fun HomeScreen(onComponentClick: (String) -> Unit, onViewAllCategoriesClick: () -> Unit) {
    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
        ) {
            item { HomeHeader() }
            item { FeatureBannerRow() }
            item { NewsSection(onViewAllCategoriesClick) }
            item { CategoryGrid(onViewAllCategoriesClick) }
        }
    }
}

@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { }) {
                Icon(Icons.Outlined.Notifications, contentDescription = "Notifications")
            }
            Column {
                Text(
                    "Hi,",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    "Developer",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                    .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Outlined.Person,
                contentDescription = "Profile",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


private val featureBanners = listOf(
    FeatureBanner("New: Buttons Module", "6 Material 3 button variants with a live playground", Color(0xFF2F6FED)),
    FeatureBanner("Text Module Live", "Explore all 5 typography roles interactively", Color(0xFF2E9E5B)),
    FeatureBanner("More Coming Soon", "Text Inputs, Layouts, and more categories on the way", Color(0xFFE58A2E))
)

@Composable
private fun FeatureBannerRow() {
    LazyRow(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(featureBanners) { banner ->
            Box(
                modifier = Modifier
                    .width(260.dp)
                    .aspectRatio(1.8f)
                    .clip(RoundedCornerShape(AppRadii.card))
                    .background(banner.tint),
                contentAlignment = Alignment.BottomStart
            ) {
                Column(Modifier.padding(20.dp)) {
                    Text(
                        banner.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                    Text(
                        banner.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }
        }
    }
}

// Headlines from developer.android.com (as of this app's last content refresh) —
// static snapshot, not a live feed.
private val newsItems = listOf(
    NewsItem(
        "Android 17 is here",
        "Officially released with AOSP source — top 5 updates to prep your app for API 37",
        Color(0xFF2F6FED)
    ),
    NewsItem(
        "Dive into Android XR",
        "Expanded engine support and new resources for building and scaling XR experiences",
        Color(0xFF2E9E5B)
    ),
    NewsItem(
        "Elevating AI assistance",
        "Android Bench adds updated LLM benchmarking methodology and new benchmarked models",
        Color(0xFFE58A2E)
    )
)

@Composable
private fun NewsSection(onViewMoreClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Android News",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            TextButton(onClick = onViewMoreClick) {
                Text("View more")
            }
        }
        LazyRow(
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(newsItems) { news ->
                val border = LocalAppColors.current.border
                OutlinedCard(
                    shape = RoundedCornerShape(AppRadii.card),
                    border = BorderStroke(1.dp, border),
                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.outlinedCardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.width(220.dp)
                ) {
                    Column {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(news.tint.copy(alpha = 0.15f))
                        )
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                news.title,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                news.snippet,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

private val categoryTiles = listOf(
    CategoryTile("Buttons", Icons.Outlined.TouchApp, Color(0xFF2F6FED)),
    CategoryTile("Text", Icons.Outlined.TextFields, Color(0xFF2E9E5B)),
    CategoryTile("Text Inputs", Icons.Outlined.Edit, Color(0xFFE58A2E)),
    CategoryTile("Images", Icons.Outlined.Image, Color(0xFF2F6FED)),
    CategoryTile("Layouts", Icons.Outlined.Dashboard, Color(0xFF2E9E5B)),
    CategoryTile("Lists", Icons.AutoMirrored.Outlined.List, Color(0xFFE58A2E))
)

@Composable
private fun CategoryGrid(onCategoryClick: () -> Unit) {
    Column(Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
        Text(
            "Explore by Category",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(340.dp)
        ) {
            items(categoryTiles) { tile ->
                val border = LocalAppColors.current.border
                OutlinedCard(
                    shape = RoundedCornerShape(AppRadii.card),
                    border = BorderStroke(1.dp, border),
                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.outlinedCardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.clickable { onCategoryClick() }
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Box(
                            Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(tile.tint.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(tile.icon, contentDescription = null, tint = tile.tint)
                        }
                        Text(
                            tile.label,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    }
}
