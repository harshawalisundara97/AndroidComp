package com.androidcomp.app.features.text.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.androidcomp.app.features.text.playground.TextPlaygroundState

@Composable
fun HeadingTextPreview(state: TextPlaygroundState) {
    Text(
        text = state.text,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = if (state.bold) FontWeight.Bold else null,
        fontStyle = if (state.italic) FontStyle.Italic else null
    )
}

@Composable
fun BodyTextPreview(state: TextPlaygroundState) {
    Text(
        text = state.text,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = if (state.bold) FontWeight.Bold else null,
        fontStyle = if (state.italic) FontStyle.Italic else null
    )
}
