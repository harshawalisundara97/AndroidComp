package com.androidcomp.app.features.navigation.customstyles

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/** 1. Morphing Indicator Bottom Nav — a pill indicator glides between the selected items. */
@Composable
fun MorphingIndicatorBottomNav(selectedIndex: Int, onSelect: (Int) -> Unit) {
    val icons = listOf(Icons.Outlined.Home, Icons.Outlined.Search, Icons.Outlined.Person)

    BoxWithConstraints(
        Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clip(RoundedCornerShape(26.dp))
            .background(Color(0xFFF4F5F7))
    ) {
        val itemWidth = maxWidth / icons.size
        val indicatorOffset by animateDpAsState(
            itemWidth * selectedIndex,
            animationSpec = spring(dampingRatio = 0.7f),
            label = "morphingIndicatorOffset"
        )
        Box(
            Modifier
                .width(itemWidth)
                .height(64.dp)
                .graphicsLayer { translationX = indicatorOffset.toPx() }
                .padding(8.dp)
                .clip(RoundedCornerShape(50))
                .background(Color(0xFF2F6FED))
        )
        Row(Modifier.fillMaxWidth()) {
            icons.forEachIndexed { index, icon ->
                val tint by animateColorAsState(
                    if (selectedIndex == index) Color.White else Color(0xFF6B7280),
                    label = "morphingIconTint"
                )
                Box(
                    Modifier
                        .width(itemWidth)
                        .height(64.dp)
                        .clickable { onSelect(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = tint)
                }
            }
        }
    }
}

/** 2. Sliding Underline Tabs — a colored underline slides to the selected tab. */
@Composable
fun SlidingUnderlineTabs(selectedIndex: Int, onSelect: (Int) -> Unit) {
    val labels = listOf("Overview", "Activity", "Settings")

    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val itemWidth = maxWidth / labels.size
        val underlineOffset by animateDpAsState(itemWidth * selectedIndex, label = "underlineOffset")

        Column(Modifier.fillMaxWidth()) {
            Row(Modifier.fillMaxWidth()) {
                labels.forEachIndexed { index, label ->
                    val color by animateColorAsState(
                        if (selectedIndex == index) Color(0xFF2F6FED) else Color(0xFF6B7280),
                        label = "underlineTabColor"
                    )
                    Box(
                        Modifier
                            .width(itemWidth)
                            .clickable { onSelect(index) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(label, color = color)
                    }
                }
            }
            Box(
                Modifier
                    .width(itemWidth)
                    .height(3.dp)
                    .graphicsLayer { translationX = underlineOffset.toPx() }
                    .background(Color(0xFF2F6FED), RoundedCornerShape(50))
            )
        }
    }
}

/** 3. Segmented Control — an animated background slides behind the selected option. */
@Composable
fun SegmentedControlNav(selectedIndex: Int, onSelect: (Int) -> Unit) {
    val labels = listOf("Day", "Week", "Month")

    BoxWithConstraints(
        Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFF4F5F7))
            .padding(4.dp)
    ) {
        val itemWidth = maxWidth / labels.size
        val segmentOffset by animateDpAsState(itemWidth * selectedIndex, label = "segmentOffset")

        Box(
            Modifier
                .width(itemWidth)
                .height(36.dp)
                .graphicsLayer { translationX = segmentOffset.toPx() }
                .clip(RoundedCornerShape(50))
                .background(Color.White)
        )
        Row(Modifier.fillMaxWidth()) {
            labels.forEachIndexed { index, label ->
                val color by animateColorAsState(
                    if (selectedIndex == index) Color(0xFF1F2937) else Color(0xFF9CA3AF),
                    label = "segmentTextColor"
                )
                Box(
                    Modifier
                        .width(itemWidth)
                        .height(36.dp)
                        .clickable { onSelect(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(label, color = color)
                }
            }
        }
    }
}

/** 4. Floating Pill Nav — compact icon-only bar; the selected item grows with a highlight. */
@Composable
fun FloatingPillNav(selectedIndex: Int, onSelect: (Int) -> Unit) {
    val icons = listOf(Icons.Outlined.Home, Icons.Outlined.Favorite, Icons.Outlined.Notifications)

    Row(
        Modifier
            .clip(RoundedCornerShape(50))
            .background(Color(0xFF1F2937))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icons.forEachIndexed { index, icon ->
            val selected = selectedIndex == index
            val scale by animateFloatAsState(if (selected) 1.15f else 1f, label = "pillScale")
            val bgColor by animateColorAsState(
                if (selected) Color(0xFF2F6FED) else Color.Transparent,
                label = "pillBg"
            )
            Box(
                Modifier
                    .padding(horizontal = 4.dp)
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .clip(CircleShape)
                    .background(bgColor)
                    .clickable { onSelect(index) }
                    .size(44.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
        }
    }
}

/** 5. Rail Navigation — a vertical narrow rail for tablet/landscape-style navigation. */
@Composable
fun RailNavigation(selectedIndex: Int, onSelect: (Int) -> Unit) {
    data class RailDestination(val icon: ImageVector, val label: String)
    val destinations = listOf(
        RailDestination(Icons.Outlined.Home, "Home"),
        RailDestination(Icons.Outlined.Search, "Explore"),
        RailDestination(Icons.Outlined.Notifications, "Alerts"),
        RailDestination(Icons.Outlined.Settings, "Settings")
    )

    Column(
        Modifier
            .width(72.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFF4F5F7))
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        destinations.forEachIndexed { index, destination ->
            val selected = selectedIndex == index
            val bgColor by animateColorAsState(
                if (selected) Color(0xFF2F6FED).copy(alpha = 0.12f) else Color.Transparent,
                label = "railBg"
            )
            val tint by animateColorAsState(
                if (selected) Color(0xFF2F6FED) else Color(0xFF6B7280),
                label = "railTint"
            )
            Column(
                Modifier
                    .padding(vertical = 6.dp)
                    .width(56.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(bgColor)
                    .clickable { onSelect(index) }
                    .padding(vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(destination.icon, contentDescription = destination.label, tint = tint)
                Text(destination.label, color = tint, fontSize = androidx.compose.ui.unit.TextUnit(10f, androidx.compose.ui.unit.TextUnitType.Sp))
            }
        }
    }
}
