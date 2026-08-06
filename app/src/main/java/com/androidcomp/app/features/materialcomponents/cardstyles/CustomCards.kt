package com.androidcomp.app.features.materialcomponents.cardstyles

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.automirrored.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 1. Stat Card — big number, label, trend indicator; tap to simulate an updated value. */
@Composable
fun StatCard(value: Int, onIncrement: () -> Unit) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF5F6FA))
            .clickable { onIncrement() }
            .padding(20.dp)
    ) {
        Column {
            Text("Total Downloads", fontSize = 13.sp, color = Color(0xFF6B7280))
            AnimatedContent(targetState = value, label = "statValue") { v ->
                Text("$v", fontSize = 28.sp, color = Color(0xFF111827))
            }
            Row {
                Icon(Icons.AutoMirrored.Outlined.TrendingUp, contentDescription = null, tint = Color(0xFF2ECC71), modifier = Modifier.size(16.dp))
                Text(" +12% this week", fontSize = 12.sp, color = Color(0xFF2ECC71))
            }
        }
    }
}

/** 2. Image Card — header image placeholder with a toggleable bookmark. */
@Composable
fun ImageCard(saved: Boolean, onToggleSaved: () -> Unit) {
    Box(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFFFFFF))
            .border(1.dp, Color(0xFFEDEEF2), RoundedCornerShape(24.dp))
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(Color(0xFFE0E7FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Image, contentDescription = null, tint = Color(0xFF6366F1), modifier = Modifier.size(36.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth().padding(14.dp),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Mountain Sunrise", fontSize = 14.sp, color = Color(0xFF111827))
                IconButton(onClick = onToggleSaved) {
                    Icon(
                        if (saved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,
                        contentDescription = if (saved) "Saved" else "Save",
                        tint = if (saved) Color(0xFF6366F1) else Color(0xFF9CA3AF)
                    )
                }
            }
        }
    }
}

/** 3. Gradient Card — vibrant gradient surface, subtle press glow. */
@Composable
fun GradientCard(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val glowElevation by animateDpAsState(if (pressed) 14.dp else 4.dp, label = "gradientCardGlow")

    Box(
        modifier = Modifier
            .width(220.dp)
            .shadow(glowElevation, RoundedCornerShape(24.dp), spotColor = Color(0xFF8B5CF6))
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFFEC4899))))
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(20.dp)
    ) {
        Column {
            Text("Premium Plan", fontSize = 16.sp, color = Color.White)
            Text("Unlock all features", fontSize = 13.sp, color = Color.White.copy(alpha = 0.85f))
        }
    }
}

/** 4. Minimal Bordered Card — flat, thin border, list-item style row. */
@Composable
fun MinimalBorderedCard(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .width(220.dp)
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(16.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Account Settings", fontSize = 14.sp, color = Color(0xFF111827))
        Icon(Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null, tint = Color(0xFF9CA3AF), modifier = Modifier.size(18.dp))
    }
}

/** 5. Elevated Interactive Card — elevation rises on press, like a tactile button-card hybrid. */
@Composable
fun ElevatedInteractiveCard(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(if (pressed) 2.dp else 8.dp, label = "elevatedCardElevation")

    Box(
        modifier = Modifier
            .width(220.dp)
            .shadow(elevation, RoundedCornerShape(24.dp), ambientColor = Color(0xFFB8BCC8))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable(interactionSource = interactionSource, indication = null) { onClick() }
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2F6FED).copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Outlined.TrendingUp, contentDescription = null, tint = Color(0xFF2F6FED))
            }
            Column(Modifier.padding(start = 12.dp)) {
                Text("View Analytics", fontSize = 14.sp, color = Color(0xFF111827))
                Text("Updated 2m ago", fontSize = 12.sp, color = Color(0xFF9CA3AF))
            }
        }
    }
}
