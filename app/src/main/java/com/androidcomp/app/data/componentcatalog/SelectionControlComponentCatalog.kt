package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object SelectionControlComponentCatalog {

    private val checkbox = ComponentSpec(
        id = "selection-checkbox",
        category = ComponentCategory.SELECTION_CONTROLS,
        title = "Checkbox",
        overview = "A binary or tri-state control used to select one or more independent " +
            "options from a set, typically paired with a text label.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var checked by remember { mutableStateOf(false) }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = checked, onCheckedChange = { checked = it })
                    Text("Subscribe to newsletter")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <CheckBox
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Subscribe to newsletter" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onNewsletterToggled(checked: Boolean) {
                viewModel.setNewsletterSubscribed(checked)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("checked", "Boolean", "required", "Current checked state of the box."),
            ComponentProperty("onCheckedChange", "((Boolean) -> Unit)?", "required", "Called when the user toggles the box; null makes it read-only."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state."),
            ComponentProperty("colors", "CheckboxColors", "CheckboxDefaults.colors()", "Colors for checked/unchecked/disabled states.")
        ),
        events = listOf("onCheckedChange — fired when the checkbox is tapped or activated via accessibility services."),
        bestPractices = listOf(
            "Make the entire row (checkbox + label) tappable by applying `Modifier.toggleable()` or `selectable()` on the Row, not just the Checkbox.",
            "Use Checkbox for independent, multi-select options; use RadioButton instead when only one option in a group can be selected."
        ),
        commonMistakes = listOf(
            "Only the small Checkbox itself being tappable, failing the 48x48dp minimum touch target in practice.",
            "Using a Checkbox where a Switch would be more appropriate, e.g. for an immediate on/off setting rather than a form selection."
        ),
        accessibilityNotes = listOf(
            "When wrapping in a custom Row, add `Modifier.semantics(mergeDescendants = true)` so TalkBack announces label + state together.",
            "Announce the tri-state (ToggleableState.Indeterminate) case distinctly if using TriStateCheckbox for parent/child selections."
        ),
        performanceNotes = listOf(
            "Checkbox state should be hoisted; avoid recomposing the entire list row tree on every toggle by scoping state narrowly."
        ),
        relatedComponentIds = listOf("selection-radio-button", "selection-switch"),
        minApi = 21
    )

    private val radioButton = ComponentSpec(
        id = "selection-radio-button",
        category = ComponentCategory.SELECTION_CONTROLS,
        title = "Radio Button",
        overview = "A control for selecting exactly one option from a mutually exclusive set, " +
            "always used in a group of two or more RadioButtons sharing selection state.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val options = listOf("Small", "Medium", "Large")
                var selected by remember { mutableStateOf(options.first()) }

                Column(Modifier.selectableGroup()) {
                    options.forEach { option ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = (option == selected),
                                    onClick = { selected = option },
                                    role = Role.RadioButton
                                ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(selected = (option == selected), onClick = null)
                            Text(option)
                        }
                    }
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <RadioGroup
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical">
                    <RadioButton
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Small" />
                    <RadioButton
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:text="Medium" />
                </RadioGroup>
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onSizeSelected(size: String) {
                viewModel.setSelectedSize(size)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("selected", "Boolean", "required", "Whether this specific radio button is the selected option."),
            ComponentProperty("onClick", "(() -> Unit)?", "required", "Called when this radio button is clicked directly; pass null when the parent Row handles selection."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state."),
            ComponentProperty("colors", "RadioButtonColors", "RadioButtonDefaults.colors()", "Colors for selected/unselected/disabled states.")
        ),
        events = listOf("onClick — fired when the radio button (or its owning row) is activated."),
        bestPractices = listOf(
            "Apply `Modifier.selectableGroup()` to the container so accessibility services announce the set as a single radio group.",
            "Put `onClick` and the `selectable` Role.RadioButton semantics on the parent Row, passing `onClick = null` to the RadioButton itself to avoid double touch targets."
        ),
        commonMistakes = listOf(
            "Managing each RadioButton's selection independently instead of a single hoisted 'selectedOption' state, allowing multiple selections.",
            "Omitting selectableGroup(), causing TalkBack to announce each option without conveying they are mutually exclusive."
        ),
        accessibilityNotes = listOf(
            "role = Role.RadioButton on the selectable Row ensures TalkBack announces 'radio button' rather than 'button'.",
            "Group size should be announced meaningfully; avoid radio groups with only one option — use a Checkbox instead."
        ),
        performanceNotes = listOf(
            "Recomposition on selection change is scoped to the row content when selected state is compared with `==` on a simple value type."
        ),
        relatedComponentIds = listOf("selection-checkbox", "selection-switch"),
        minApi = 21
    )

    private val switch = ComponentSpec(
        id = "selection-switch",
        category = ComponentCategory.SELECTION_CONTROLS,
        title = "Switch",
        overview = "A control that toggles a single setting on or off immediately, without " +
            "requiring a separate confirmation action — commonly used in Settings screens.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var enabled by remember { mutableStateOf(true) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Enable notifications")
                    Switch(checked = enabled, onCheckedChange = { enabled = it })
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.materialswitch.MaterialSwitch
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Enable notifications" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onNotificationsToggled(enabled: Boolean) {
                viewModel.setNotificationsEnabled(enabled)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("checked", "Boolean", "required", "Current on/off state of the switch."),
            ComponentProperty("onCheckedChange", "((Boolean) -> Unit)?", "required", "Called when the user toggles the switch; null makes it read-only."),
            ComponentProperty("thumbContent", "@Composable (() -> Unit)?", "null", "Optional icon rendered inside the thumb, e.g. a check mark when on."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state.")
        ),
        events = listOf("onCheckedChange — fired immediately when the user drags or taps the switch."),
        bestPractices = listOf(
            "Use Switch only for settings that take effect immediately; use Checkbox in forms that require an explicit Save/Submit action.",
            "Keep the associated label static — don't change the label text based on the switch's own state (e.g. avoid 'On'/'Off' as the label itself)."
        ),
        commonMistakes = listOf(
            "Using Switch inside a form that also has a Submit button, creating ambiguity about whether the change already applied.",
            "Not making the entire settings row tappable, forcing users to hit the small Switch thumb precisely."
        ),
        accessibilityNotes = listOf(
            "TalkBack announces Switch as 'on'/'off' automatically — don't duplicate that in the visible label text.",
            "Ensure the full settings row (label + switch) is one 48dp+ tall tappable target via Modifier.toggleable()."
        ),
        performanceNotes = listOf(
            "Switch's thumb position is animated internally; avoid wrapping it in additional custom animation logic."
        ),
        relatedComponentIds = listOf("selection-checkbox", "selection-radio-button"),
        minApi = 21
    )

    private val toggleStyles = ComponentSpec(
        id = "selection-toggle-styles",
        category = ComponentCategory.SELECTION_CONTROLS,
        title = "Custom Toggle Styles",
        overview = "Five fully custom-designed toggle switches beyond the standard Material 3 " +
            "Switch — each with its own track shape, color animation, and motion feel (spring " +
            "bounce, icon morph, neon glow, neumorphic depth, or rubber-like squish). Tap each " +
            "one below to see it animate, and copy its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun CustomToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
                    val trackColor by animateColorAsState(if (checked) activeColor else inactiveColor)
                    val thumbOffset by animateDpAsState(if (checked) travelDistance else 0.dp)

                    Box(
                        Modifier
                            .size(56.dp, 32.dp)
                            .background(trackColor, RoundedCornerShape(50))
                            .clickable { onCheckedChange(!checked) }
                    ) {
                        Box(Modifier.offset(x = thumbOffset).size(24.dp).background(Color.White, CircleShape))
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("checked", "Boolean", "required", "Current on/off state driving color, position, and icon animations."),
            ComponentProperty("onCheckedChange", "(Boolean) -> Unit", "required", "Called when the toggle is tapped."),
            ComponentProperty("trackColor animation", "State<Color> via animateColorAsState", "n/a", "Interpolates the track's background between its inactive and active colors."),
            ComponentProperty("thumb motion spec", "AnimationSpec<Dp>", "varies per style", "spring() for bounce/elastic feels, tween() for mechanical or soft glides.")
        ),
        events = listOf("onCheckedChange — fired on tap, same contract as Material3 Switch so it's a drop-in replacement."),
        bestPractices = listOf(
            "Keep a custom toggle's animation duration under ~500ms so it still reads as immediate feedback, not a delay.",
            "Reuse one AnimationSpec (spring vs tween) consistently across a design system rather than mixing feels arbitrarily per screen."
        ),
        commonMistakes = listOf(
            "Building a fully custom toggle without also implementing accessibility semantics (Modifier.toggleable with Role.Switch), leaving it invisible to TalkBack.",
            "Hardcoding pixel offsets instead of computing thumb travel from actual track/thumb sizes, which breaks if either is resized later."
        ),
        accessibilityNotes = listOf(
            "Custom-drawn toggles must apply `Modifier.toggleable(value = checked, onValueChange = ..., role = Role.Switch)` so screen readers announce them correctly — plain `clickable` alone is not enough.",
            "Don't rely on color change alone to convey state (e.g. gray vs cyan) — the position change and/or icon swap should also be perceivable for color-blind users."
        ),
        performanceNotes = listOf(
            "Prefer `Modifier.graphicsLayer` for scale/rotation animations (as used for the spin and squish effects here) — it animates on the compositor without triggering layout/measure passes.",
            "animateColorAsState/animateDpAsState are cheap for a handful of toggles; for a long list of many toggles, consider driving animations from a single shared transition instead of one per row."
        ),
        relatedComponentIds = listOf("selection-switch"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(checkbox, radioButton, switch, toggleStyles)
}
