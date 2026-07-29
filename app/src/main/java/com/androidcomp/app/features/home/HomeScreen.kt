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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Gesture
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Widgets
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
import com.androidcomp.app.core.ui.AppBadge
import com.androidcomp.app.features.news.NewsCatalog
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.lazy.items

private data class FeatureBanner(val title: String, val subtitle: String, val tint: Color)
private data class CategoryTile(val label: String, val icon: ImageVector, val tint: Color, val isNew: Boolean = false)

@Composable
fun HomeScreen(
    onComponentClick: (String) -> Unit,
    onViewAllCategoriesClick: () -> Unit,
    onNewsClick: (String) -> Unit
) {
    Scaffold { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 8.dp)
        ) {
            item { HomeHeader() }
            item { FeatureBannerRow() }
            item { NewsSection(onViewAllCategoriesClick, onNewsClick) }
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
    FeatureBanner("Text Module Live", "Browse Text components — headings, body styles, and more", Color(0xFF2E9E5B)),
    FeatureBanner("24 Categories Live", "Every Android Comp category now has real, browsable content", Color(0xFFE58A2E))
)

@Composable
private fun FeatureBannerRow() {
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()

    androidx.compose.runtime.LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(3000)
            val nextIndex = (listState.firstVisibleItemIndex + 1) % featureBanners.size
            listState.animateScrollToItem(nextIndex)
        }
    }

    LazyRow(
        state = listState,
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

@Composable
private fun NewsSection(onViewMoreClick: () -> Unit, onNewsClick: (String) -> Unit) {
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
            items(NewsCatalog.all) { news ->
                val border = LocalAppColors.current.border
                OutlinedCard(
                    shape = RoundedCornerShape(AppRadii.card),
                    border = BorderStroke(1.dp, border),
                    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.outlinedCardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .width(220.dp)
                        .clickable { onNewsClick(news.id) }
                ) {
                    Column {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .background(news.tint.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                news.icon,
                                contentDescription = null,
                                tint = news.tint,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Column(Modifier.padding(16.dp)) {
                            if (news.isNew) {
                                AppBadge("NEW", containerColor = news.tint)
                            }
                            Text(
                                news.title,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = if (news.isNew) 6.dp else 0.dp)
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
    CategoryTile("Text", Icons.Outlined.TextFields, Color(0xFF2E9E5B), isNew = true),
    CategoryTile("Text Inputs", Icons.Outlined.Edit, Color(0xFFE58A2E)),
    CategoryTile("Images", Icons.Outlined.Image, Color(0xFF2F6FED)),
    CategoryTile("Layouts", Icons.Outlined.Dashboard, Color(0xFF2E9E5B)),
    CategoryTile("Lists", Icons.AutoMirrored.Outlined.List, Color(0xFFE58A2E)),
    CategoryTile("Navigation", Icons.Outlined.Navigation, Color(0xFF2F6FED)),
    CategoryTile("Dialogs", Icons.AutoMirrored.Outlined.Chat, Color(0xFF2E9E5B)),
    CategoryTile("Menus", Icons.Outlined.Menu, Color(0xFFE58A2E)),
    CategoryTile("Material Components", Icons.Outlined.Widgets, Color(0xFF2F6FED)),
    CategoryTile("Progress", Icons.Outlined.HourglassEmpty, Color(0xFF2E9E5B)),
    CategoryTile("Selection Controls", Icons.Outlined.CheckBox, Color(0xFFE58A2E)),
    CategoryTile("Sliders", Icons.Outlined.Tune, Color(0xFF2F6FED)),
    CategoryTile("Gestures", Icons.Outlined.Gesture, Color(0xFF2E9E5B)),
    CategoryTile("Animations", Icons.Outlined.Animation, Color(0xFFE58A2E)),
    CategoryTile("Graphics", Icons.Outlined.Brush, Color(0xFF2F6FED)),
    CategoryTile("Camera", Icons.Outlined.CameraAlt, Color(0xFF2E9E5B)),
    CategoryTile("Media", Icons.Outlined.Movie, Color(0xFFE58A2E)),
    CategoryTile("Notifications", Icons.Outlined.Notifications, Color(0xFF2F6FED)),
    CategoryTile("Permissions", Icons.Outlined.Lock, Color(0xFF2E9E5B)),
    CategoryTile("Storage", Icons.Outlined.Storage, Color(0xFFE58A2E)),
    CategoryTile("Networking", Icons.Outlined.Cloud, Color(0xFF2F6FED)),
    CategoryTile("Maps", Icons.Outlined.Map, Color(0xFF2E9E5B)),
    CategoryTile("Sensors", Icons.Outlined.Sensors, Color(0xFFE58A2E))
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
        categoryTiles.chunked(2).forEach { rowTiles ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowTiles.forEach { tile ->
                    CategoryTileCard(tile, onCategoryClick, Modifier.weight(1f))
                }
                if (rowTiles.size == 1) {
                    Box(Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun CategoryTileCard(tile: CategoryTile, onCategoryClick: () -> Unit, modifier: Modifier = Modifier) {
    val border = LocalAppColors.current.border
    OutlinedCard(
        shape = RoundedCornerShape(AppRadii.card),
        border = BorderStroke(1.dp, border),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 0.dp),
        modifier = modifier.clickable { onCategoryClick() }
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
            if (tile.isNew) {
                AppBadge(
                    "NEW",
                    containerColor = tile.tint,
                    modifier = Modifier.padding(top = 8.dp)
                )
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
