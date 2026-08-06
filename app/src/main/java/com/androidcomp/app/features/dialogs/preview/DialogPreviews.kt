package com.androidcomp.app.features.dialogs.preview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/** dialog-alert — button opens a real AlertDialog with Confirm/Dismiss actions. */
@Composable
fun AlertDialogPreview() {
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Text("Show Alert Dialog")
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Delete item?") },
            text = { Text("This action cannot be undone. Are you sure you want to continue?") },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) { Text("Confirm") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Dismiss") }
            }
        )
    }
}

/** dialog-fullscreen — button opens a real full-screen Dialog with a Close button. */
@Composable
fun FullScreenDialogPreview() {
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Text("Show Full-Screen Dialog")
    }

    if (showDialog) {
        Dialog(
            onDismissRequest = { showDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Full-Screen Dialog",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
                    )
                    Text(
                        text = "This dialog fills the available space, similar to a full-screen modal flow.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 24.dp)) {
                        Button(onClick = { showDialog = false }) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}
