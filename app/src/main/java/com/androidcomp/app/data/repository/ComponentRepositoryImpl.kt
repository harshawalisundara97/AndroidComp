package com.androidcomp.app.data.repository

import com.androidcomp.app.data.componentcatalog.ButtonComponentCatalog
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import javax.inject.Inject

class ComponentRepositoryImpl @Inject constructor() : ComponentRepository {

    private val catalog: List<ComponentSpec> = ButtonComponentCatalog.all

    override fun getAll(): List<ComponentSpec> = catalog

    override fun getByCategory(category: ComponentCategory): List<ComponentSpec> =
        catalog.filter { it.category == category }

    override fun getById(id: String): ComponentSpec? =
        catalog.find { it.id == id }
}
