package com.androidcomp.app.features.storage.customstyles

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cached
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Sync
import androidx.compose.material.icons.outlined.VideoFile
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private data class StorageSegment(val label: String, val fraction: Float, val color: Color)

private val storageSegments = listOf(
    StorageSegment("Photos", 0.38f, Color(0xFF3B82F6)),
    StorageSegment("Videos", 0.27f, Color(0xFFF97316)),
    StorageSegment("Apps", 0.22f, Color(0xFF8B5CF6)),
    StorageSegment("Other", 0.13f, Color(0xFF9CA3AF))
)

/** 1. Storage Usage Bar — segmented horizontal bar animating in on first composition, with a legend. */
@Composable
fun StorageUsageBar() {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }

    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(14.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF2F2F2))
        ) {
            storageSegments.forEach { segment ->
                val animatedFraction by animateFloatAsState(
                    targetValue = if (started) segment.fraction else 0f,
                    animationSpec = tween(700),
                    label = "segment-${segment.label}"
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(animatedFraction.coerceAtLeast(0.0001f))
                        .background(segment.color)
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            storageSegments.forEach { segment ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(segment.color, CircleShape)
                    )
                    Text(
                        text = "  ${segment.label}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/** 2. Upload Progress Card — filename, animated progress bar over ~a couple seconds, Cancel button. */
@Composable
fun UploadProgressCard(
    uploadState: UploadState,
    progress: Float,
    onStart: () -> Unit,
    onCancel: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(150, easing = LinearEasing),
        label = "uploadProgress"
    )
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Outlined.CloudUpload,
                    contentDescription = null,
                    tint = Color(0xFF3B82F6),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "  vacation_photos.zip",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
            AnimatedVisibility(visible = uploadState != UploadState.IDLE) {
                Column(Modifier.padding(top = 12.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFFE5E7EB))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (uploadState == UploadState.DONE) Color(0xFF22C55E) else Color(0xFF3B82F6))
                        )
                    }
                    Text(
                        text = if (uploadState == UploadState.DONE) "Upload complete" else "${(animatedProgress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                }
            }
            Row(Modifier.padding(top = 14.dp)) {
                when (uploadState) {
                    UploadState.IDLE -> Button(
                        onClick = onStart,
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                    ) { Text("Start Upload") }
                    UploadState.UPLOADING -> OutlinedButton(
                        onClick = onCancel,
                        shape = RoundedCornerShape(18.dp)
                    ) { Text("Cancel") }
                    UploadState.DONE -> Text(
                        "Done",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF22C55E)
                    )
                }
            }
        }
    }
}

/** 3. Sync Status Indicator — icon + label cycling Synced/Syncing/Offline, tap to cycle. */
@Composable
fun SyncStatusIndicator(status: SyncStatus, onTap: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "syncSpin")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(900, easing = LinearEasing)),
        label = "syncRotation"
    )

    val (icon, label, color) = when (status) {
        SyncStatus.SYNCED -> Triple(Icons.Outlined.CheckCircle, "Synced", Color(0xFF22C55E))
        SyncStatus.SYNCING -> Triple(Icons.Outlined.Sync, "Syncing...", Color(0xFF3B82F6))
        SyncStatus.OFFLINE -> Triple(Icons.Outlined.CloudOff, "Offline", Color(0xFF9CA3AF))
    }
    val tint by animateColorAsState(color, label = "syncColor")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF9FAFB))
            .clickable { onTap() }
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier
                .size(22.dp)
                .graphicsLayer { if (status == SyncStatus.SYNCING) rotationZ = rotation }
        )
        AnimatedContent(targetState = label, label = "syncLabel") { text ->
            Text(
                text = "  $text",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private data class MockFile(val name: String, val size: String, val icon: ImageVector, val tint: Color)

private val mockFiles = listOf(
    MockFile("Resume.pdf", "184 KB", Icons.Outlined.Description, Color(0xFFEF4444)),
    MockFile("Beach.jpg", "3.2 MB", Icons.Outlined.Image, Color(0xFF3B82F6)),
    MockFile("Trip_recap.mp4", "128 MB", Icons.Outlined.VideoFile, Color(0xFF8B5CF6)),
    MockFile("Archive.zip", "45 MB", Icons.Outlined.Folder, Color(0xFFF97316))
)

/** 4. File Type Icon List — rows with distinct type icons and sizes, tap to highlight (selection feedback). */
@Composable
fun FileTypeIconList() {
    Column(Modifier.fillMaxWidth()) {
        mockFiles.forEach { file ->
            var selected by remember { mutableStateOf(false) }
            val background by animateColorAsState(
                targetValue = if (selected) Color(0xFFEFF6FF) else Color.Transparent,
                label = "fileRowBg-${file.name}"
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(background)
                    .clickable { selected = !selected }
                    .padding(vertical = 10.dp, horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(file.tint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(file.icon, contentDescription = null, tint = file.tint, modifier = Modifier.size(18.dp))
                }
                Text(
                    text = file.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                )
                Text(
                    text = file.size,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/** 5. Cache Clear Confirmation — shows "Cache: N MB", Clear Cache button counts number down to 0. */
@Composable
fun CacheClearCard(cacheSizeMb: Int, isClearing: Boolean, onClear: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.Cached,
                contentDescription = null,
                tint = Color(0xFF9CA3AF),
                modifier = Modifier.size(22.dp)
            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    "Cache",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                AnimatedContent(targetState = cacheSizeMb, label = "cacheSize") { size ->
                    Text(
                        text = "$size MB",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            TextButton(
                onClick = onClear,
                enabled = !isClearing && cacheSizeMb > 0
            ) {
                Text(if (isClearing) "Clearing..." else "Clear Cache", color = Color(0xFFEF4444))
            }
        }
    }
}
