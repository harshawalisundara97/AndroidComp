package com.androidcomp.app.features.selectioncontrols.preview

import androidx.compose.runtime.Composable
import com.androidcomp.app.features.selectioncontrols.playground.SelectionPlaygroundState

object SelectionPreviewRegistry {
    val previews: Map<String, @Composable (SelectionPlaygroundState) -> Unit> = mapOf(
        "selection-checkbox" to { state -> CheckboxPreview(state) },
        "selection-radio-button" to { state -> RadioButtonPreview(state) },
        "selection-switch" to { state -> SwitchPreview(state) }
    )
}
