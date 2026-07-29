package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object SliderComponentCatalog {

    private val basicSlider = ComponentSpec(
        id = "slider-basic",
        category = ComponentCategory.SLIDERS,
        title = "Slider",
        overview = "Lets users select a single value from a continuous or stepped range by " +
            "dragging a thumb along a track, e.g. adjusting volume or brightness.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var volume by remember { mutableFloatStateOf(0.5f) }

                Slider(
                    value = volume,
                    onValueChange = { volume = it },
                    valueRange = 0f..1f,
                    steps = 0
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.slider.Slider
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:valueFrom="0.0"
                    android:valueTo="1.0"
                    android:value="0.5" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onVolumeChanged(value: Float) {
                viewModel.setVolume(value)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("value", "Float", "required", "The current thumb position; must be hoisted by the caller."),
            ComponentProperty("onValueChange", "(Float) -> Unit", "required", "Called continuously as the user drags the thumb."),
            ComponentProperty("valueRange", "ClosedFloatingPointRange<Float>", "0f..1f", "The minimum and maximum selectable values."),
            ComponentProperty("steps", "Int", "0", "Number of discrete divisions between the endpoints; 0 means continuous."),
            ComponentProperty("onValueChangeFinished", "(() -> Unit)?", "null", "Called once when the user releases the thumb — ideal for committing expensive side effects.")
        ),
        events = listOf(
            "onValueChange — fired repeatedly during drag with the live value.",
            "onValueChangeFinished — fired once when the drag gesture ends."
        ),
        bestPractices = listOf(
            "Use `onValueChangeFinished` (not `onValueChange`) to trigger expensive work like network calls or persistence, since `onValueChange` fires on every pixel of drag.",
            "Show the current numeric value near the slider (or in a tooltip) when precision matters, since the thumb position alone is hard to read exactly."
        ),
        commonMistakes = listOf(
            "Forgetting to hoist `value` into state and instead using a `var` that isn't backed by `remember`, causing the thumb to snap back after every drag.",
            "Running heavy work directly inside `onValueChange`, causing jank while dragging."
        ),
        accessibilityNotes = listOf(
            "Slider automatically exposes `ProgressBarRangeInfo` semantics so TalkBack announces the current value and range.",
            "Ensure the touch target for the thumb remains at least 48x48dp; avoid shrinking the slider's height via custom modifiers."
        ),
        performanceNotes = listOf(
            "`onValueChange` can fire many times per second during a drag — keep its body allocation-free and non-blocking.",
            "Prefer `mutableFloatStateOf` over `mutableStateOf<Float>` for the hoisted value to avoid boxing on every update."
        ),
        relatedComponentIds = listOf("slider-range"),
        minApi = 21
    )

    private val rangeSlider = ComponentSpec(
        id = "slider-range",
        category = ComponentCategory.SLIDERS,
        title = "Range Slider",
        overview = "A two-thumb slider that lets users select a range between a low and high " +
            "value, e.g. filtering a price range or a date span.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var priceRange by remember { mutableStateOf(20f..80f) }

                RangeSlider(
                    value = priceRange,
                    onValueChange = { priceRange = it },
                    valueRange = 0f..100f,
                    steps = 0
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <com.google.android.material.slider.RangeSlider
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:valueFrom="0.0"
                    android:valueTo="100.0" />
            """.trimIndent()
        ),
        viewModelUsage = """
            fun onPriceRangeChanged(range: ClosedFloatingPointRange<Float>) {
                viewModel.setPriceFilter(range.start, range.endInclusive)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("value", "ClosedFloatingPointRange<Float>", "required", "The current low..high selection; must be hoisted by the caller."),
            ComponentProperty("onValueChange", "(ClosedFloatingPointRange<Float>) -> Unit", "required", "Called continuously as either thumb is dragged."),
            ComponentProperty("valueRange", "ClosedFloatingPointRange<Float>", "0f..1f", "The overall minimum and maximum selectable bounds."),
            ComponentProperty("steps", "Int", "0", "Number of discrete divisions between the endpoints; 0 means continuous."),
            ComponentProperty("onValueChangeFinished", "(() -> Unit)?", "null", "Called once when the user releases either thumb.")
        ),
        events = listOf(
            "onValueChange — fired repeatedly during drag of either thumb with the live range.",
            "onValueChangeFinished — fired once when a drag gesture ends."
        ),
        bestPractices = listOf(
            "Display both endpoint values as text near the slider (e.g. \"$20 - $80\") since two thumbs are harder to read at a glance than one.",
            "Commit filter queries in `onValueChangeFinished`, not `onValueChange`, to avoid re-querying on every drag frame."
        ),
        commonMistakes = listOf(
            "Allowing the two thumbs to cross without clamping, which produces a confusing or invalid range depending on how the caller handles it.",
            "Using `RangeSlider` for a single value instead of the simpler `Slider`, adding unnecessary complexity."
        ),
        accessibilityNotes = listOf(
            "Each thumb is a separate focusable element for TalkBack, announcing its own value and role within the range.",
            "Keep enough visual and touch spacing between the two thumbs at their closest allowed positions so users can reliably grab the correct one."
        ),
        performanceNotes = listOf(
            "As with `Slider`, avoid heavy work in `onValueChange`; use `onValueChangeFinished` for expensive operations like re-filtering a large list.",
            "Prefer a stable `ClosedFloatingPointRange<Float>` state holder over recreating range objects unnecessarily on unrelated recompositions."
        ),
        relatedComponentIds = listOf("slider-basic"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(basicSlider, rangeSlider)
}
