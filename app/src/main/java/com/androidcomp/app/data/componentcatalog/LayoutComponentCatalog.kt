package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object LayoutComponentCatalog {

    private val weightedRow = ComponentSpec(
        id = "layout-row-weight",
        category = ComponentCategory.LAYOUTS,
        title = "Row with Weighted Children",
        overview = "Arranges children horizontally, using `Modifier.weight` to proportionally " +
            "distribute remaining space — the Compose equivalent of a LinearLayout with " +
            "layout_weight.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Label", modifier = Modifier.weight(1f))
                    Text("Value", modifier = Modifier.weight(2f))
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal">
                    <TextView
                        android:layout_width="0dp"
                        android:layout_weight="1"
                        android:layout_height="wrap_content"
                        android:text="Label" />
                    <TextView
                        android:layout_width="0dp"
                        android:layout_weight="2"
                        android:layout_height="wrap_content"
                        android:text="Value" />
                </LinearLayout>
            """.trimIndent()
        ),
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("horizontalArrangement", "Arrangement.Horizontal", "Arrangement.Start", "How children are spaced along the main axis."),
            ComponentProperty("verticalAlignment", "Alignment.Vertical", "Alignment.Top", "How children are aligned along the cross axis."),
            ComponentProperty("modifier", "Modifier", "Modifier", "Modifier applied to the Row itself, e.g. fillMaxWidth()."),
            ComponentProperty("content", "@Composable RowScope.() -> Unit", "required", "Children; only usable inside RowScope can call Modifier.weight().")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Use Arrangement.spacedBy() instead of manual Spacer composables between children for consistent gaps.",
            "Reserve Modifier.weight() for children that should share remaining space proportionally; use wrapContent otherwise."
        ),
        commonMistakes = listOf(
            "Calling Modifier.weight() outside a RowScope/ColumnScope receiver — it only compiles inside those scopes.",
            "Combining fillMaxWidth() on a weighted child with another fillMaxWidth() sibling, causing measurement conflicts."
        ),
        accessibilityNotes = listOf(
            "Ensure reading order in a Row matches visual left-to-right order, or set a custom traversal order for RTL locales.",
            "Row does not clip overflow by default — clipped weighted text should use Modifier.weight(1f, fill = true) with ellipsis."
        ),
        performanceNotes = listOf(
            "Row/Column are lightweight single-pass layouts; prefer them over ConstraintLayout for simple linear arrangements."
        ),
        relatedComponentIds = listOf("layout-box-stack"),
        minApi = 21
    )

    private val boxStack = ComponentSpec(
        id = "layout-box-stack",
        category = ComponentCategory.LAYOUTS,
        title = "Box with Layering",
        overview = "Stacks children on top of one another along the z-axis, the Compose " +
            "equivalent of a FrameLayout, commonly used for badges, overlays, and image captions.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Box(modifier = Modifier.size(120.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.banner),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize(),
                        contentScale = ContentScale.Crop
                    )
                    Text(
                        text = "New",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    )
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <FrameLayout
                    android:layout_width="120dp"
                    android:layout_height="120dp">
                    <ImageView
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        android:scaleType="centerCrop"
                        android:src="@drawable/banner" />
                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_gravity="top|end"
                        android:padding="8dp"
                        android:text="New" />
                </FrameLayout>
            """.trimIndent()
        ),
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("contentAlignment", "Alignment", "Alignment.TopStart", "Default alignment applied to children that don't specify their own align()."),
            ComponentProperty("propagateMinConstraints", "Boolean", "false", "Whether Box's incoming min constraints are passed to its children."),
            ComponentProperty("modifier", "Modifier", "Modifier", "Modifier applied to the Box itself, e.g. size()."),
            ComponentProperty("content", "@Composable BoxScope.() -> Unit", "required", "Children; BoxScope provides Modifier.align() and matchParentSize().")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Use Modifier.align() per child inside BoxScope rather than nesting extra Rows/Columns to position overlays.",
            "Use matchParentSize() (not fillMaxSize()) for a child that must size itself to Box's other content, e.g. a background image."
        ),
        commonMistakes = listOf(
            "Relying on child declaration order for stacking without realizing later children always draw on top.",
            "Using Box purely for padding/centering a single child when a simpler Modifier chain would suffice."
        ),
        accessibilityNotes = listOf(
            "Overlapping text and image content must maintain contrast; consider a scrim behind text on top of images.",
            "Ensure touch targets of overlapping interactive children don't unintentionally overlap each other."
        ),
        performanceNotes = listOf(
            "Box performs a single measurement pass per child; avoid deeply nested Box-in-Box hierarchies for complex overlays."
        ),
        relatedComponentIds = listOf("layout-row-weight"),
        minApi = 21
    )

    private val layoutCustomStyles = ComponentSpec(
        id = "layout-custom-styles",
        category = ComponentCategory.LAYOUTS,
        title = "Custom Layout Patterns",
        overview = "Five hand-built layout interactions beyond basic Row/Column/Box arrangement — " +
            "a collapsing header, a staggered grid, a swipeable card stack, an expandable accordion, " +
            "and a sticky-header grouped list. Each responds to real touch input (drag, tap, scroll) " +
            "with genuine Compose animation. Copy the code for any pattern to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun ExpandableAccordionLayout(expandedIndex: Int, onToggle: (Int) -> Unit) {
                    sections.forEachIndexed { index, (title, body) ->
                        val expanded = expandedIndex == index
                        Column {
                            Row(Modifier.clickable { onToggle(index) }) { Text(title) }
                            AnimatedVisibility(visible = expanded, enter = expandVertically() + fadeIn()) {
                                Text(body)
                            }
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("expandedAccordionIndex", "Int", "n/a", "Index of the currently open accordion section; -1 means all collapsed."),
            ComponentProperty("onToggle", "(Int) -> Unit", "n/a", "Called when a section header is tapped; toggles that section open/closed."),
            ComponentProperty("drag gesture state", "Modifier.pointerInput + detectHorizontalDragGestures", "n/a", "Drives the swipeable card stack's live offset and rotation."),
            ComponentProperty("stickyHeader", "LazyListScope.stickyHeader (ExperimentalFoundationApi)", "n/a", "Pins a section header at the top of the list while its items scroll beneath it.")
        ),
        events = listOf("onToggle(index) — fired when an accordion header is tapped."),
        bestPractices = listOf(
            "Prefer graphicsLayer for drag/scale transforms (card stack, collapsing header) so animation runs on the compositor, not layout.",
            "Keep only one accordion section open at a time for scannability, driven by a single expandedIndex rather than per-item booleans."
        ),
        commonMistakes = listOf(
            "Forgetting @OptIn(ExperimentalFoundationApi::class) when using stickyHeader inside a LazyColumn.",
            "Not resetting a dragged card stack's offset back to zero after a drag that didn't cross the dismiss threshold, leaving it stuck off-center."
        ),
        accessibilityNotes = listOf(
            "Accordion headers should expose expanded/collapsed state via semantics (e.g. Modifier.semantics { stateDescription = ... }) for screen reader users.",
            "Swipe-to-dismiss interactions need a non-gesture fallback (e.g. a button) for users who cannot perform drag gestures."
        ),
        performanceNotes = listOf(
            "LazyVerticalStaggeredGrid and LazyColumn only compose visible items, so these patterns scale to long lists without added memory cost.",
            "Avoid recomposing the entire accordion list on every drag frame — scope animated state (like card offset) to the smallest composable possible."
        ),
        relatedComponentIds = listOf("layout-row-weight", "layout-box-stack"),
        minApi = 21
    )

    private val layoutCustomStyles2 = ComponentSpec(
        id = "layout-custom-styles-2",
        category = ComponentCategory.LAYOUTS,
        title = "Custom Layout Patterns II",
        overview = "A second set of five layout interactions distinct from the first batch — a " +
            "drag-to-reorder list controlled by up/down affordances, a pinned two-pane " +
            "master-detail layout, a parallax scrolling header that shrinks and fades on " +
            "scroll, a tabbed content switcher with a directional slide transition, and an " +
            "adaptive card-flow layout built on FlowRow. Each responds to real interaction " +
            "and its Compose code is copyable below.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun ParallaxScrollingHeader() {
                    val scrollState = rememberScrollState()
                    val progress = scrollState.value.toFloat() / scrollState.maxValue

                    Column(Modifier.verticalScroll(scrollState)) {
                        Box(
                            Modifier.graphicsLayer {
                                alpha = 1f - progress * 0.7f
                                scaleX = 1f - progress * 0.15f
                                scaleY = 1f - progress * 0.15f
                            }
                        ) { Text("Header") }
                        // scrollable content below...
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("reorderItems / onMove(from, to)", "List<String> / (Int, Int) -> Unit", "n/a", "Backing list and mutation callback for the drag-to-reorder grid's up/down controls."),
            ComponentProperty("masterSelectedIndex", "Int", "0", "Which master-list row is currently shown in the pinned detail pane."),
            ComponentProperty("rememberScrollState().value / .maxValue", "Int", "n/a", "Derives the parallax header's live shrink/fade progress from 0f to 1f."),
            ComponentProperty("FlowRow", "androidx.compose.foundation.layout.FlowRow", "n/a", "Wraps card children onto new lines automatically to fill available width.")
        ),
        events = listOf("onMove / onSelect / onSelect(tab) / onAdd/onRemove — fired by each design's controls to update its hoisted state."),
        bestPractices = listOf(
            "Prefer explicit move controls (or a well-tested drag library) over hand-rolled pointerInput dragging for reorderable lists — it's far easier to get accessible and correct.",
            "Derive parallax progress from real scroll state rather than a manually tracked offset, so it always matches what's on screen.",
            "Use AnimatedContent's directional slideIn/slideOut pair for tab switches so back-and-forth navigation reads as spatially consistent."
        ),
        commonMistakes = listOf(
            "Computing scroll progress by dividing by a maxValue that can be zero (no scrollable overflow), causing a divide-by-zero/NaN alpha.",
            "Forgetting @OptIn(ExperimentalLayoutApi::class) is no longer required for stable FlowRow, but still using the old accompanist FlowRow leftover in new code.",
            "Letting the master-detail pane's detail content lag one click behind because selection state lives locally instead of being hoisted."
        ),
        accessibilityNotes = listOf(
            "Reorder up/down buttons must have adequate touch targets (48x48dp) and content descriptions like 'Move Alpha up'.",
            "Master-detail layouts should still expose a single logical reading order to TalkBack, not two disconnected panes.",
            "Tab switcher content changes should be announced so screen reader users know new content loaded after a tab tap."
        ),
        performanceNotes = listOf(
            "FlowRow measures children in a single pass and is cheap even as card count grows into the dozens; a LazyVerticalGrid is still preferable for large counts.",
            "Parallax header effects should stay on graphicsLayer properties (alpha/scale) driven by scroll state, never trigger a re-measure per scroll delta.",
            "AnimatedContent disposes the outgoing tab's composition after its exit animation completes — avoid holding heavy state in it that needs to survive."
        ),
        relatedComponentIds = listOf("layout-custom-styles"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(weightedRow, boxStack, layoutCustomStyles, layoutCustomStyles2)
}
