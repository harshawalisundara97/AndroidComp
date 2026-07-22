package com.androidcomp.app.core.navigation

sealed class Route(val path: String) {
    data object Home : Route("home")
    data object Search : Route("search")
    data object Categories : Route("categories")
    data object Favorites : Route("favorites")
    data object Settings : Route("settings")

    data class ComponentDetail(val componentId: String) : Route("component_detail") {
        companion object {
            const val ARG_COMPONENT_ID = "componentId"
            const val ROUTE_PATTERN = "component_detail/{$ARG_COMPONENT_ID}"
            fun buildRoute(componentId: String) = "component_detail/$componentId"
        }
    }
}

val bottomNavRoutes = listOf(Route.Home, Route.Search, Route.Categories, Route.Favorites, Route.Settings)
