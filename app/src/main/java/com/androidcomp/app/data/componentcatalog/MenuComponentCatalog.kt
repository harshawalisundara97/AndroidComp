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

    val all: List<ComponentSpec> = listOf(dropdownMenu, exposedDropdown)
}
