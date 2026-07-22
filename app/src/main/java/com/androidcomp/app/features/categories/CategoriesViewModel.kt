package com.androidcomp.app.features.categories

import androidx.lifecycle.ViewModel
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.usecase.GetComponentsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    getComponentsByCategoryUseCase: GetComponentsByCategoryUseCase
) : ViewModel() {

    private val _buttonComponents = MutableStateFlow<List<ComponentSpec>>(emptyList())
    val buttonComponents: StateFlow<List<ComponentSpec>> = _buttonComponents.asStateFlow()

    init {
        _buttonComponents.value = getComponentsByCategoryUseCase(ComponentCategory.BUTTONS)
    }
}
