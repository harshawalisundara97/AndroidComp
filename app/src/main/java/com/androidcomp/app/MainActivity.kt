package com.androidcomp.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.androidcomp.app.core.navigation.AndroidCompNavHost
import com.androidcomp.app.core.navigation.BottomNavBar
import com.androidcomp.app.core.ui.theme.AndroidCompTheme
import com.androidcomp.app.features.settings.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Activity-scoped: the single source of truth for dark mode, shared between
            // the theme root here and the toggle in SettingsScreen (which is otherwise
            // scoped per nav back-stack-entry and would be a separate instance).
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val darkMode by settingsViewModel.darkModeEnabled.collectAsState()

            AndroidCompTheme(darkTheme = darkMode) {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = { BottomNavBar(navController) }
                ) { padding ->
                    AndroidCompNavHost(
                        navController,
                        settingsViewModel = settingsViewModel,
                        modifier = Modifier.padding(padding)
                    )
                }
            }
        }
    }
}
