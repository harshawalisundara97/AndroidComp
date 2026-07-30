package com.androidcomp.app.features.categories

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.androidcomp.app.core.ui.AppTopBar
import com.androidcomp.app.core.ui.CategoryVisuals
import com.androidcomp.app.core.ui.theme.AppRadii
import com.androidcomp.app.core.ui.theme.LocalAppColors
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec

@Composable
fun CategoriesScreen(
    onComponentClick: (String) -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val categorizedComponents by viewModel.categorizedComponents.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    var expandedCategories by remember { mutableStateOf(setOf<String>()) }

    Scaffold(topBar = { AppTopBar("Categories") }) { padding ->
        Column(Modifier.padding(padding).padding(horizontal = 20.dp)) {
            CategorySearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChanged
            )

            if (searchQuery.isBlank()) {
                LazyColumn(contentPadding = PaddingValues(vertical = 12.dp)) {
                    items(categorizedComponents, key = { it.first.name }) { (category, specs) ->
                        CategoryCard(
                            category = category,
                            specs = specs,
                            expanded = expandedCategories.contains(category.name),
                            onToggleExpanded = {
                                expandedCategories = if (expandedCategories.contains(category.name)) {
                                    expandedCategories - category.name
                                } else {
                                    expandedCategories + category.name
                                }
                            },
                            onComponentClick = onComponentClick
                        )
                    }
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(vertical = 12.dp)) {
                    items(searchResults, key = { it.id }) { spec ->
                        SearchResultRow(spec, onComponentClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategorySearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        placeholder = { Text("Search components") },
        leadingIcon = { Icon(Icons.Outlined.Search, contentDescription = null) },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(Icons.Outlined.Close, contentDescription = "Clear search")
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(AppRadii.input),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    )
}

@Composable
private fun CategoryCard(
    category: ComponentCategory,
    specs: List<ComponentSpec>,
    expanded: Boolean,
    onToggleExpanded: () -> Unit,
    onComponentClick: (String) -> Unit
) {
    val visual = CategoryVisuals.of(category)
    val border = LocalAppColors.current.border
    val chevronRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "chevronRotation"
    )

    OutlinedCard(
        shape = RoundedCornerShape(AppRadii.card),
        border = BorderStroke(1.dp, border),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpanded() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(visual.tint.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(visual.icon, contentDescription = null, tint = visual.tint)
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        category.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        "${specs.size} component${if (specs.size == 1) "" else "s"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    Icons.Filled.ArrowDropDown,
                    contentDescription = if (expanded) "Collapse" else "Expand",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.rotate(chevronRotation)
                )
            }

            AnimatedVisibility(visible = expanded, enter = expandVertically(), exit = shrinkVertically()) {
                Column(Modifier.padding(bottom = 8.dp)) {
                    specs.forEach { spec ->
                        ListItem(
                            headlineContent = { Text(spec.title) },
                            modifier = Modifier.clickable { onComponentClick(spec.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchResultRow(spec: ComponentSpec, onComponentClick: (String) -> Unit) {
    val visual = CategoryVisuals.of(spec.category)
    ListItem(
        headlineContent = { Text(spec.title) },
        supportingContent = { Text(spec.category.displayName) },
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(visual.tint.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(visual.icon, contentDescription = null, tint = visual.tint)
            }
        },
        modifier = Modifier.clickable { onComponentClick(spec.id) }
    )
}
