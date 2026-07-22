package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import javax.inject.Inject

class GetComponentsByCategoryUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(category: ComponentCategory): List<ComponentSpec> =
        repository.getByCategory(category)
}
