package com.androidcomp.app.features.selectioncontrols.playground

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SelectionPlaygroundControls(
    state: SelectionPlaygroundState,
    onCheckedChange: (Boolean) -> Unit,
    onEnabledChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Row {
            Text("Checked / Selected")
            Switch(checked = state.checked, onCheckedChange = onCheckedChange)
        }
        Row {
            Text("Enabled")
            Switch(checked = state.enabled, onCheckedChange = onEnabledChange)
        }
    }
}
