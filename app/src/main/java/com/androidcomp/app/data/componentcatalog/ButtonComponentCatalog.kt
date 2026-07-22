package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object ButtonComponentCatalog {

    private val filled = ComponentSpec(
        id = "button-filled",
        category = ComponentCategory.BUTTONS,
        title = "Filled Button",
        overview = "A high-emphasis button with a solid fill color, used for the single most " +
            "important action on a screen (e.g. \"Submit\", \"Confirm\").",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Button(onClick = { /* action */ }) {
                    Text("Filled Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Filled Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onSubmitClicked() {
                viewModel.submit()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state."),
            ComponentProperty("colors", "ButtonColors", "ButtonDefaults.buttonColors()", "Container/content colors."),
            ComponentProperty("shape", "Shape", "ButtonDefaults.shape", "The button's shape.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Use at most one Filled Button per screen for the primary action.",
            "Keep labels short and action-oriented (\"Save\", not \"Save Changes Now\")."
        ),
        commonMistakes = listOf(
            "Using multiple Filled Buttons on the same screen, diluting visual hierarchy.",
            "Disabling the button without explaining why to the user."
        ),
        accessibilityNotes = listOf(
            "Minimum touch target is 48x48dp — Button already meets this by default.",
            "Ensure the label text has sufficient contrast against the container color."
        ),
        performanceNotes = listOf(
            "Avoid creating new lambda instances for onClick on every recomposition; hoist state."
        ),
        relatedComponentIds = listOf("button-filled-tonal", "button-outlined", "button-elevated"),
        minApi = 21
    )

    private val filledTonal = ComponentSpec(
        id = "button-filled-tonal",
        category = ComponentCategory.BUTTONS,
        title = "Filled Tonal Button",
        overview = "A medium-emphasis button using a tonal color, useful for actions that need " +
            "more emphasis than an Outlined Button but less than a Filled Button.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                FilledTonalButton(onClick = { /* action */ }) {
                    Text("Filled Tonal Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.TonalButton"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Filled Tonal Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onSecondaryActionClicked() {
                viewModel.performSecondaryAction()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state."),
            ComponentProperty("colors", "ButtonColors", "ButtonDefaults.filledTonalButtonColors()", "Container/content colors.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Use for secondary actions that still deserve emphasis, e.g. \"Add to cart\" next to a primary \"Buy now\"."
        ),
        commonMistakes = listOf(
            "Using Filled Tonal and Filled Buttons together without a clear hierarchy reason."
        ),
        accessibilityNotes = listOf(
            "Tonal color contrast is lower than Filled — verify against WCAG AA for the label text."
        ),
        performanceNotes = listOf(
            "Same recomposition considerations as Filled Button."
        ),
        relatedComponentIds = listOf("button-filled", "button-outlined"),
        minApi = 21
    )

    private val outlined = ComponentSpec(
        id = "button-outlined",
        category = ComponentCategory.BUTTONS,
        title = "Outlined Button",
        overview = "A medium-emphasis button with a stroked border and transparent background, " +
            "typically used for alternative or secondary actions.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                OutlinedButton(onClick = { /* action */ }) {
                    Text("Outlined Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.OutlinedButton"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Outlined Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onCancelClicked() {
                viewModel.cancel()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("border", "BorderStroke?", "ButtonDefaults.outlinedButtonBorder", "The stroke drawn around the button.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Pair with a Filled Button for a clear primary/secondary action pair (e.g. \"Cancel\" / \"Save\")."
        ),
        commonMistakes = listOf(
            "Using an Outlined Button as the only action on a screen when a Filled Button would communicate priority better."
        ),
        accessibilityNotes = listOf(
            "The border alone is not sufficient contrast signal — ensure label text meets contrast requirements independently."
        ),
        performanceNotes = listOf(
            "No additional overhead versus Filled Button; border is drawn via Modifier, not a separate layer."
        ),
        relatedComponentIds = listOf("button-filled", "button-text"),
        minApi = 21
    )

    private val text = ComponentSpec(
        id = "button-text",
        category = ComponentCategory.BUTTONS,
        title = "Text Button",
        overview = "A low-emphasis button with no container, typically used for the least " +
            "important actions, such as in dialogs or cards.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                TextButton(onClick = { /* action */ }) {
                    Text("Text Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.TextButton"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Text Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onLearnMoreClicked() {
                viewModel.openLearnMore()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Use inside dialogs (e.g. \"Cancel\") or alongside cards where a container would add visual noise."
        ),
        commonMistakes = listOf(
            "Using Text Buttons for primary actions — low emphasis makes them easy to miss."
        ),
        accessibilityNotes = listOf(
            "Touch target must still be 48x48dp minimum even though visually the button looks smaller."
        ),
        performanceNotes = listOf(
            "Lightest-weight button variant — no container draw, minimal overdraw."
        ),
        relatedComponentIds = listOf("button-outlined", "button-filled"),
        minApi = 21
    )

    private val elevated = ComponentSpec(
        id = "button-elevated",
        category = ComponentCategory.BUTTONS,
        title = "Elevated Button",
        overview = "A button with a shadow to convey elevation, typically used when a button " +
            "needs to stand out against a busy or colored background.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                ElevatedButton(onClick = { /* action */ }) {
                    Text("Elevated Button")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.ElevatedButton"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="Elevated Button" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onPromotedActionClicked() {
                viewModel.performPromotedAction()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("elevation", "ButtonElevation?", "ButtonDefaults.buttonElevation()", "Shadow elevation per interaction state.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Reserve for buttons placed on top of colored or image backgrounds where a flat button would lack contrast."
        ),
        commonMistakes = listOf(
            "Using Elevated Button on a plain white background — the shadow reads as unnecessary visual noise."
        ),
        accessibilityNotes = listOf(
            "Shadow is a purely visual cue; do not rely on it alone to convey interactivity — labels must be descriptive."
        ),
        performanceNotes = listOf(
            "Shadow rendering adds a small compositing cost — avoid overusing on long scrolling lists."
        ),
        relatedComponentIds = listOf("button-filled", "button-filled-tonal"),
        minApi = 21
    )

    private val icon = ComponentSpec(
        id = "button-icon",
        category = ComponentCategory.BUTTONS,
        title = "Icon Button",
        overview = "A compact, icon-only button for common, space-constrained actions such as " +
            "toolbar or app bar actions.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                IconButton(onClick = { /* action */ }) {
                    Icon(Icons.Filled.Favorite, contentDescription = "Favorite")
                }
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.button.MaterialButton
                    style="@style/Widget.Material3.Button.IconButton"
                    android:layout_width="48dp"
                    android:layout_height="48dp"
                    app:icon="@drawable/ic_favorite" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onFavoriteToggled() {
                viewModel.toggleFavorite()
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onClick", "() -> Unit", "required", "Called when the button is clicked."),
            ComponentProperty("enabled", "Boolean", "true", "Controls the enabled state.")
        ),
        events = listOf("onClick — fired on tap or accessibility activation."),
        bestPractices = listOf(
            "Always pair with a `contentDescription` on the Icon — Icon Button has no visible label."
        ),
        commonMistakes = listOf(
            "Omitting contentDescription, making the button unusable for screen reader users.",
            "Using Icon Button for actions whose meaning isn't obvious from the icon alone."
        ),
        accessibilityNotes = listOf(
            "contentDescription is mandatory, not optional, for this component.",
            "Default size meets the 48x48dp minimum touch target."
        ),
        performanceNotes = listOf(
            "Negligible overhead; icon vector rendering is the only extra cost versus TextButton."
        ),
        relatedComponentIds = listOf("button-text"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(filled, filledTonal, outlined, text, elevated, icon)
}
