package com.androidcomp.app.features.permissions.customstyles

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Camera
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 1. Rationale Card — explains why a permission is needed, animates to a Granted state. */
@Composable
fun RationaleCard(granted: Boolean, onAllow: () -> Unit, onNotNow: () -> Unit) {
    val borderColor by animateColorAsState(
        targetValue = if (granted) Color(0xFF2ECC71) else Color(0xFFF2F2F2),
        label = "rationaleBorder"
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        AnimatedContent(targetState = granted, label = "rationaleContent") { isGranted ->
            if (isGranted) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFE8F8EF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Outlined.Check,
                            contentDescription = null,
                            tint = Color(0xFF2ECC71)
                        )
                    }
                    Column(Modifier.padding(start = 12.dp)) {
                        Text("Access Granted", style = MaterialTheme.typography.bodyLarge)
                        Text(
                            "Camera access is now enabled.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color(0xFFEAF2FF), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Outlined.Camera,
                                contentDescription = null,
                                tint = Color(0xFF3B82F6)
                            )
                        }
                        Column(Modifier.padding(start = 12.dp)) {
                            Text("Allow Camera Access", style = MaterialTheme.typography.bodyLarge)
                            Text(
                                "Used to scan documents and take photos.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            "Not now",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .clickable { onNotNow() }
                                .padding(vertical = 10.dp, horizontal = 12.dp)
                        )
                        Text(
                            "Allow",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF3B82F6),
                            modifier = Modifier
                                .clickable { onAllow() }
                                .padding(vertical = 10.dp, horizontal = 12.dp)
                        )
                    }
                }
            }
        }
    }
}

/** 2. Status Chip — tapping cycles Not requested -> Granted -> Denied -> Not requested. */
@Composable
fun StatusChip(state: PermissionGrantState, onClick: () -> Unit) {
    val (label, bg, fg) = when (state) {
        PermissionGrantState.GRANTED -> Triple("Granted", Color(0xFFE8F8EF), Color(0xFF2ECC71))
        PermissionGrantState.DENIED -> Triple("Denied", Color(0xFFFDEBEB), Color(0xFFE74C3C))
        PermissionGrantState.NOT_REQUESTED -> Triple("Not requested", Color(0xFFF2F2F2), Color(0xFF6B7280))
    }
    val bgColor by animateColorAsState(targetValue = bg, label = "chipBg")
    val fgColor by animateColorAsState(targetValue = fg, label = "chipFg")
    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        AnimatedContent(targetState = label, label = "chipLabel") { text ->
            Text(text, style = MaterialTheme.typography.bodySmall, color = fgColor)
        }
    }
}

/** 3. Settings Redirect Banner — warning banner shown when denied, with a mock "Open Settings" action. */
@Composable
fun SettingsRedirectBanner(dismissed: Boolean, onOpenSettings: () -> Unit) {
    AnimatedVisibility(
        visible = !dismissed,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF6E5), RoundedCornerShape(16.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Warning,
                contentDescription = null,
                tint = Color(0xFFE67E22)
            )
            Column(
                Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    "Permission required",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF7A4A00)
                )
                Text(
                    "Enable this permission in Settings to continue.",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF9A6A1A)
                )
            }
            Text(
                "Open Settings",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFFE67E22),
                modifier = Modifier
                    .clickable { onOpenSettings() }
                    .padding(8.dp)
            )
        }
    }
}

/** 4. Permission Checklist — tap each row to toggle granted; checkmark animates in. */
@Composable
fun PermissionChecklist(items: List<ChecklistPermission>, onToggle: (String) -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            val icon = when (item.label) {
                "Camera" -> Icons.Outlined.Camera
                "Location" -> Icons.Outlined.LocationOn
                "Microphone" -> Icons.Outlined.Mic
                else -> Icons.Outlined.Notifications
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle(item.label) }
                    .padding(vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    item.label,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .fillMaxWidth()
                        .weight(1f)
                )
                AnimatedContent(targetState = item.granted, label = "checklistState${index}") { granted ->
                    Icon(
                        if (granted) Icons.Outlined.CheckCircle else Icons.Outlined.Circle,
                        contentDescription = null,
                        tint = if (granted) Color(0xFF2ECC71) else Color(0xFFD0D0D4)
                    )
                }
            }
        }
    }
}

/** 5. Animated Shield Prompt — pulsing shield icon above a permission request message. */
@Composable
fun AnimatedShieldPrompt(granted: Boolean, onAllow: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "shieldPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shieldScale"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F8FA), RoundedCornerShape(24.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .graphicsLayer {
                    scaleX = if (granted) 1f else pulseScale
                    scaleY = if (granted) 1f else pulseScale
                }
                .background(if (granted) Color(0xFFE8F8EF) else Color(0xFFEAF2FF), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(targetState = granted, label = "shieldIcon") { isGranted ->
                Icon(
                    if (isGranted) Icons.Outlined.Shield else Icons.Outlined.Lock,
                    contentDescription = null,
                    tint = if (isGranted) Color(0xFF2ECC71) else Color(0xFF3B82F6),
                    modifier = Modifier.size(30.dp)
                )
            }
        }
        Text(
            if (granted) "You're all set" else "Allow secure access",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            if (granted) "This app can now use protected features."
            else "We use this to keep your data safe and in sync.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
        if (!granted) {
            Text(
                "Allow",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color(0xFF3B82F6), RoundedCornerShape(18.dp))
                    .clickable { onAllow() }
                    .padding(horizontal = 24.dp, vertical = 10.dp)
            )
        }
    }
}
