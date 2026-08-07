package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object ListComponentCatalog {

    private val lazyColumn = ComponentSpec(
        id = "list-lazy-column",
        category = ComponentCategory.LISTS,
        title = "LazyColumn",
        overview = "Renders a vertically scrolling list that only composes and lays out items " +
            "currently visible on screen, the Compose equivalent of RecyclerView with a " +
            "LinearLayoutManager.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = contacts, key = { it.id }) { contact ->
                        ContactRow(contact = contact)
                    }
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <androidx.recyclerview.widget.RecyclerView
                    android:id="@+id/contactsList"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent"
                    android:clipToPadding="false"
                    android:paddingVertical="16dp" />
            """.trimIndent()
        ),
        viewModelUsage = """
            val contacts: StateFlow<List<Contact>> = contactRepository.observeContacts()
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("contentPadding", "PaddingValues", "PaddingValues(0.dp)", "Padding applied around the list's content, inside its scroll bounds."),
            ComponentProperty("state", "LazyListState", "rememberLazyListState()", "Controls and observes scroll position; hoist to control scrolling programmatically."),
            ComponentProperty("verticalArrangement", "Arrangement.Vertical", "Arrangement.Top", "Spacing/arrangement of items along the scroll axis."),
            ComponentProperty("reverseLayout", "Boolean", "false", "Reverses the direction of scrolling and item layout.")
        ),
        events = listOf("Scroll position changes are observable via the hoisted LazyListState."),
        bestPractices = listOf(
            "Always pass a stable `key` in items()/itemsIndexed() so Compose can correctly track item identity across list mutations.",
            "Hoist and reuse a single LazyListState across recompositions via rememberLazyListState() to preserve scroll position."
        ),
        commonMistakes = listOf(
            "Nesting a LazyColumn inside another scrollable Column without constraining its height, causing a crash or infinite height.",
            "Omitting `key` in items(), which causes item state (e.g. TextField focus) to shift unexpectedly when the list mutates."
        ),
        accessibilityNotes = listOf(
            "Each item row should be a single merged accessibility node describing its full content, not scattered separate nodes.",
            "TalkBack announces list size/position ('item 3 of 20') automatically for LazyColumn — avoid duplicating that in row content."
        ),
        performanceNotes = listOf(
            "Only visible items (plus a small buffer) are composed and measured — safe for very large or unbounded datasets.",
            "Avoid heavy work (e.g. bitmap decoding) directly in the item lambda; hoist it to a remembered derived state or ViewModel."
        ),
        relatedComponentIds = listOf("list-lazy-row"),
        minApi = 21
    )

    private val lazyRow = ComponentSpec(
        id = "list-lazy-row",
        category = ComponentCategory.LISTS,
        title = "LazyRow",
        overview = "Renders a horizontally scrolling list that only composes visible items, " +
            "commonly used for carousels, chip rows, and horizontal image galleries.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = categories, key = { it.id }) { category ->
                        CategoryChip(category = category)
                    }
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <androidx.recyclerview.widget.RecyclerView
                    android:id="@+id/categoryList"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="horizontal"
                    android:clipToPadding="false"
                    android:paddingHorizontal="16dp" />
            """.trimIndent()
        ),
        viewModelUsage = """
            val categories: StateFlow<List<Category>> = catalogRepository.observeCategories()
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("contentPadding", "PaddingValues", "PaddingValues(0.dp)", "Padding applied around the list's content along the scroll axis."),
            ComponentProperty("state", "LazyListState", "rememberLazyListState()", "Controls and observes horizontal scroll position."),
            ComponentProperty("horizontalArrangement", "Arrangement.Horizontal", "Arrangement.Start", "Spacing/arrangement of items along the scroll axis."),
            ComponentProperty("flingBehavior", "FlingBehavior", "ScrollableDefaults.flingBehavior()", "Controls fling/deceleration behavior after a swipe.")
        ),
        events = listOf("Scroll position changes are observable via the hoisted LazyListState."),
        bestPractices = listOf(
            "Use contentPadding rather than Spacer items to add leading/trailing space, so it participates correctly in fling/scroll math.",
            "Consider snapping (rememberSnapFlingBehavior) for carousel-style LazyRows so items settle into alignment after a swipe."
        ),
        commonMistakes = listOf(
            "Placing a LazyRow inside a horizontally scrollable parent, causing gesture conflicts between the two scroll containers.",
            "Forgetting fillMaxWidth() on the parent, causing the LazyRow to size to its content instead of the available width."
        ),
        accessibilityNotes = listOf(
            "Horizontal lists are harder to discover for screen reader/switch-access users — ensure a visible scroll affordance.",
            "In RTL locales, verify item order and swipe direction visually match the locale's expected reading direction."
        ),
        performanceNotes = listOf(
            "Same windowed composition benefits as LazyColumn — safe for long horizontal datasets without added memory cost."
        ),
        relatedComponentIds = listOf("list-lazy-column"),
        minApi = 21
    )

    private val listCustomStyles = ComponentSpec(
        id = "list-custom-styles",
        category = ComponentCategory.LISTS,
        title = "Custom List Row Patterns",
        overview = "Five hand-built list row interactions beyond a plain LazyColumn row — swipe " +
            "to delete, an expandable detail row, drag-to-reorder controls, a grouped sectioned " +
            "list, and a connected timeline. Each responds to real touch input with genuine " +
            "Compose animation. Copy the code for any pattern to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun SwipeToDeleteRow(item: SwipeItem, onDelete: () -> Unit) {
                    var offsetX by remember { mutableFloatStateOf(0f) }
                    val animatedOffsetX by animateFloatAsState(offsetX)

                    Box(
                        Modifier
                            .graphicsLayer { translationX = animatedOffsetX }
                            .pointerInput(item.id) {
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        if (offsetX < deleteThreshold) onDelete() else offsetX = 0f
                                    },
                                    onHorizontalDrag = { change, dragAmount ->
                                        change.consume()
                                        offsetX = (offsetX + dragAmount).coerceAtMost(0f)
                                    }
                                )
                            }
                    ) {
                        Text(item.label)
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("swipeItems", "List<SwipeItem>", "n/a", "Backing list for the swipe-to-delete row; items are removed on a threshold-crossing drag."),
            ComponentProperty("expandedRowId", "Int?", "null", "Id of the currently expanded detail row, or null if all are collapsed."),
            ComponentProperty("onMove", "(Int, Int) -> Unit", "n/a", "Called with (fromIndex, toIndex) when the reorder controls swap two items."),
            ComponentProperty("dotColor", "Color per timeline entry", "n/a", "Marks a timeline row as completed, active, or pending via its dot color.")
        ),
        events = listOf(
            "onDelete(id) — fired once a swipe-to-delete drag crosses the dismiss threshold.",
            "onToggle(id) — fired when an expandable row is tapped.",
            "onMove(from, to) — fired when a reorder control is tapped."
        ),
        bestPractices = listOf(
            "Drive swipe-to-delete offset through graphicsLayer translationX, not layout offset, to keep the drag on the compositor thread.",
            "Always pass a stable key to items() in lists that reorder or delete, so item identity and internal state survive the mutation."
        ),
        commonMistakes = listOf(
            "Not clamping swipe offset (coerceAtMost(0f)) which lets the row drag past the screen edge or reverse direction unexpectedly.",
            "Reordering a list by mutating indices directly instead of removing then re-inserting the item, which can silently drop or duplicate entries."
        ),
        accessibilityNotes = listOf(
            "Swipe-to-delete needs a non-gesture fallback (e.g. a long-press menu or visible delete icon) for users who cannot perform drag gestures.",
            "Reorder controls (up/down buttons) are more accessible than drag-only reordering — keep them even if drag gestures are also supported."
        ),
        performanceNotes = listOf(
            "Prefer animateContentSize() over manual height animation for the expandable row — it measures once and animates the size delta automatically.",
            "Keep timeline/grouped list rendering flat (no nested LazyColumns) since the item counts here are small and don't need lazy windowing."
        ),
        relatedComponentIds = listOf("list-lazy-column"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(lazyColumn, lazyRow, listCustomStyles)
}
