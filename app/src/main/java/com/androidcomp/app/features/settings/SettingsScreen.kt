package com.androidcomp.app.features.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Newspaper
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidcomp.app.core.ui.AppTopBar
import com.androidcomp.app.core.ui.SectionHeader

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val darkMode by viewModel.darkModeEnabled.collectAsState()
    val context = LocalContext.current
    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)

    Scaffold(topBar = { AppTopBar("Settings") }) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 20.dp)) {
            SectionHeader("Appearance")
            ListItem(
                headlineContent = { Text("Dark mode") },
                supportingContent = { Text("Switch between light and dark theme") },
                leadingContent = { Icon(Icons.Outlined.DarkMode, contentDescription = null) },
                trailingContent = {
                    Switch(checked = darkMode, onCheckedChange = viewModel::setDarkMode)
                }
            )

            SectionHeader("About")
            ListItem(
                headlineContent = { Text("App version") },
                supportingContent = { Text("${packageInfo.versionName} (build ${packageInfo.longVersionCode})") },
                leadingContent = { Icon(Icons.Outlined.Info, contentDescription = null) }
            )
            ListItem(
                headlineContent = { Text("Built with") },
                supportingContent = { Text("Kotlin, Jetpack Compose, Material 3, Hilt") },
                leadingContent = { Icon(Icons.Outlined.Newspaper, contentDescription = null) }
            )
        }
    }
}
