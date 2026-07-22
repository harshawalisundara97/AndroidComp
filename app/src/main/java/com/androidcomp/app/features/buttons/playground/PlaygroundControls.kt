package com.androidcomp.app.features.buttons.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PlaygroundControls(
    state: PlaygroundState,
    onLabelChange: (String) -> Unit,
    onEnabledChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.label,
            onValueChange = onLabelChange,
            label = { Text("Label") },
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            Text("Enabled")
            Switch(checked = state.enabled, onCheckedChange = onEnabledChange)
        }
    }
}
