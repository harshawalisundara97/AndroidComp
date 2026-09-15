package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object MaterialComponentCatalog {

    private val card = ComponentSpec(
        id = "material-card",
        category = ComponentCategory.MATERIAL_COMPONENTS,
        title = "Card",
        overview = "A surface that groups related content and actions, with elevation or " +
            "an outline to visually separate it from the background.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Card(
                    onClick = { onCardClicked() },
                    shape = RoundedCornerShape(24.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Card title", style = MaterialTheme.typography.titleMedium)
                        Text("Supporting text", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.card.MaterialCardView
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    app:cardCornerRadius="24dp"
                    app:cardElevation="2dp">
                    <!-- card content -->
                </com.google.android.material.card.MaterialCardView>
            """.trimIndent()
        ),
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("onClick", "(() -> Unit)?", "none (use Card() overload without onClick for non-interactive cards)", "Makes the whole card tappable when provided."),
            ComponentProperty("shape", "Shape", "CardDefaults.shape", "Corner shape of the card surface."),
            ComponentProperty("colors", "CardColors", "CardDefaults.cardColors()", "Container and content colors."),
            ComponentProperty("elevation", "CardElevation", "CardDefaults.cardElevation()", "Shadow elevation per interaction state."),
            ComponentProperty("border", "BorderStroke?", "null", "Optional outline stroke, used for OutlinedCard-style treatments.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation, only when the interactive Card overload is used."),
        bestPractices = listOf(
            "Use a consistent corner radius across all cards in the app (per this app's design system, 24dp).",
            "Keep card padding at 16-20dp so content doesn't feel cramped against the edges."
        ),
        commonMistakes = listOf(
            "Nesting a clickable Card inside another clickable container, creating ambiguous or conflicting touch targets.",
            "Stacking heavy shadows on many cards in a list, which reads as cluttered rather than premium."
        ),
        accessibilityNotes = listOf(
            "When using the clickable Card overload, the whole card becomes a single touch target — ensure it's at least 48dp tall.",
            "Group the card's text content so screen readers announce it as one coherent unit, not fragmented lines."
        ),
        performanceNotes = listOf(
            "Elevation/shadows add a compositing cost — keep default elevation low (per design system: soft, low elevation only).",
            "In lists, prefer LazyColumn with stable keys so Card items aren't needlessly recomposed on scroll."
        ),
        relatedComponentIds = listOf("material-badge", "material-chip"),
        minApi = 21
    )

    private val chip = ComponentSpec(
        id = "material-chip",
        category = ComponentCategory.MATERIAL_COMPONENTS,
        title = "Chip (Assist / Filter)",
        overview = "Compact elements representing an input, attribute, or filter action; " +
            "AssistChip triggers an action, FilterChip toggles a filter on/off.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var selected by remember { mutableStateOf(false) }
                FilterChip(
                    selected = selected,
                    onClick = { selected = !selected },
                    label = { Text("Wireless") },
                    leadingIcon = if (selected) {
                        { Icon(Icons.Filled.Check, contentDescription = null) }
                    } else null
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.chip.Chip
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Wireless"
                    app:checkable="true" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onFilterToggled(filter: String, isSelected: Boolean) {
                viewModel.updateFilter(filter, isSelected)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("selected", "Boolean", "required (FilterChip)", "Whether the filter is currently active."),
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the chip is tapped."),
            ComponentProperty("label", "@Composable () -> Unit", "required", "Text content shown inside the chip."),
            ComponentProperty("leadingIcon", "@Composable (() -> Unit)?", "null", "Optional icon shown before the label, e.g. a checkmark when selected."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation, typically toggling selected state for FilterChip."),
        bestPractices = listOf(
            "Use FilterChip for multi-select filtering UIs and AssistChip for one-off suggested actions.",
            "Lay chips out in a FlowRow so they wrap naturally instead of overflowing horizontally."
        ),
        commonMistakes = listOf(
            "Using a Chip where a Button was intended for a primary action — chips are for compact, secondary interactions.",
            "Not reflecting the selected state visually (leading check icon or color change), confusing users about active filters."
        ),
        accessibilityNotes = listOf(
            "FilterChip's selected state must be exposed to accessibility services, which Compose's selected parameter handles automatically.",
            "Ensure the chip's touch target is at least 48dp tall even though its visual height is often smaller."
        ),
        performanceNotes = listOf(
            "In a row of many chips, use LazyRow instead of a plain Row to avoid measuring/composing all chips upfront.",
            "Avoid recreating the leadingIcon lambda instance unnecessarily; keep it stable across recompositions where possible."
        ),
        relatedComponentIds = listOf("material-card", "material-badge"),
        minApi = 21
    )

    private val badge = ComponentSpec(
        id = "material-badge",
        category = ComponentCategory.MATERIAL_COMPONENTS,
        title = "Badge",
        overview = "A small marker, typically overlaid on an icon or NavigationBarItem, used to " +
            "convey a status or count (e.g. unread notifications).",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                BadgedBox(
                    badge = { Badge { Text("3") } }
                ) {
                    Icon(Icons.Filled.Notifications, contentDescription = "Notifications")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <!-- BadgeDrawable is attached programmatically to a view, e.g.: -->
                BadgeDrawable badge = BadgeDrawable.create(context);
                badge.setNumber(3);
                BadgeUtils.attachBadgeDrawable(badge, iconView);
            """.trimIndent()
        ),
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("badge", "@Composable (() -> Unit)?", "null", "The badge content, usually a Badge composable, shown atop the box's content."),
            ComponentProperty("containerColor", "Color", "MaterialTheme.colorScheme.error", "Background color of the Badge itself (on the Badge composable)."),
            ComponentProperty("contentColor", "Color", "contentColorFor(containerColor)", "Color of the text/number inside the Badge."),
            ComponentProperty("content", "@Composable BoxScope.() -> Unit", "required", "The anchor content the badge is overlaid on, e.g. an Icon.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Keep badge counts short (e.g. '9+' instead of large numbers) so the badge stays compact and legible.",
            "Use a dot-only Badge (no text) when only presence/absence of new content matters, not an exact count."
        ),
        commonMistakes = listOf(
            "Using Badge as a standalone clickable element — it is a passive status indicator, not an interactive component.",
            "Placing a Badge without sufficient contrast against its anchor icon's color, hurting legibility."
        ),
        accessibilityNotes = listOf(
            "Badge content is not automatically announced with context — pair with a contentDescription on the anchor (e.g. 'Notifications, 3 unread').",
            "Do not rely on color alone (e.g. red dot) to convey urgency; ensure the anchor icon's semantics also communicate it."
        ),
        performanceNotes = listOf(
            "BadgedBox adds a lightweight overlay layout; negligible cost compared to the anchor content itself.",
            "Avoid recomposing the badge count on every frame — derive it from a stable, debounced state source."
        ),
        relatedComponentIds = listOf("material-chip", "material-card"),
        minApi = 21
    )

    private val cardStyles = ComponentSpec(
        id = "material-card-styles",
        category = ComponentCategory.MATERIAL_COMPONENTS,
        title = "Custom Card Styles",
        overview = "Five custom card designs beyond the standard Material 3 Card — a stat " +
            "card, image card, gradient card, minimal bordered card, and elevated interactive " +
            "card — each demonstrating a different real-world use case and motion feel. Tap " +
            "each one to see it respond, and copy its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun CustomCard(onClick: () -> Unit) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(containerColor)
                            .clickable { onClick() }
                            .padding(20.dp)
                    ) {
                        Column {
                            Text(title)
                            Text(subtitle)
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("shape", "RoundedCornerShape", "24.dp (this app's card radius)", "Corner radius; kept consistent across all 5 styles per the app's design system."),
            ComponentProperty("elevation animation", "State<Dp> via animateDpAsState", "varies per style", "Drives the press-in/press-out shadow feel on the Gradient and Elevated Interactive styles."),
            ComponentProperty("onClick", "() -> Unit", "required (for interactive styles)", "Called on tap; the Stat and Image cards use it to update local state (count, saved flag).")
        ),
        events = listOf("onClick — fired on tap, same contract as a standard clickable Card."),
        bestPractices = listOf(
            "Keep a consistent corner radius across all custom card variants in one app, even when their fill/border treatment differs.",
            "Reserve gradient/glow treatments for a small number of high-emphasis cards (e.g. a premium upsell) — using them everywhere dilutes the effect."
        ),
        commonMistakes = listOf(
            "Building a fully custom clickable card without Modifier.semantics/Role.Button-equivalent handling, leaving it invisible to TalkBack as an actionable element.",
            "Stacking heavy shadows on many custom cards in a scrolling list, hurting both performance and visual calm."
        ),
        accessibilityNotes = listOf(
            "A clickable custom card should expose a meaningful merged content description (e.g. \"View Analytics, updated 2 minutes ago\") rather than reading each Text child separately.",
            "Ensure text contrast holds up against gradient/colored backgrounds specifically — check both ends of a gradient, not just the average color."
        ),
        performanceNotes = listOf(
            "Prefer Modifier.shadow's ambientColor/spotColor over stacking multiple background layers to fake elevation — it's a single compositor-backed effect.",
            "For a scrolling grid of custom cards, ensure each card's internal state (like the Stat card's counter) is scoped narrowly so scrolling doesn't trigger unrelated recomposition."
        ),
        relatedComponentIds = listOf("material-card"),
        minApi = 21
    )

    private val customStyles = ComponentSpec(
        id = "material-custom-styles",
        category = ComponentCategory.MATERIAL_COMPONENTS,
        title = "Custom Material Component Styles",
        overview = "Five custom-designed general Material Components beyond the standard set — " +
            "a multi-select filter chip group with animated checkmarks, a FAB that expands into " +
            "a staggered speed-dial of mini-FABs, a draggable bottom sheet with a pill grab " +
            "handle, a notification badge that pulses while unread, and an iOS-style segmented " +
            "button group with a sliding highlight. Tap or drag each demo to see it respond, and " +
            "copy its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun SlidingSegmentedButtons(selectedIndex: Int, onSelect: (Int) -> Unit) {
                    val offsetX by animateDpAsState(segmentWidth * selectedIndex)
                    Box(Modifier.clip(RoundedCornerShape(14.dp)).background(trackColor)) {
                        Box(
                            Modifier
                                .offset(x = offsetX)
                                .width(segmentWidth)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color.White)
                        )
                        Row {
                            labels.forEachIndexed { index, label ->
                                Box(Modifier.width(segmentWidth).clickable { onSelect(index) }) {
                                    Text(label)
                                }
                            }
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("selectedChips / selectedSegment", "Set<String> / Int", "required", "Hoisted selection state driving each design's animated fill and offset."),
            ComponentProperty("speedDialExpanded", "Boolean", "false", "Toggles the staggered AnimatedVisibility reveal of the 3 mini-FABs."),
            ComponentProperty("badgeUnread", "Boolean", "true", "Drives the rememberInfiniteTransition pulse animation on the badge dot."),
            ComponentProperty("dragOffset", "Float (local state)", "0f", "Tracks the bottom sheet handle's vertical drag distance to decide dismissal."),
            ComponentProperty("onSelect / onToggle", "(Int) -> Unit / () -> Unit", "required", "Callbacks that update the hoisted ViewModel state for each design.")
        ),
        events = listOf("onToggle (chip, badge, FAB), onSelect (segmented buttons), onShow/onDismiss (bottom sheet) — fired on tap or drag-release."),
        bestPractices = listOf(
            "Drive sliding/offset highlights (segmented buttons, thumb positions) with animateDpAsState instead of manual pixel math so they stay smooth across rapid taps.",
            "Stagger speed-dial mini-FAB reveals with a small per-item delayMillis so the expansion reads as a sequence rather than a single flat pop.",
            "Reserve infinite pulse/glow animations (like the unread badge) for states that truly need ongoing attention — stop them once the state is resolved.",
            "Keep the bottom sheet's dismiss drag threshold generous enough (e.g. 80dp+) to avoid accidental dismissal from small drags."
        ),
        commonMistakes = listOf(
            "Forgetting to coerce drag offsets, letting a bottom sheet handle be dragged upward past its resting position.",
            "Using Icons.Outlined.* for directional arrows in a speed dial or chip instead of the AutoMirrored variants, breaking RTL layouts.",
            "Leaving an infinite badge pulse animation running even after the badge is dismissed/read, wasting recomposition cycles.",
            "Not resetting speed-dial expansion state when the FAB's parent screen is navigated away from, leaving mini-FABs stuck open on return."
        ),
        accessibilityNotes = listOf(
            "Filter chips must expose their selected state (Compose's selected/toggleable modifiers handle this for TalkBack automatically).",
            "The draggable bottom sheet should also offer a non-drag dismiss path (e.g. a close button or scrim tap) for users who cannot perform drag gestures.",
            "An unread badge's pulsing motion is decorative only — pair it with a contentDescription like 'Notifications, unread' so the state isn't conveyed by animation alone.",
            "Segmented buttons should be grouped with a single accessible role so screen readers announce them as one control with 4 selectable options, not 4 separate buttons."
        ),
        performanceNotes = listOf(
            "rememberInfiniteTransition (the pulsing badge) keeps animating for as long as it's composed — gate it behind the unread boolean so it stops once read.",
            "Prefer animateDpAsState/animateFloatAsState over recomposition-driven offset recalculation for the segmented highlight and speed-dial rotation.",
            "detectDragGestures on the bottom sheet handle should consume only the pointer events it needs, avoiding interference with nested scrollable content.",
            "Keep the speed-dial's mini-FAB count small (3-5); more than that is better served by a bottom sheet or menu for both usability and animation cost."
        ),
        relatedComponentIds = listOf("material-chip", "material-badge", "material-card-styles"),
        minApi = 21
    )

    private val customStyles2 = ComponentSpec(
        id = "material-custom-styles-2",
        category = ComponentCategory.MATERIAL_COMPONENTS,
        title = "Custom Material Component Styles II",
        overview = "A second batch of five custom Material Components, distinct from the first " +
            "set and from the Card showcase — a DatePicker-styled calendar month grid with an " +
            "animated selection circle, a TimePicker-styled analog clock dial whose hand rotates " +
            "to the tapped hour, a vertical NavigationRail with a pill indicator that slides " +
            "between destinations, a Snackbar with an action that can also be swiped away with " +
            "a fading offset, and an ExposedDropdownMenu-style select field whose chevron " +
            "rotates as its option list expands. Tap, drag, or swipe each demo to see it " +
            "respond, and copy its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun AnimatedDropdownSelect(
                    expanded: Boolean,
                    selected: String,
                    onToggle: () -> Unit,
                    onSelectOption: (String) -> Unit
                ) {
                    val chevronRotation by animateFloatAsState(if (expanded) 180f else 0f)
                    Row(Modifier.clickable { onToggle() }) {
                        Text(selected)
                        Icon(
                            Icons.Outlined.ExpandMore,
                            contentDescription = null,
                            modifier = Modifier.rotate(chevronRotation)
                        )
                    }
                    AnimatedVisibility(visible = expanded) {
                        Column {
                            options.forEach { option ->
                                Row(Modifier.clickable { onSelectOption(option) }) { Text(option) }
                            }
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("selectedDate / selectedHour", "Int", "required", "Hoisted selection driving the calendar grid's highlighted day and the clock dial's hand angle."),
            ComponentProperty("selectedRailIndex", "Int", "0", "Index of the active NavigationRail destination; drives the animated pill offset."),
            ComponentProperty("snackbarVisible", "Boolean", "false", "Controls the AnimatedVisibility wrapping the swipe-to-dismiss Snackbar."),
            ComponentProperty("expanded / selectedOption", "Boolean / String", "false / required", "Drives the dropdown's chevron rotation and its expand/collapse of the option list."),
            ComponentProperty("onSelectDate / onSelectHour / onSelect / onDismiss / onToggle / onSelectOption", "lambdas", "required", "Callbacks that update the hoisted ViewModel state for each of the 5 designs.")
        ),
        events = listOf("onSelectDate / onSelectHour (calendar, clock), onSelect (nav rail), onShow/onDismiss (snackbar), onToggle/onSelectOption (dropdown) — fired on tap, drag-release, or swipe."),
        bestPractices = listOf(
            "Drive the clock hand and nav-rail pill with animateFloatAsState/animateDpAsState rather than snapping, so repeated taps feel continuous rather than jumpy.",
            "Use an Animatable (not just animate*AsState) for the swipeable Snackbar so mid-gesture drag offsets and the post-release snap-back/dismiss share one coherent animation state.",
            "Keep the calendar grid's per-day recomposition cheap — only the selected/unselected two cells actually change color, so avoid recomposing the whole grid on selection.",
            "Rotate the dropdown chevron with a plain rotate() Modifier driven by animateFloatAsState instead of swapping icons, so the transition reads as one continuous motion."
        ),
        commonMistakes = listOf(
            "Computing the clock dial's hour-label positions with raw pixel math instead of Dp-aware trigonometry, causing misaligned labels on different screen densities.",
            "Forgetting to coerce the Snackbar's drag offset/alpha, letting it fully disappear without ever calling onDismiss or getting stuck semi-transparent.",
            "Leaving the dropdown's option list attached to the composition tree (visibility=false via a boolean flag) instead of using AnimatedVisibility, losing the expand/collapse animation.",
            "Not resetting the NavigationRail's indicator position when the destination list changes length, leaving the pill offset pointing at a stale index."
        ),
        accessibilityNotes = listOf(
            "The calendar grid's day cells and the clock dial's hour targets must each be at least 48dp of touch target even where the visible circle is smaller.",
            "A swipe-to-dismiss Snackbar must also be dismissible without a gesture — always keep a tappable close/UNDO action alongside the swipe.",
            "NavigationRail items should expose their selected state via Compose's selected semantics so TalkBack announces which destination is active.",
            "The dropdown's expanded/collapsed state should be exposed via appropriate semantics (e.g. expanded property) so screen readers announce it as one control, not a button plus a hidden list."
        ),
        performanceNotes = listOf(
            "The clock dial's Canvas redraws only its hand's angle-dependent geometry each frame; keep the static track/hour-label layer out of the per-frame draw scope where possible.",
            "Prefer Animatable.snapTo during an active drag (not animateTo) to avoid stacking animation jobs on every pointer move in the swipeable Snackbar.",
            "animateDpAsState on the NavigationRail's indicator is cheap for 4-5 destinations; for a much longer rail, prefer a LazyColumn-friendly approach.",
            "AnimatedVisibility on the dropdown's option list only composes those rows while expanded, so collapsed state has near-zero layout cost."
        ),
        relatedComponentIds = listOf("material-custom-styles", "material-card-styles"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(card, chip, badge, cardStyles, customStyles, customStyles2)
}
