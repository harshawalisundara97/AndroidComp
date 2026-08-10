package com.androidcomp.app.features.storage.customstyles

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
fun StorageStylesShowcase(
    state: StorageStylesState,
    onStartUpload: () -> Unit,
    onCancelUpload: () -> Unit,
    onCycleSyncStatus: () -> Unit,
    onClearCache: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        StorageStyleRow(
            "1. Storage Usage Bar",
            "Horizontal bar split into Photos/Videos/Apps/Other segments, with a legend below.",
            { StorageUsageBar() },
            storageUsageBarCode
        )
        StorageStyleRow(
            "2. Upload Progress Card",
            "Filename with an animated progress fill; Start Upload runs it, Cancel resets it.",
            {
                UploadProgressCard(
                    uploadState = state.uploadState,
                    progress = state.uploadProgress,
                    onStart = onStartUpload,
                    onCancel = onCancelUpload
                )
            },
            uploadProgressCardCode
        )
        StorageStyleRow(
            "3. Sync Status Indicator",
            "Icon+text row cycling Synced / Syncing… / Offline — tap to cycle for demo.",
            { SyncStatusIndicator(state.syncStatus, onCycleSyncStatus) },
            syncStatusIndicatorCode
        )
        StorageStyleRow(
            "4. File Type Icon List",
            "Small list of file rows, each with a distinct file-type icon and size label.",
            { FileTypeIconList() },
            fileTypeIconListCode
        )
        StorageStyleRow(
            "5. Cache Clear Confirmation",
            "Shows current cache size; Clear Cache animates the number counting down to 0 MB.",
            {
                CacheClearCard(
                    cacheSizeMb = state.cacheSizeMb,
                    isClearing = state.isClearingCache,
                    onClear = onClearCache
                )
            },
            cacheClearCardCode
        )
    }
}

@Composable
private fun StorageStyleRow(
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

private val storageUsageBarCode = """
    Row(Modifier.fillMaxWidth().height(14.dp).clip(RoundedCornerShape(8.dp))) {
        segments.forEach { segment ->
            Box(
                Modifier
                    .weight(segment.fraction)
                    .fillMaxHeight()
                    .background(segment.color)
            )
        }
    }
    // legend: colored dot + label + percentage per segment
""".trimIndent()

private val uploadProgressCardCode = """
    val animatedProgress by animateFloatAsState(progress, tween(150))
    Column(Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(24.dp)).padding(20.dp)) {
        Text(fileName)
        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(50))
        )
        Row {
            Button(onClick = onStart, enabled = uploadState == UploadState.IDLE) { Text("Start Upload") }
            TextButton(onClick = onCancel) { Text("Cancel") }
        }
    }
""".trimIndent()

private val syncStatusIndicatorCode = """
    val rotation by rememberInfiniteTransition().animateFloat(
        initialValue = 0f, targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing))
    )
    Row(Modifier.clickable { onTap() }) {
        Icon(
            Icons.Outlined.Sync,
            null,
            modifier = Modifier.graphicsLayer { rotationZ = if (status == SyncStatus.SYNCING) rotation else 0f }
        )
        AnimatedContent(status) { s -> Text(labelFor(s)) }
    }
""".trimIndent()

private val fileTypeIconListCode = """
    Column {
        files.forEach { file ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(file.icon, null)
                Text(file.name, modifier = Modifier.weight(1f))
                Text(file.sizeLabel, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
""".trimIndent()

private val cacheClearCardCode = """
    Column(Modifier.background(Color.White, RoundedCornerShape(24.dp)).padding(20.dp)) {
        AnimatedContent(cacheSizeMb, transitionSpec = { fadeIn() togetherWith fadeOut() }) { size ->
            Text("Cache: ${'$'}size MB", style = MaterialTheme.typography.headlineSmall)
        }
        Button(onClick = onClear, enabled = !isClearing && cacheSizeMb > 0) {
            Text(if (isClearing) "Clearing..." else "Clear Cache")
        }
    }
""".trimIndent()
