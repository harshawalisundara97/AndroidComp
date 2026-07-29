package com.androidcomp.app.features.text.preview

import androidx.compose.runtime.Composable
import com.androidcomp.app.features.text.playground.TextPlaygroundState

object TextPreviewRegistry {
    val previews: Map<String, @Composable (TextPlaygroundState) -> Unit> = mapOf(
        "text-heading" to { state -> HeadingTextPreview(state) },
        "text-body" to { state -> BodyTextPreview(state) }
    )
}
