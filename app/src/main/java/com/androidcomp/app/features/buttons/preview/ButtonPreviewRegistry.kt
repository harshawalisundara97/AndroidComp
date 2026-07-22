package com.androidcomp.app.features.buttons.preview

import androidx.compose.runtime.Composable
import com.androidcomp.app.features.buttons.playground.PlaygroundState

object ButtonPreviewRegistry {
    val previews: Map<String, @Composable (PlaygroundState) -> Unit> = mapOf(
        "button-filled" to { state -> FilledButtonPreview(state) },
        "button-filled-tonal" to { state -> FilledTonalButtonPreview(state) },
        "button-outlined" to { state -> OutlinedButtonPreview(state) },
        "button-text" to { state -> TextButtonPreview(state) },
        "button-elevated" to { state -> ElevatedButtonPreview(state) },
        "button-icon" to { state -> IconButtonPreview(state) }
    )
}
