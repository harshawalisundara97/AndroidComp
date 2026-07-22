package com.androidcomp.app.features.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onComponentClick: (String) -> Unit) {
    Scaffold { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Android Comp")
            Text("Browse categories to explore live Android UI components.")
        }
    }
}
