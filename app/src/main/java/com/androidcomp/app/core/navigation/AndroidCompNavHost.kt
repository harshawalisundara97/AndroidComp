package com.androidcomp.app.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.androidcomp.app.features.categories.CategoriesScreen
import com.androidcomp.app.features.componentdetail.ComponentDetailScreen
import com.androidcomp.app.features.favorites.FavoritesScreen
import com.androidcomp.app.features.home.HomeScreen
import com.androidcomp.app.features.settings.SettingsScreen
import com.androidcomp.app.features.settings.SettingsViewModel
import com.androidcomp.app.features.search.SearchScreen

@Composable
fun AndroidCompNavHost(
    navController: NavHostController,
    settingsViewModel: SettingsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = Route.Home.path, modifier = modifier) {
        composable(Route.Home.path) {
            HomeScreen(
                onComponentClick = { id ->
                    navController.navigate(Route.ComponentDetail.buildRoute(id))
                },
                onViewAllCategoriesClick = { navController.navigateToTab(Route.Categories) }
            )
        }
        composable(Route.Search.path) {
            SearchScreen(onComponentClick = { id ->
                navController.navigate(Route.ComponentDetail.buildRoute(id))
            })
        }
        composable(Route.Categories.path) {
            CategoriesScreen(onComponentClick = { id ->
                navController.navigate(Route.ComponentDetail.buildRoute(id))
            })
        }
        composable(Route.Favorites.path) {
            FavoritesScreen()
        }
        composable(Route.Settings.path) {
            SettingsScreen(viewModel = settingsViewModel)
        }
        composable(
            route = Route.ComponentDetail.ROUTE_PATTERN,
            arguments = listOf(navArgument(Route.ComponentDetail.ARG_COMPONENT_ID) { type = NavType.StringType })
        ) {
            ComponentDetailScreen(
                onRelatedComponentClick = { id ->
                    navController.navigate(Route.ComponentDetail.buildRoute(id))
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
