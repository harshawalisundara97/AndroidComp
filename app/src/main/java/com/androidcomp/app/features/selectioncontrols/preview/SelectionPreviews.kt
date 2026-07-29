package com.androidcomp.app.features.selectioncontrols.preview

import androidx.compose.material3.Checkbox
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import com.androidcomp.app.features.selectioncontrols.playground.SelectionPlaygroundState

@Composable
fun CheckboxPreview(state: SelectionPlaygroundState) {
    Checkbox(checked = state.checked, onCheckedChange = {}, enabled = state.enabled)
}

@Composable
fun RadioButtonPreview(state: SelectionPlaygroundState) {
    RadioButton(selected = state.checked, onClick = {}, enabled = state.enabled)
}

@Composable
fun SwitchPreview(state: SelectionPlaygroundState) {
    Switch(checked = state.checked, onCheckedChange = {}, enabled = state.enabled)
}
