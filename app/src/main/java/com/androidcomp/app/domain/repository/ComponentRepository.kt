package com.androidcomp.app.domain.repository

import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec

interface ComponentRepository {
    fun getAll(): List<ComponentSpec>
    fun getByCategory(category: ComponentCategory): List<ComponentSpec>
    fun getById(id: String): ComponentSpec?
}
