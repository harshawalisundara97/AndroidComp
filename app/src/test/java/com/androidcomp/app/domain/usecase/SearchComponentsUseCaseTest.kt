package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private class FakeComponentRepositoryForSearchTest(
    private val specs: List<ComponentSpec>
) : ComponentRepository {
    override fun getAll() = specs
    override fun getByCategory(category: ComponentCategory) = specs.filter { it.category == category }
    override fun getById(id: String) = specs.find { it.id == id }
}

class SearchComponentsUseCaseTest {

    private fun spec(id: String, title: String) = ComponentSpec(
        id = id, category = ComponentCategory.BUTTONS, title = title,
        overview = "", composeCode = CodeSample(CodeLanguage.COMPOSE, ""),
        xmlCode = null, viewModelUsage = null, properties = emptyList(), events = emptyList(),
        bestPractices = emptyList(), commonMistakes = emptyList(), accessibilityNotes = emptyList(),
        performanceNotes = emptyList(), relatedComponentIds = emptyList(), minApi = 21
    )

    @Test
    fun `matches titles case-insensitively by substring`() {
        val filled = spec("button-filled", "Filled Button")
        val outlined = spec("button-outlined", "Outlined Button")
        val useCase = SearchComponentsUseCase(FakeComponentRepositoryForSearchTest(listOf(filled, outlined)))

        val result = useCase("filled")

        assertThat(result).containsExactly(filled)
    }

    @Test
    fun `returns empty list when query matches nothing`() {
        val filled = spec("button-filled", "Filled Button")
        val useCase = SearchComponentsUseCase(FakeComponentRepositoryForSearchTest(listOf(filled)))

        val result = useCase("zzz")

        assertThat(result).isEmpty()
    }
}
