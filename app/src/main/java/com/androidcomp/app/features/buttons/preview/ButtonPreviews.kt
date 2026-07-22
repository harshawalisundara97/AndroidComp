package com.androidcomp.app.features.buttons.preview

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.androidcomp.app.features.buttons.playground.PlaygroundState

@Composable
fun FilledButtonPreview(state: PlaygroundState) {
    Button(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun FilledTonalButtonPreview(state: PlaygroundState) {
    FilledTonalButton(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun OutlinedButtonPreview(state: PlaygroundState) {
    OutlinedButton(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun TextButtonPreview(state: PlaygroundState) {
    TextButton(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun ElevatedButtonPreview(state: PlaygroundState) {
    ElevatedButton(onClick = {}, enabled = state.enabled) {
        Text(state.label)
    }
}

@Composable
fun IconButtonPreview(state: PlaygroundState) {
    IconButton(onClick = {}, enabled = state.enabled) {
        Icon(Icons.Filled.Favorite, contentDescription = state.label)
    }
}
