package com.androidcomp.app.core.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

sealed class Route(val path: String) {
    data object Home : Route("home")
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

    data class NewsDetail(val newsId: String) : Route("news_detail") {
        companion object {
            const val ARG_NEWS_ID = "newsId"
            const val ROUTE_PATTERN = "news_detail/{$ARG_NEWS_ID}"
            fun buildRoute(newsId: String) = "news_detail/$newsId"
        }
    }
}

val bottomNavRoutes = listOf(Route.Home, Route.Categories, Route.Favorites, Route.Settings)

/**
 * Navigates to a bottom-nav destination using the standard tab-switch pattern
 * (single back-stack entry per tab, state preserved across switches). Use this
 * for ANY navigation to a bottom-nav route — including links from other screens
 * (e.g. a "View all categories" button on Home) — not just BottomNavBar itself,
 * so a forward push from elsewhere can't leave a duplicate entry that later
 * confuses tab selection/back navigation.
 */
fun NavHostController.navigateToTab(route: Route) {
    navigate(route.path) {
        popUpTo(graph.findStartDestination().id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
