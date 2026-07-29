package com.androidcomp.app.features.progress.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ProgressPlaygroundControls(
    state: ProgressPlaygroundState,
    onProgressChange: (Float) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Text("Progress: ${(state.progress * 100).toInt()}%")
        Slider(
            value = state.progress,
            onValueChange = onProgressChange,
            valueRange = 0f..1f,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
