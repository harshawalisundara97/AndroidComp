package com.androidcomp.app.features.componentdetail

import androidx.lifecycle.SavedStateHandle
import com.androidcomp.app.core.navigation.Route
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import com.androidcomp.app.domain.usecase.GetComponentDetailUseCase
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private class FakeComponentRepository(
    private val specs: List<ComponentSpec>
) : ComponentRepository {
    override fun getAll() = specs
    override fun getByCategory(category: ComponentCategory) = specs.filter { it.category == category }
    override fun getById(id: String) = specs.find { it.id == id }
}

class ComponentDetailViewModelTest {

    private val spec = ComponentSpec(
        id = "button-filled", category = ComponentCategory.BUTTONS, title = "Filled Button",
        overview = "An overview", composeCode = CodeSample(CodeLanguage.COMPOSE, ""),
        xmlCode = null, viewModelUsage = null, properties = emptyList(), events = emptyList(),
        bestPractices = emptyList(), commonMistakes = emptyList(), accessibilityNotes = emptyList(),
        performanceNotes = emptyList(), relatedComponentIds = emptyList(), minApi = 21
    )

    @Test
    fun `spec is populated from the componentId nav arg`() {
        val savedStateHandle = SavedStateHandle(mapOf(Route.ComponentDetail.ARG_COMPONENT_ID to "button-filled"))
        val useCase = GetComponentDetailUseCase(FakeComponentRepository(listOf(spec)))

        val viewModel = ComponentDetailViewModel(savedStateHandle, useCase)

        assertThat(viewModel.spec.value).isEqualTo(spec)
    }

    @Test
    fun `spec is null when no component matches the id`() {
        val savedStateHandle = SavedStateHandle(mapOf(Route.ComponentDetail.ARG_COMPONENT_ID to "does-not-exist"))
        val useCase = GetComponentDetailUseCase(FakeComponentRepository(listOf(spec)))

        val viewModel = ComponentDetailViewModel(savedStateHandle, useCase)

        assertThat(viewModel.spec.value).isNull()
    }

    @Test(expected = IllegalStateException::class)
    fun `throws when componentId nav arg is missing`() {
        val savedStateHandle = SavedStateHandle()
        val useCase = GetComponentDetailUseCase(FakeComponentRepository(listOf(spec)))

        ComponentDetailViewModel(savedStateHandle, useCase)
    }
}
