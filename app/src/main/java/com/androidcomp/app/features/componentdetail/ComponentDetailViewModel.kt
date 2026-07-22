package com.androidcomp.app.features.componentdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.androidcomp.app.core.navigation.Route
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.usecase.GetComponentDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ComponentDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getComponentDetailUseCase: GetComponentDetailUseCase
) : ViewModel() {

    private val _spec = MutableStateFlow<ComponentSpec?>(null)
    val spec: StateFlow<ComponentSpec?> = _spec.asStateFlow()

    init {
        val componentId: String = checkNotNull(
            savedStateHandle[Route.ComponentDetail.ARG_COMPONENT_ID]
        )
        _spec.value = getComponentDetailUseCase(componentId)
    }
}
