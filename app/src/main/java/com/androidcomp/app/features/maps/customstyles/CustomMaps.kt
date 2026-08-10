package com.androidcomp.app.features.maps.customstyles

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * 1. Custom Map Pin Marker — colored placeholder map background with a Canvas-drawn teardrop
 * pin that bounces in with a scale spring animation. Tap the map to re-trigger the drop.
 */
@Composable
fun MapPinMarker(dropTrigger: Int, onTap: () -> Unit) {
    var animateIn by remember { mutableStateOf(false) }

    LaunchedEffect(dropTrigger) {
        animateIn = false
        animateIn = true
    }

    val pinScale by animateFloatAsState(
        targetValue = if (animateIn) 1f else 0f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "pinScale"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFD8E8DD))
            .clickable { onTap() },
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.foundation.Canvas(
            modifier = Modifier
                .size(36.dp, 46.dp)
                .graphicsLayer {
                    scaleX = pinScale
                    scaleY = pinScale
                    transformOrigin = TransformOrigin(0.5f, 1f)
                }
        ) {
            val w = size.width
            val h = size.height
            val path = Path().apply {
                moveTo(w / 2f, h)
                cubicTo(w * 0.05f, h * 0.55f, 0f, h * 0.35f, w / 2f, 0f)
                cubicTo(w, h * 0.35f, w * 0.95f, h * 0.55f, w / 2f, h)
                close()
            }
            drawPath(path, color = Color(0xFFE53935))
            drawCircle(color = Color.White, radius = w * 0.16f, center = Offset(w / 2f, h * 0.34f))
        }
        Text(
            "Tap map to drop pin",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF4A5C50),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        )
    }
}

/**
 * 2. Location Card with Distance Badge — place name, address, and a small distance badge.
 */
@Composable
fun LocationDistanceCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFE3F2FD), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = Color(0xFF1E88E5))
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Blue Bottle Coffee", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                    Text(
                        "300 Webster St, Oakland, CA",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Box(
                modifier = Modifier
                    .background(Color(0xFFE8F5E9), RoundedCornerShape(50))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text("2.3 km away", style = MaterialTheme.typography.labelSmall, color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
            }
        }
    }
}

/**
 * 3. Route Summary Card — origin -> destination with a dotted route icon, ETA, and distance.
 */
@Composable
fun RouteSummaryCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(Modifier.size(10.dp).background(Color(0xFF1E88E5), CircleShape))
                    androidx.compose.foundation.Canvas(Modifier.width(2.dp).height(28.dp)) {
                        val dashCount = 4
                        val gap = size.height / (dashCount * 2 - 1)
                        for (i in 0 until dashCount) {
                            drawLine(
                                color = Color(0xFFB0BEC5),
                                start = Offset(size.width / 2f, i * 2 * gap),
                                end = Offset(size.width / 2f, i * 2 * gap + gap),
                                strokeWidth = size.width
                            )
                        }
                    }
                    Box(Modifier.size(10.dp).background(Color(0xFFE53935), CircleShape))
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("Home", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(22.dp))
                    Text("Downtown Office", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Outlined.AccessTime, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("18 min", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.width(16.dp))
                Icon(Icons.AutoMirrored.Outlined.TrendingUp, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(4.dp))
                Text("6.4 km", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * 4. Map Style Toggle — segmented control switching between Standard and Satellite,
 * changing the placeholder map background to simulate the style change.
 */
@Composable
fun MapStyleToggle(isSatellite: Boolean, onStyleChange: (Boolean) -> Unit) {
    val backgroundColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSatellite) Color(0xFF263238) else Color(0xFFD8E8DD),
        animationSpec = tween(350),
        label = "mapStyleBackground"
    )
    val indicatorOffset by animateDpAsState(
        targetValue = if (isSatellite) 92.dp else 0.dp,
        animationSpec = tween(250),
        label = "segmentedIndicator"
    )

    Column(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(backgroundColor)
        ) {
            if (isSatellite) {
                Box(Modifier.size(60.dp, 40.dp).align(Alignment.TopStart).padding(12.dp).background(Color(0xFF37474F), RoundedCornerShape(6.dp)))
                Box(Modifier.size(50.dp, 30.dp).align(Alignment.BottomEnd).padding(12.dp).background(Color(0xFF3E5058), RoundedCornerShape(6.dp)))
            } else {
                Box(Modifier.size(70.dp, 8.dp).align(Alignment.Center).background(Color(0xFFFFFFFF).copy(alpha = 0.6f), RoundedCornerShape(4.dp)))
            }
        }
        Spacer(Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .width(184.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(50))
                .padding(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .offset(x = indicatorOffset)
                    .width(92.dp)
                    .height(32.dp)
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(50))
            )
            Row(Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .width(92.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable { onStyleChange(false) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Standard", style = MaterialTheme.typography.labelMedium, fontWeight = if (!isSatellite) FontWeight.SemiBold else FontWeight.Normal)
                }
                Box(
                    modifier = Modifier
                        .width(92.dp)
                        .height(32.dp)
                        .clip(RoundedCornerShape(50))
                        .clickable { onStyleChange(true) },
                    contentAlignment = Alignment.Center
                ) {
                    Text("Satellite", style = MaterialTheme.typography.labelMedium, fontWeight = if (isSatellite) FontWeight.SemiBold else FontWeight.Normal)
                }
            }
        }
    }
}

private data class NearbyPlace(val name: String, val rating: String, val icon: ImageVector, val tint: Color)

/**
 * 5. Nearby Places List — horizontal scrollable row of small place cards.
 */
@Composable
fun NearbyPlacesRow() {
    val places = remember {
        listOf(
            NearbyPlace("The Grind Cafe", "4.6", Icons.Outlined.LocalCafe, Color(0xFF6D4C41)),
            NearbyPlace("Olive & Basil", "4.4", Icons.Outlined.Restaurant, Color(0xFFE65100)),
            NearbyPlace("Riverside Park", "4.8", Icons.Outlined.Park, Color(0xFF2E7D32)),
            NearbyPlace("QuickFuel Station", "4.1", Icons.Outlined.LocalGasStation, Color(0xFF1565C0)),
            NearbyPlace("Market Square", "4.3", Icons.Outlined.ShoppingBag, Color(0xFF6A1B9A))
        )
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        places.forEach { place ->
            Column(
                modifier = Modifier
                    .width(110.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                    .padding(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(place.tint.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(place.icon, contentDescription = null, tint = place.tint, modifier = Modifier.size(18.dp))
                }
                Spacer(Modifier.height(8.dp))
                Text(place.name, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Medium, maxLines = 2)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.Star, contentDescription = null, tint = Color(0xFFFFB300), modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(2.dp))
                    Text(place.rating, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
