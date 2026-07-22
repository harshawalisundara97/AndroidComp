package com.androidcomp.app.features.search

import androidx.lifecycle.ViewModel
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.usecase.SearchComponentsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchComponentsUseCase: SearchComponentsUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _results = MutableStateFlow<List<ComponentSpec>>(emptyList())
    val results: StateFlow<List<ComponentSpec>> = _results.asStateFlow()

    fun onQueryChanged(newQuery: String) {
        _query.value = newQuery
        _results.value = if (newQuery.isBlank()) emptyList() else searchComponentsUseCase(newQuery)
    }
}
