package com.androidcomp.app.data.repository

import com.androidcomp.app.data.componentcatalog.AnimationComponentCatalog
import com.androidcomp.app.data.componentcatalog.ButtonComponentCatalog
import com.androidcomp.app.data.componentcatalog.CameraComponentCatalog
import com.androidcomp.app.data.componentcatalog.DialogComponentCatalog
import com.androidcomp.app.data.componentcatalog.GestureComponentCatalog
import com.androidcomp.app.data.componentcatalog.GraphicsComponentCatalog
import com.androidcomp.app.data.componentcatalog.ImageComponentCatalog
import com.androidcomp.app.data.componentcatalog.LayoutComponentCatalog
import com.androidcomp.app.data.componentcatalog.ListComponentCatalog
import com.androidcomp.app.data.componentcatalog.MapComponentCatalog
import com.androidcomp.app.data.componentcatalog.MaterialComponentCatalog
import com.androidcomp.app.data.componentcatalog.MediaComponentCatalog
import com.androidcomp.app.data.componentcatalog.MenuComponentCatalog
import com.androidcomp.app.data.componentcatalog.NavigationComponentCatalog
import com.androidcomp.app.data.componentcatalog.NetworkingComponentCatalog
import com.androidcomp.app.data.componentcatalog.NotificationComponentCatalog
import com.androidcomp.app.data.componentcatalog.PermissionComponentCatalog
import com.androidcomp.app.data.componentcatalog.ProgressComponentCatalog
import com.androidcomp.app.data.componentcatalog.SelectionControlComponentCatalog
import com.androidcomp.app.data.componentcatalog.SensorComponentCatalog
import com.androidcomp.app.data.componentcatalog.SliderComponentCatalog
import com.androidcomp.app.data.componentcatalog.StorageComponentCatalog
import com.androidcomp.app.data.componentcatalog.TextComponentCatalog
import com.androidcomp.app.data.componentcatalog.TextInputComponentCatalog
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentSpec
import com.androidcomp.app.domain.repository.ComponentRepository
import javax.inject.Inject

class ComponentRepositoryImpl @Inject constructor() : ComponentRepository {

    private val catalog: List<ComponentSpec> =
        ButtonComponentCatalog.all +
            TextComponentCatalog.all +
            TextInputComponentCatalog.all +
            ImageComponentCatalog.all +
            LayoutComponentCatalog.all +
            ListComponentCatalog.all +
            SelectionControlComponentCatalog.all +
            NavigationComponentCatalog.all +
            DialogComponentCatalog.all +
            MenuComponentCatalog.all +
            MaterialComponentCatalog.all +
            ProgressComponentCatalog.all +
            SliderComponentCatalog.all +
            AnimationComponentCatalog.all +
            GestureComponentCatalog.all +
            GraphicsComponentCatalog.all +
            CameraComponentCatalog.all +
            MediaComponentCatalog.all +
            NotificationComponentCatalog.all +
            PermissionComponentCatalog.all +
            StorageComponentCatalog.all +
            NetworkingComponentCatalog.all +
            MapComponentCatalog.all +
            SensorComponentCatalog.all

    override fun getAll(): List<ComponentSpec> = catalog

    override fun getByCategory(category: ComponentCategory): List<ComponentSpec> =
        catalog.filter { it.category == category }

    override fun getById(id: String): ComponentSpec? =
        catalog.find { it.id == id }
}
