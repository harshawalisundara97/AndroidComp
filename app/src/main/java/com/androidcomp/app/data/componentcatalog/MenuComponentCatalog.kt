package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object MenuComponentCatalog {

    private val dropdownMenu = ComponentSpec(
        id = "menu-dropdown",
        category = ComponentCategory.MENUS,
        title = "Dropdown Menu",
        overview = "A transient popup menu of options anchored to a trigger element, such as " +
            "an overflow icon button in a toolbar or a list item's context menu.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var expanded by remember { mutableStateOf(false) }
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Filled.MoreVert, contentDescription = "More options")
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text("Share") }, onClick = {
                            expanded = false
                            onShare()
                        })
                        DropdownMenuItem(text = { Text("Delete") }, onClick = {
                            expanded = false
                            onDelete()
                        })
                    }
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <menu xmlns:android="http://schemas.android.com/apk/res/android">
                    <item android:id="@+id/action_share" android:title="Share" />
                    <item android:id="@+id/action_delete" android:title="Delete" />
                </menu>
                <!-- Inflated via PopupMenu(context, anchorView).apply { inflate(R.menu.item_menu) } -->
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onDeleteSelected(itemId: String) {
                viewModel.deleteItem(itemId)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("expanded", "Boolean", "required", "Whether the menu is currently visible."),
            ComponentProperty("onDismissRequest", "() -> Unit", "required", "Called when the user taps outside the menu or presses back."),
            ComponentProperty("offset", "DpOffset", "DpOffset(0.dp, 0.dp)", "Pixel offset applied to the menu's position relative to its anchor."),
            ComponentProperty("properties", "PopupProperties", "PopupProperties()", "Controls focusability and dismiss behavior of the underlying popup.")
        ),
        events = listOf("onDismissRequest — fired on outside tap/back; each DropdownMenuItem fires its own onClick."),
        bestPractices = listOf(
            "Always set expanded = false inside each item's onClick so the menu closes after a selection.",
            "Anchor the DropdownMenu inside the same Box as its trigger so positioning is automatic."
        ),
        commonMistakes = listOf(
            "Forgetting to collapse the menu after a selection, leaving it open over the new screen state.",
            "Using DropdownMenu for a large number of options — consider a bottom sheet or full list screen instead."
        ),
        accessibilityNotes = listOf(
            "The anchoring IconButton must have a contentDescription describing that it opens a menu (e.g. 'More options').",
            "Menu items are announced as list items by TalkBack; keep labels short and unambiguous."
        ),
        performanceNotes = listOf(
            "Menu content is only composed while expanded is true, so it adds no cost when closed.",
            "Avoid rebuilding the item list from scratch on every recomposition; hoist a stable list of options."
        ),
        relatedComponentIds = listOf("menu-exposed-dropdown"),
        minApi = 21
    )

    private val exposedDropdown = ComponentSpec(
        id = "menu-exposed-dropdown",
        category = ComponentCategory.MENUS,
        title = "Exposed Dropdown Menu",
        overview = "A text field paired with a dropdown menu of selectable options, used for " +
            "single-selection fields like 'Country' or 'Category' where typing isn't required.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var expanded by remember { mutableStateOf(false) }
                var selected by remember { mutableStateOf(options.first()) }

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    TextField(
                        value = selected,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor()
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        options.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = { selected = option; expanded = false }
                            )
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.textfield.TextInputLayout
                    style="@style/Widget.Material3.TextInputLayout.OutlinedBox.ExposedDropdownMenu"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content">
                    <AutoCompleteTextView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:inputType="none" />
                </com.google.android.material.textfield.TextInputLayout>
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onCategorySelected(category: String) {
                viewModel.setSelectedCategory(category)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("expanded", "Boolean", "required", "Whether the dropdown menu is currently visible."),
            ComponentProperty("onExpandedChange", "(Boolean) -> Unit", "required", "Called when the box is tapped, toggling expanded state."),
            ComponentProperty("modifier", "Modifier", "Modifier", "Must include Modifier.menuAnchor() on the anchor TextField."),
            ComponentProperty("readOnly", "Boolean", "false", "Set true on the TextField when options are selection-only, not free text.")
        ),
        events = listOf("onExpandedChange — fired on anchor tap; each DropdownMenuItem's onClick fires on selection."),
        bestPractices = listOf(
            "Use readOnly = true on the TextField when users must pick from the list rather than type a custom value.",
            "Always apply Modifier.menuAnchor() to the anchor field or the menu will not align correctly."
        ),
        commonMistakes = listOf(
            "Omitting Modifier.menuAnchor(), which breaks the menu's positioning relative to the text field.",
            "Not updating onExpandedChange state after a selection, leaving the box stuck open."
        ),
        accessibilityNotes = listOf(
            "The trailing dropdown icon should not be the only affordance — the whole field must be tappable to expand.",
            "Announce the field's purpose via a label (e.g. TextField's label parameter), not placeholder text alone."
        ),
        performanceNotes = listOf(
            "For large option lists, prefer a lazily-composed list inside the menu rather than mapping all options eagerly.",
            "Selection state should be hoisted to the caller/ViewModel to avoid losing it across configuration changes."
        ),
        relatedComponentIds = listOf("menu-dropdown"),
        minApi = 21
    )

    private val menuCustomStyles = ComponentSpec(
        id = "menu-custom-styles",
        category = ComponentCategory.MENUS,
        title = "Custom Menu Styles",
        overview = "Five fully custom-designed menu patterns beyond the standard DropdownMenu — " +
            "a bottom sheet action list, an icon-led context menu, a radial FAB menu, a " +
            "checkmark-driven segmented dropdown, and an expandable nested submenu. Tap each " +
            "one below to see it in action, and copy its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun SegmentedDropdown(selected: String, onSelectedChange: (String) -> Unit) {
                    var expanded by remember { mutableStateOf(false) }
                    Box {
                        OutlinedButton(onClick = { expanded = true }) { Text(selected) }
                        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    trailingIcon = { if (option == selected) Icon(Icons.Outlined.Check, null) },
                                    onClick = { expanded = false; onSelectedChange(option) }
                                )
                            }
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("sheetState", "SheetState via rememberModalBottomSheetState()", "n/a", "Drives the ModalBottomSheet's expand/collapse animation for the bottom sheet design."),
            ComponentProperty("expanded", "Boolean", "required", "Controls visibility for the DropdownMenu-based designs (context menu, segmented dropdown, nested submenu)."),
            ComponentProperty("offsetX / offsetY animation", "State<Float> via animateFloatAsState + spring()", "n/a", "Positions each radial menu action along an arc computed from sin/cos of its angle."),
            ComponentProperty("submenuOpen", "Boolean", "false", "Toggles inline expansion of a nested submenu without dismissing the parent DropdownMenu.")
        ),
        events = listOf("onSelectionChange / onSelectedChange — fired when an item in a given menu design is chosen."),
        bestPractices = listOf(
            "Use ModalBottomSheet instead of DropdownMenu once an action list grows past a handful of items or needs icons and descriptions.",
            "For radial menus, keep the arc to 3-4 actions max — beyond that, targets become too small and close together to tap reliably."
        ),
        commonMistakes = listOf(
            "Forgetting to reset a nested submenu's open state in onDismissRequest, so it reopens already-expanded the next time the parent menu is shown.",
            "Animating radial menu items with a plain tween instead of spring(), which reads as mechanical rather than a natural 'pop out' motion."
        ),
        accessibilityNotes = listOf(
            "Every trigger (bottom sheet button, context menu button, FAB, dropdown button) needs a clear contentDescription or visible label describing what it opens.",
            "Radial menu action buttons must stay at least 44-48dp so they remain tappable despite being visually smaller than the central FAB."
        ),
        performanceNotes = listOf(
            "ModalBottomSheet content composes only while shown, same as DropdownMenu — no cost while closed.",
            "Keep the radial menu's per-item animateFloatAsState calls independent (one per action) rather than deriving all positions from a single shared Animatable to keep spring physics per-item natural."
        ),
        relatedComponentIds = listOf("menu-dropdown", "menu-exposed-dropdown"),
        minApi = 21
    )

    private val menuCustomStyles2 = ComponentSpec(
        id = "menu-custom-styles-2",
        category = ComponentCategory.MENUS,
        title = "Custom Menu Styles II",
        overview = "Five more custom-designed menu patterns distinct from the first batch — a " +
            "searchable dropdown for jumping to a destination by typing, an icon-grid quick-actions " +
            "menu, a cascading breadcrumb menu that drills into nested categories, a toolbar overflow " +
            "menu carrying a clearable notification badge, and a horizontally swipeable carousel of " +
            "sort options acting as a lightweight menu. Tap each one below to see it in action, and " +
            "copy its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun ToolbarOverflowBadgeMenu(count: Int, onClear: () -> Unit) {
                    var expanded by remember { mutableStateOf(false) }
                    BadgedBox(badge = { if (count > 0) Badge { Text(count.toString()) } }) {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Outlined.MoreVert, contentDescription = "Overflow menu")
                        }
                    }
                    if (expanded) {
                        Card {
                            Row(Modifier.clickable { onClear(); expanded = false }) {
                                Text("Clear notifications")
                            }
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("query", "String", "\"\"", "Current text typed into the searchable dropdown's filter field."),
            ComponentProperty("path", "List<String>", "listOf(\"Home\")", "Breadcrumb stack for the cascading menu; tapping a segment truncates back to it."),
            ComponentProperty("badgeCount", "Int", "0", "Notification count shown on the toolbar overflow menu's badge; clears to 0 on action."),
            ComponentProperty("selectedIndex", "Int", "0", "Currently highlighted option in the swipeable carousel picker menu.")
        ),
        events = listOf("onQueryChange / onPush / onPopTo / onClear / onSelect — fired by each design's respective interaction."),
        bestPractices = listOf(
            "Debounce or simply filter in-memory (as shown) for searchable menus with small option counts; move to a real search API for large datasets.",
            "Keep icon-grid quick-actions to at most 6 items so they fit two or three rows without scrolling.",
            "Clear a toolbar badge's count as soon as its action is acknowledged so it doesn't feel stuck.",
            "Use animateContentSize() on cascading breadcrumb containers so height changes between levels animate smoothly."
        ),
        commonMistakes = listOf(
            "Forgetting to reset the search query when the menu closes, so it reopens pre-filtered unexpectedly.",
            "Letting a breadcrumb path grow unbounded without a way to jump back multiple levels at once.",
            "Using a LazyRow carousel menu without a visual selected-state indicator, leaving users unsure what's active.",
            "Not disabling the badge-clearing action when the count is already zero."
        ),
        accessibilityNotes = listOf(
            "The searchable dropdown's text field needs a descriptive placeholder or label so TalkBack announces its purpose before typing.",
            "Icon-grid quick action icons must pair with visible text labels, not rely on icon shape alone for meaning.",
            "Badge counts should be included in the overflow button's contentDescription (e.g. 'More options, 3 unread') for screen readers.",
            "Breadcrumb segments need at least 48dp touch targets even though their text may be visually small."
        ),
        performanceNotes = listOf(
            "Filtering an in-memory list on every keystroke is cheap for small option sets; derive the filtered list with `remember(query)` to avoid recomputation on unrelated recompositions.",
            "The breadcrumb menu's option map is static and defined once outside recomposition scope where possible.",
            "LazyRow in the carousel menu only composes visible items, so it scales to many sort options cheaply.",
            "Badge state changes should update only the badge count field via `copy()`, not replace the entire showcase state object unnecessarily."
        ),
        relatedComponentIds = listOf("menu-custom-styles"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(dropdownMenu, exposedDropdown, menuCustomStyles, menuCustomStyles2)
}
