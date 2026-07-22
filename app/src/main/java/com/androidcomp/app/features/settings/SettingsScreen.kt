package com.androidcomp.app.features.settings

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val darkMode by viewModel.darkModeEnabled.collectAsState()

    Scaffold { padding ->
        Row(Modifier.padding(padding).padding(16.dp)) {
            Text("Dark mode")
            Switch(checked = darkMode, onCheckedChange = viewModel::setDarkMode)
        }
    }
}
