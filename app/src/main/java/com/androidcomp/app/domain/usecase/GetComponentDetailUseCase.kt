package com.androidcomp.app.domain.usecase

import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import javax.inject.Inject

class GetComponentDetailUseCase @Inject constructor(
    private val repository: ComponentRepository
) {
    operator fun invoke(id: String): ComponentSpec? = repository.getById(id)
}
