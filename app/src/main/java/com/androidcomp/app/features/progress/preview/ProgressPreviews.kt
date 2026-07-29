package com.androidcomp.app.features.progress.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.androidcomp.app.features.progress.playground.ProgressPlaygroundState

@Composable
fun CircularProgressPreview(state: ProgressPlaygroundState) {
    CircularProgressIndicator(progress = { state.progress })
}

@Composable
fun LinearProgressPreview(state: ProgressPlaygroundState) {
    LinearProgressIndicator(
        progress = { state.progress },
        modifier = Modifier.fillMaxWidth()
    )
}
