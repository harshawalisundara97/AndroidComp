package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.repository.ComponentRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private class FakeComponentRepositoryForCategoryTest(
    private val specs: List<com.androidcomp.app.domain.model.ComponentSpec>
) : ComponentRepository {
    override fun getAll() = specs
    override fun getByCategory(category: ComponentCategory) = specs.filter { it.category == category }
    override fun getById(id: String) = specs.find { it.id == id }
}

class GetComponentsByCategoryUseCaseTest {

    @Test
    fun `returns only specs matching the requested category`() {
        val buttonSpec = com.androidcomp.app.domain.model.ComponentSpec(
            id = "button-filled", category = ComponentCategory.BUTTONS, title = "Filled Button",
            overview = "", composeCode = com.androidcomp.app.domain.model.CodeSample(com.androidcomp.app.domain.model.CodeLanguage.COMPOSE, ""),
            xmlCode = null, viewModelUsage = null, properties = emptyList(), events = emptyList(),
            bestPractices = emptyList(), commonMistakes = emptyList(), accessibilityNotes = emptyList(),
            performanceNotes = emptyList(), relatedComponentIds = emptyList(), minApi = 21
        )
        val textSpec = buttonSpec.copy(id = "text-label", category = ComponentCategory.TEXT, title = "Label")
        val repository = FakeComponentRepositoryForCategoryTest(listOf(buttonSpec, textSpec))
        val useCase = GetComponentsByCategoryUseCase(repository)

        val result = useCase(ComponentCategory.BUTTONS)

        assertThat(result).containsExactly(buttonSpec)
    }
}
