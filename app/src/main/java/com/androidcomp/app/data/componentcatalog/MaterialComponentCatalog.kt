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

    val all: List<ComponentSpec> = listOf(card, chip, badge, cardStyles)
}
