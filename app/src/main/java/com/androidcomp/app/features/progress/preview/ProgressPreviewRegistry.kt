package com.androidcomp.app.features.progress.preview

import androidx.compose.runtime.Composable
import com.androidcomp.app.features.progress.playground.ProgressPlaygroundState

object ProgressPreviewRegistry {
    val previews: Map<String, @Composable (ProgressPlaygroundState) -> Unit> = mapOf(
        "progress-circular" to { state -> CircularProgressPreview(state) },
        "progress-linear" to { state -> LinearProgressPreview(state) }
    )
}
