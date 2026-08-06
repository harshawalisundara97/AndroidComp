package com.androidcomp.app.features.textinputs.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

/** textinput-outlined — Outlined text field bound to local state, fully typeable. */
@Composable
fun OutlinedTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Your name") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

/** textinput-filled — Filled text field bound to local state, fully typeable. */
@Composable
fun FilledTextFieldPreview() {
    var text by remember { mutableStateOf("") }
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Your name") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}
