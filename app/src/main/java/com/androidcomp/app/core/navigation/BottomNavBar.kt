package com.androidcomp.app.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

private fun iconFor(route: Route): ImageVector = when (route) {
    Route.Home -> Icons.Filled.Home
    Route.Search -> Icons.Filled.Search
    Route.Categories -> Icons.Filled.Category
    Route.Favorites -> Icons.Filled.Favorite
    Route.Settings -> Icons.Filled.Settings
    else -> Icons.Filled.Home
}

private fun labelFor(route: Route): String = when (route) {
    Route.Home -> "Home"
    Route.Search -> "Search"
    Route.Categories -> "Categories"
    Route.Favorites -> "Favorites"
    Route.Settings -> "Settings"
    else -> ""
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        bottomNavRoutes.forEach { route ->
            val selected = currentDestination?.hierarchy?.any { it.route == route.path } == true
            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(route.path) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(iconFor(route), contentDescription = labelFor(route)) },
                label = { Text(labelFor(route)) }
            )
        }
    }
}
