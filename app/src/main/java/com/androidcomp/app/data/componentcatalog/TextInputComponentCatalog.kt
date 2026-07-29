package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object TextInputComponentCatalog {

    private val outlined = ComponentSpec(
        id = "textinput-outlined",
        category = ComponentCategory.TEXT_INPUTS,
        title = "Outlined Text Field",
        overview = "A text input with a bordered outline and floating label, the recommended " +
            "Material 3 default for most forms since it reads clearly on any background.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var value by remember { mutableStateOf("") }

                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Email") },
                    singleLine = true
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.textfield.TextInputLayout
                    style="@style/Widget.Material3.TextInputLayout.OutlinedBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="Email">
                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content" />
                </com.google.android.material.textfield.TextInputLayout>
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onEmailChanged(newValue: String) {
                viewModel.updateEmail(newValue)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("value", "String", "required", "The current text value shown in the field."),
            ComponentProperty("onValueChange", "(String) -> Unit", "required", "Called with the new value on every edit."),
            ComponentProperty("label", "@Composable (() -> Unit)?", "null", "Floating label content shown above/inside the field."),
            ComponentProperty("isError", "Boolean", "false", "Whether to show the field in an error state."),
            ComponentProperty("singleLine", "Boolean", "false", "Collapses the field to a single scrollable line.")
        ),
        events = listOf("onValueChange — fired on every keystroke or programmatic edit."),
        bestPractices = listOf(
            "Hoist `value` into a ViewModel or remember{} at the call site — never store it inside the composable's body as a var.",
            "Pair with `isError` and a supporting text Text() below to explain validation failures."
        ),
        commonMistakes = listOf(
            "Forgetting `onValueChange` updates state, causing the field to appear frozen (no recomposition loop).",
            "Using OutlinedTextField for passwords without `visualTransformation = PasswordVisualTransformation()`."
        ),
        accessibilityNotes = listOf(
            "Always supply a `label` or `placeholder` — TalkBack announces the label to identify the field's purpose.",
            "Set `keyboardOptions` with the correct `KeyboardType` (Email, Phone, etc.) to aid both users and autofill."
        ),
        performanceNotes = listOf(
            "Avoid wrapping onValueChange in an expensive validation call on every keystroke; debounce heavy validation."
        ),
        relatedComponentIds = listOf("textinput-filled"),
        minApi = 21
    )

    private val filled = ComponentSpec(
        id = "textinput-filled",
        category = ComponentCategory.TEXT_INPUTS,
        title = "Filled Text Field",
        overview = "A text input with a solid filled background and an underline indicator, " +
            "used when a denser, more compact input style is preferred over the outlined variant.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var value by remember { mutableStateOf("") }

                TextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Search") },
                    singleLine = true
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.textfield.TextInputLayout
                    style="@style/Widget.Material3.TextInputLayout.FilledBox"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:hint="Search">
                    <com.google.android.material.textfield.TextInputEditText
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content" />
                </com.google.android.material.textfield.TextInputLayout>
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onSearchQueryChanged(newValue: String) {
                viewModel.updateSearchQuery(newValue)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("value", "String", "required", "The current text value shown in the field."),
            ComponentProperty("onValueChange", "(String) -> Unit", "required", "Called with the new value on every edit."),
            ComponentProperty("placeholder", "@Composable (() -> Unit)?", "null", "Content shown when the field is empty and unfocused."),
            ComponentProperty("trailingIcon", "@Composable (() -> Unit)?", "null", "Icon shown at the end of the field, e.g. a clear button.")
        ),
        events = listOf("onValueChange — fired on every keystroke or programmatic edit."),
        bestPractices = listOf(
            "Reserve Filled TextField for dense forms or search bars where the outlined variant would feel too heavy.",
            "Use a trailingIcon clear button when the field commonly holds transient input like search queries."
        ),
        commonMistakes = listOf(
            "Mixing Filled and Outlined text fields on the same form, which breaks visual consistency.",
            "Not clipping the filled background's corners when placing the field inside an already-rounded card."
        ),
        accessibilityNotes = listOf(
            "The filled background must maintain sufficient contrast against the surrounding surface color.",
            "Ensure the underline indicator color change on error is not the only error cue — pair with supporting text."
        ),
        performanceNotes = listOf(
            "Functionally identical performance profile to OutlinedTextField; difference is purely visual styling."
        ),
        relatedComponentIds = listOf("textinput-outlined"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(outlined, filled)
}
