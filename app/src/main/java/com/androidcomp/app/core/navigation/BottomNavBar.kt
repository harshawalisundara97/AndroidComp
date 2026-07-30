package com.androidcomp.app.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.androidcomp.app.core.ui.theme.AppRadii

private fun iconFor(route: Route): ImageVector = when (route) {
    Route.Home -> Icons.Outlined.Home
    Route.Categories -> Icons.Outlined.Category
    Route.Favorites -> Icons.Outlined.FavoriteBorder
    Route.Settings -> Icons.Outlined.Settings
    else -> Icons.Outlined.Home
}

private fun labelFor(route: Route): String = when (route) {
    Route.Home -> "Home"
    Route.Categories -> "Categories"
    Route.Favorites -> "Favorites"
    Route.Settings -> "Settings"
    else -> ""
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Box(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 12.dp)) {
        Surface(
            shape = RoundedCornerShape(AppRadii.bottomNav),
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 6.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            NavigationBar(
                containerColor = Color.Transparent,
                tonalElevation = 0.dp,
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                bottomNavRoutes.forEach { route ->
                    val selected = currentDestination?.hierarchy?.any { it.route == route.path } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = { navController.navigateToTab(route) },
                        icon = { Icon(iconFor(route), contentDescription = labelFor(route)) },
                        label = { Text(labelFor(route)) },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        )
                    )
                }
            }
        }
    }
}
