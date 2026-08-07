package com.androidcomp.app.features.maps.customstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun MapStylesShowcase(
    state: MapStylesState,
    onTriggerPinDrop: () -> Unit,
    onStyleChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        MapStyleRow(
            "1. Custom Map Pin Marker",
            "A Canvas-drawn pin bounces in with a scale animation; tap to re-trigger the drop.",
            { MapPinMarker(state.pinDropTrigger, onTriggerPinDrop) },
            mapPinMarkerCode
        )
        MapStyleRow(
            "2. Location Card with Distance Badge",
            "Place name and address with a small \"2.3 km away\" distance badge.",
            { LocationDistanceCard() },
            locationDistanceCardCode
        )
        MapStyleRow(
            "3. Route Summary Card",
            "Origin → destination with a dotted-line icon, estimated time, and distance.",
            { RouteSummaryCard() },
            routeSummaryCardCode
        )
        MapStyleRow(
            "4. Map Style Toggle",
            "Segmented control switching Standard/Satellite, changing the placeholder map look.",
            { MapStyleToggle(state.isSatelliteStyle, onStyleChange) },
            mapStyleToggleCode
        )
        MapStyleRow(
            "5. Nearby Places List",
            "Horizontal row of small place cards with icon, name, and rating.",
            { NearbyPlacesRow() },
            nearbyPlacesRowCode
        )
    }
}

@Composable
private fun MapStyleRow(
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
        Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val mapPinMarkerCode = """
    val scale = remember { Animatable(0f) }
    LaunchedEffect(dropTrigger) {
        scale.snapTo(0f)
        scale.animateTo(1f, spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow))
    }
    Box(Modifier.background(Color(0xFFDCEFE3)).clickable { onTap() }) {
        Icon(
            Icons.Outlined.LocationOn,
            null,
            modifier = Modifier.graphicsLayer { scaleX = scale.value; scaleY = scale.value }
        )
    }
""".trimIndent()

private val locationDistanceCardCode = """
    Row(Modifier.background(Color.White, RoundedCornerShape(24.dp)).padding(16.dp)) {
        Column(Modifier.weight(1f)) { Text(placeName); Text(address) }
        Box(Modifier.background(Color(0xFFEAF2FF), RoundedCornerShape(50)).padding(horizontal = 10.dp, vertical = 4.dp)) {
            Text("2.3 km away")
        }
    }
""".trimIndent()

private val routeSummaryCardCode = """
    Column(Modifier.background(Color.White, RoundedCornerShape(24.dp)).padding(16.dp)) {
        Row { Text(origin); Icon(Icons.AutoMirrored.Outlined.TrendingUp, null); Text(destination) }
        Row { Icon(Icons.Outlined.AccessTime, null); Text("12 min · 4.8 km") }
    }
""".trimIndent()

private val mapStyleToggleCode = """
    Row(Modifier.background(Color(0xFFF2F2F2), RoundedCornerShape(50))) {
        listOf("Standard", "Satellite").forEach { label ->
            val selected = (label == "Satellite") == isSatellite
            Box(
                Modifier
                    .background(if (selected) Color.White else Color.Transparent, RoundedCornerShape(50))
                    .clickable { onStyleChange(label == "Satellite") }
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) { Text(label) }
        }
    }
""".trimIndent()

private val nearbyPlacesRowCode = """
    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(places) { place ->
            Column(Modifier.background(Color.White, RoundedCornerShape(16.dp)).padding(12.dp)) {
                Icon(place.icon, null)
                Text(place.name)
                Row { Icon(Icons.Outlined.Star, null); Text(place.rating.toString()) }
            }
        }
    }
""".trimIndent()
