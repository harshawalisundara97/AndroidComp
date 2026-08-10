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

    private val navCustomStyles = ComponentSpec(
        id = "nav-custom-styles",
        category = ComponentCategory.NAVIGATION,
        title = "Custom Navigation Patterns",
        overview = "Five hand-built navigation interactions beyond the default NavigationBar — " +
            "a morphing pill indicator bottom nav, sliding underline tabs, a segmented control, " +
            "a floating pill nav, and a vertical rail for tablet/landscape layouts. Each animates " +
            "its selection state with genuine Compose motion. Copy the code for any pattern to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun MorphingIndicatorBottomNav(selectedIndex: Int, onSelect: (Int) -> Unit) {
                    BoxWithConstraints(Modifier.fillMaxWidth().height(64.dp)) {
                        val itemWidth = maxWidth / icons.size
                        val indicatorOffset by animateDpAsState(
                            itemWidth * selectedIndex,
                            animationSpec = spring(dampingRatio = 0.7f)
                        )
                        Box(
                            Modifier
                                .width(itemWidth)
                                .graphicsLayer { translationX = indicatorOffset.toPx() }
                                .clip(RoundedCornerShape(50))
                                .background(primary)
                        )
                        Row { icons.forEachIndexed { index, icon -> /* tap -> onSelect(index) */ } }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("selectedIndex", "Int", "0", "Index of the currently selected destination/tab/segment."),
            ComponentProperty("onSelect", "(Int) -> Unit", "required", "Called with the tapped index; hoist and drive selectedIndex from it."),
            ComponentProperty("indicator offset", "State<Dp> via animateDpAsState", "n/a", "Computed as itemWidth * selectedIndex inside a BoxWithConstraints to glide the indicator between items."),
            ComponentProperty("BoxWithConstraints", "Composable", "n/a", "Used to measure available width and divide it evenly per item so the indicator offset scales with any screen size.")
        ),
        events = listOf("onSelect(index) — fired when a nav item, tab, segment, or rail destination is tapped."),
        bestPractices = listOf(
            "Compute the sliding indicator's target offset from measured item width (BoxWithConstraints) rather than a hardcoded Dp, so it adapts to any screen size.",
            "Use spring() for the morphing pill indicator to give it a natural, slightly bouncy glide; use tween() for simpler underline/segment slides."
        ),
        commonMistakes = listOf(
            "Animating the indicator's position with Modifier.offset instead of graphicsLayer translationX, which re-triggers layout on every frame.",
            "Hardcoding a fixed indicator width that doesn't match the actual item width, causing visible misalignment on different screen sizes."
        ),
        accessibilityNotes = listOf(
            "Each nav item must expose its selected state via semantics (e.g. Role.Tab with selected) so screen readers announce the current destination.",
            "Icon-only nav patterns (floating pill, rail) still need a contentDescription per icon since no text label is visible."
        ),
        performanceNotes = listOf(
            "animateDpAsState/animateColorAsState per item is cheap for the 3-4 item counts used here; avoid scaling this pattern to long item lists.",
            "graphicsLayer-based translation keeps the sliding indicator on the compositor thread, avoiding unnecessary recomposition of surrounding content."
        ),
        relatedComponentIds = listOf("nav-bottom-bar"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(bottomBar, navHost, navCustomStyles)
}
