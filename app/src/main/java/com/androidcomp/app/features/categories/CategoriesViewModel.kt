package com.androidcomp.app.features.categories

import androidx.lifecycle.ViewModel
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.usecase.GetComponentsByCategoryUseCase
import com.androidcomp.app.domain.usecase.SearchComponentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    getComponentsByCategoryUseCase: GetComponentsByCategoryUseCase,
    private val searchComponentsUseCase: SearchComponentsUseCase
) : ViewModel() {

    private val _categorizedComponents =
        MutableStateFlow<List<Pair<ComponentCategory, List<ComponentSpec>>>>(emptyList())
    val categorizedComponents: StateFlow<List<Pair<ComponentCategory, List<ComponentSpec>>>> =
        _categorizedComponents.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _searchResults = MutableStateFlow<List<ComponentSpec>>(emptyList())
    val searchResults: StateFlow<List<ComponentSpec>> = _searchResults.asStateFlow()

    init {
        _categorizedComponents.value = ComponentCategory.entries
            .map { category -> category to getComponentsByCategoryUseCase(category) }
            .filter { (_, specs) -> specs.isNotEmpty() }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
        _searchResults.value = if (query.isBlank()) emptyList() else searchComponentsUseCase(query)
    }
}
