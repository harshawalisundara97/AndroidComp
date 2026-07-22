package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import javax.inject.Inject

class SearchComponentsUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(query: String): List<ComponentSpec> =
        repository.getAll().filter { it.title.contains(query, ignoreCase = true) }
}
