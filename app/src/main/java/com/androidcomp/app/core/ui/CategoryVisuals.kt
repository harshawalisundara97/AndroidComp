package com.androidcomp.app.core.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Animation
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Gesture
import androidx.compose.material.icons.outlined.HourglassEmpty
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Navigation
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Sensors
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material.icons.outlined.TextFields
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material.icons.outlined.Widgets
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.androidcomp.app.domain.model.ComponentCategory

data class CategoryVisual(val icon: ImageVector, val tint: Color)

/** Shared icon + accent color per category, used on Home's category grid and the Categories page. */
object CategoryVisuals {
    private val blue = Color(0xFF2F6FED)
    private val green = Color(0xFF2E9E5B)
    private val orange = Color(0xFFE58A2E)

    private val map: Map<ComponentCategory, CategoryVisual> = mapOf(
        ComponentCategory.BUTTONS to CategoryVisual(Icons.Outlined.TouchApp, blue),
        ComponentCategory.TEXT to CategoryVisual(Icons.Outlined.TextFields, green),
        ComponentCategory.TEXT_INPUTS to CategoryVisual(Icons.Outlined.Edit, orange),
        ComponentCategory.IMAGES to CategoryVisual(Icons.Outlined.Image, blue),
        ComponentCategory.LAYOUTS to CategoryVisual(Icons.Outlined.Dashboard, green),
        ComponentCategory.LISTS to CategoryVisual(Icons.AutoMirrored.Outlined.List, orange),
        ComponentCategory.NAVIGATION to CategoryVisual(Icons.Outlined.Navigation, blue),
        ComponentCategory.DIALOGS to CategoryVisual(Icons.AutoMirrored.Outlined.Chat, green),
        ComponentCategory.MENUS to CategoryVisual(Icons.Outlined.Menu, orange),
        ComponentCategory.PROGRESS to CategoryVisual(Icons.Outlined.HourglassEmpty, green),
        ComponentCategory.SELECTION_CONTROLS to CategoryVisual(Icons.Outlined.CheckBox, orange),
        ComponentCategory.SLIDERS to CategoryVisual(Icons.Outlined.Tune, blue),
        ComponentCategory.GESTURES to CategoryVisual(Icons.Outlined.Gesture, green),
        ComponentCategory.ANIMATIONS to CategoryVisual(Icons.Outlined.Animation, orange),
        ComponentCategory.GRAPHICS to CategoryVisual(Icons.Outlined.Brush, blue),
        ComponentCategory.CAMERA to CategoryVisual(Icons.Outlined.CameraAlt, green),
        ComponentCategory.PERMISSIONS to CategoryVisual(Icons.Outlined.Lock, green),
        ComponentCategory.NOTIFICATIONS to CategoryVisual(Icons.Outlined.Notifications, blue),
        ComponentCategory.STORAGE to CategoryVisual(Icons.Outlined.Storage, orange),
        ComponentCategory.NETWORKING to CategoryVisual(Icons.Outlined.Cloud, blue),
        ComponentCategory.MAPS to CategoryVisual(Icons.Outlined.Map, green),
        ComponentCategory.SENSORS to CategoryVisual(Icons.Outlined.Sensors, orange),
        ComponentCategory.MEDIA to CategoryVisual(Icons.Outlined.Movie, orange),
        ComponentCategory.MATERIAL_COMPONENTS to CategoryVisual(Icons.Outlined.Widgets, blue)
    )

    private val default = CategoryVisual(Icons.Outlined.Widgets, blue)

    fun of(category: ComponentCategory): CategoryVisual = map[category] ?: default
}
