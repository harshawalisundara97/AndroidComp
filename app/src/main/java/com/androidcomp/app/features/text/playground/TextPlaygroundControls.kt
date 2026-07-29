package com.androidcomp.app.features.text.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TextPlaygroundControls(
    state: TextPlaygroundState,
    onTextChange: (String) -> Unit,
    onBoldChange: (Boolean) -> Unit,
    onItalicChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = state.text,
            onValueChange = onTextChange,
            label = { Text("Text") },
            modifier = Modifier.fillMaxWidth()
        )
        Row {
            Text("Bold")
            Switch(checked = state.bold, onCheckedChange = onBoldChange)
        }
        Row {
            Text("Italic")
            Switch(checked = state.italic, onCheckedChange = onItalicChange)
        }
    }
}
