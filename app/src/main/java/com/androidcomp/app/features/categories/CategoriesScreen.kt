package com.androidcomp.app.features.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidcomp.app.core.ui.AppTopBar

@Composable
fun CategoriesScreen(
    onComponentClick: (String) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val components by viewModel.buttonComponents.collectAsState()

    Scaffold(topBar = { AppTopBar("Categories") }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(components, key = { it.id }) { spec ->
                ListItem(
                    headlineContent = { Text(spec.title) },
                    modifier = Modifier.clickable { onComponentClick(spec.id) }
                )
            }
        }
    }
}
