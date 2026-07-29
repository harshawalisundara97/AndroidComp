package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object NavigationComponentCatalog {

    private val bottomBar = ComponentSpec(
        id = "nav-bottom-bar",
        category = ComponentCategory.NAVIGATION,
        title = "Navigation Bar (Bottom Navigation)",
        overview = "A bottom navigation bar that lets users switch between top-level " +
            "destinations, typically 3-5 items, each with an icon and optional label.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedIndex == 0,
                        onClick = { selectedIndex = 0 },
                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                        label = { Text("Home") }
                    )
                    NavigationBarItem(
                        selected = selectedIndex == 1,
                        onClick = { selectedIndex = 1 },
                        icon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                        label = { Text("Search") }
                    )
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.bottomnavigation.BottomNavigationView
                    android:id="@+id/bottom_navigation"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    app:menu="@menu/bottom_nav_menu" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onDestinationSelected(index: Int) {
                viewModel.setSelectedTab(index)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("selected", "Boolean", "required", "Whether this item is currently selected (on NavigationBarItem)."),
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the item is tapped (on NavigationBarItem)."),
            ComponentProperty("icon", "@Composable () -> Unit", "required", "Icon content for the item."),
            ComponentProperty("label", "@Composable (() -> Unit)?", "null", "Optional text label shown below/beside the icon."),
            ComponentProperty("alwaysShowLabel", "Boolean", "true", "Whether the label is always visible or only when selected.")
        ),
        events = listOf("onClick — fired per NavigationBarItem when tapped or activated via accessibility services."),
        bestPractices = listOf(
            "Limit to 3-5 top-level destinations; use a different pattern (e.g. drawer) for more.",
            "Keep the selected destination in single-source-of-truth state hoisted above the NavigationBar."
        ),
        commonMistakes = listOf(
            "Nesting NavigationBar inside a scrollable container, causing it to scroll off screen.",
            "Forgetting to update selected state, leaving the highlighted item out of sync with the displayed screen."
        ),
        accessibilityNotes = listOf(
            "Each NavigationBarItem meets the 48x48dp minimum touch target by default.",
            "Icons should have descriptive contentDescription even when a label is also shown, for consistent screen-reader announcements."
        ),
        performanceNotes = listOf(
            "Avoid recreating the destination list on every recomposition; hoist it as a stable list.",
            "Pair with a single NavHost so destination content is swapped rather than the whole screen recomposing."
        ),
        relatedComponentIds = listOf("nav-host"),
        minApi = 21
    )

    private val navHost = ComponentSpec(
        id = "nav-host",
        category = ComponentCategory.NAVIGATION,
        title = "NavHost & NavController",
        overview = "NavHost hosts a navigation graph of composable destinations, and " +
            "NavController drives navigation between them (navigate, back stack, arguments).",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "home") {
                    composable("home") { HomeScreen(onOpenDetail = { id ->
                        navController.navigate("detail/${'$'}id")
                    }) }
                    composable(
                        "detail/{itemId}",
                        arguments = listOf(navArgument("itemId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        DetailScreen(itemId = backStackEntry.arguments?.getString("itemId"))
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            fun onItemSelected(itemId: String) {
                viewModel.recordItemOpened(itemId)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("navController", "NavHostController", "required", "Controller that owns the back stack and performs navigation."),
            ComponentProperty("startDestination", "String", "required", "Route of the first destination shown."),
            ComponentProperty("route", "String?", "null", "Optional route naming this NavHost's own graph, for nested graphs."),
            ComponentProperty("builder", "NavGraphBuilder.() -> Unit", "required", "Lambda that declares composable() destinations and their routes.")
        ),
        events = listOf("navController.navigate(route) — pushes a new destination onto the back stack."),
        bestPractices = listOf(
            "Define routes as constants or a sealed class rather than raw strings to avoid typos.",
            "Use navArgument to declare typed arguments instead of manually parsing route strings."
        ),
        commonMistakes = listOf(
            "Creating a new NavController on every recomposition instead of using rememberNavController().",
            "Passing complex objects through navigation arguments instead of IDs looked up from a shared data source."
        ),
        accessibilityNotes = listOf(
            "Ensure each destination sets an appropriate screen title/announcement so TalkBack users know navigation occurred.",
            "Back navigation must remain reachable via the system back gesture/button, not only an in-app button."
        ),
        performanceNotes = listOf(
            "Only the active destination's composable is composed; inactive back stack entries are not recomposed.",
            "Avoid heavy work in composable() lambdas directly — hoist it to a ViewModel scoped to the destination."
        ),
        relatedComponentIds = listOf("nav-bottom-bar"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(bottomBar, navHost)
}
