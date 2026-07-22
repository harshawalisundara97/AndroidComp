package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import com.google.common.truth.Truth.assertThat
import org.junit.Test

private class FakeComponentRepositoryForDetailTest(
    private val specs: List<ComponentSpec>
) : ComponentRepository {
    override fun getAll() = specs
    override fun getByCategory(category: ComponentCategory) = specs.filter { it.category == category }
    override fun getById(id: String) = specs.find { it.id == id }
}

class GetComponentDetailUseCaseTest {

    private val spec = ComponentSpec(
        id = "button-filled", category = ComponentCategory.BUTTONS, title = "Filled Button",
        overview = "", composeCode = CodeSample(CodeLanguage.COMPOSE, ""),
        xmlCode = null, viewModelUsage = null, properties = emptyList(), events = emptyList(),
        bestPractices = emptyList(), commonMistakes = emptyList(), accessibilityNotes = emptyList(),
        performanceNotes = emptyList(), relatedComponentIds = emptyList(), minApi = 21
    )

    @Test
    fun `returns the spec matching the given id`() {
        val useCase = GetComponentDetailUseCase(FakeComponentRepositoryForDetailTest(listOf(spec)))

        val result = useCase("button-filled")

        assertThat(result).isEqualTo(spec)
    }

    @Test
    fun `returns null when no spec matches the id`() {
        val useCase = GetComponentDetailUseCase(FakeComponentRepositoryForDetailTest(listOf(spec)))

        val result = useCase("does-not-exist")

        assertThat(result).isNull()
    }
}
