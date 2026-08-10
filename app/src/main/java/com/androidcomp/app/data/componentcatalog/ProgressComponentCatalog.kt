package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object ProgressComponentCatalog {

    private val circular = ComponentSpec(
        id = "progress-circular",
        category = ComponentCategory.PROGRESS,
        title = "Circular Progress Indicator",
        overview = "A circular spinner used to communicate an ongoing, indeterminate operation, " +
            "or an exact determinate percentage when a `progress` value is supplied.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                // Indeterminate
                CircularProgressIndicator()

                // Determinate
                CircularProgressIndicator(progress = { 0.65f })
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <ProgressBar
                    style="?android:attr/progressBarStyleLarge"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:indeterminate="true" />
            """.trimIndent()
        ),
        viewModelUsage = """
            val downloadProgress: StateFlow<Float> = repository.downloadProgress
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("progress", "() -> Float", "n/a (indeterminate overload)", "Fraction from 0f to 1f; omit the parameter to get the indeterminate spinner."),
            ComponentProperty("color", "Color", "ProgressIndicatorDefaults.circularColor", "Color of the active arc."),
            ComponentProperty("strokeWidth", "Dp", "ProgressIndicatorDefaults.CircularStrokeWidth", "Thickness of the drawn arc."),
            ComponentProperty("trackColor", "Color", "ProgressIndicatorDefaults.circularTrackColor", "Color of the background track."),
            ComponentProperty("strokeCap", "StrokeCap", "ProgressIndicatorDefaults.CircularIndeterminateStrokeCap", "Shape of the arc's end caps.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Use the indeterminate overload when the duration of the operation is unknown; switch to determinate as soon as real progress is available.",
            "Pair with a short text label (\"Loading orders…\") for operations longer than a couple of seconds so users understand what's happening."
        ),
        commonMistakes = listOf(
            "Passing a progress lambda that reads a value updated on a background thread without hoisting it into Compose state, so the UI never recomposes.",
            "Using the determinate variant with a progress value that never reaches 1f, leaving users unsure if the task finished."
        ),
        accessibilityNotes = listOf(
            "Wrap in a `Modifier.semantics { progressBarRangeInfo = ProgressBarRangeInfo(...) }` or rely on the default so TalkBack announces percentage for determinate progress.",
            "For indeterminate spinners, ensure surrounding content announces what is loading, since the indicator itself conveys no textual meaning."
        ),
        performanceNotes = listOf(
            "The indeterminate animation runs continuously via `rememberInfiniteTransition`; avoid keeping many off-screen instances animating in large lists.",
            "The determinate overload accepts a lambda (`progress: () -> Float`) specifically to avoid recomposition of the whole indicator on every progress tick — only the draw phase reads it."
        ),
        relatedComponentIds = listOf("progress-linear"),
        minApi = 21
    )

    private val linear = ComponentSpec(
        id = "progress-linear",
        category = ComponentCategory.PROGRESS,
        title = "Linear Progress Indicator",
        overview = "A horizontal bar used to communicate progress for tasks like file uploads, " +
            "multi-step forms, or page loads, in both determinate and indeterminate forms.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                // Indeterminate
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

                // Determinate
                LinearProgressIndicator(
                    progress = { uploadProgress },
                    modifier = Modifier.fillMaxWidth()
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <ProgressBar
                    style="?android:attr/progressBarStyleHorizontal"
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:max="100"
                    android:progress="65" />
            """.trimIndent()
        ),
        viewModelUsage = """
            val uploadProgress: StateFlow<Float> = uploadRepository.progress
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("progress", "() -> Float", "n/a (indeterminate overload)", "Fraction from 0f to 1f; omit for the indeterminate sweeping bar."),
            ComponentProperty("color", "Color", "ProgressIndicatorDefaults.linearColor", "Color of the filled portion of the bar."),
            ComponentProperty("trackColor", "Color", "ProgressIndicatorDefaults.linearTrackColor", "Color of the unfilled track."),
            ComponentProperty("strokeCap", "StrokeCap", "ProgressIndicatorDefaults.LinearStrokeCap", "End cap shape for the bar and track.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Anchor determinate bars to real, monotonically increasing progress values so users trust the estimate.",
            "Use `fillMaxWidth()` so the bar's length reads as a proportional progress cue rather than an arbitrary shape."
        ),
        commonMistakes = listOf(
            "Letting progress jump backwards (e.g. due to a retried chunked upload) without smoothing, which reads as broken to users.",
            "Placing the indeterminate variant permanently on screen for tasks that actually do have measurable progress."
        ),
        accessibilityNotes = listOf(
            "Determinate progress is exposed to TalkBack as a percentage automatically via `ProgressBarRangeInfo` semantics.",
            "Don't rely on color alone (e.g. red vs. green track) to convey success/failure state once progress completes; pair with text or icon."
        ),
        performanceNotes = listOf(
            "Like the circular variant, the determinate overload takes a `() -> Float` lambda so only the draw pass recomposes on progress changes, not the whole composable tree.",
            "Avoid recreating the indicator inside a frequently-recomposing parent; hoist the progress state to the lowest stable scope."
        ),
        relatedComponentIds = listOf("progress-circular"),
        minApi = 21
    )

    private val progressCustomStyles = ComponentSpec(
        id = "progress-custom-styles",
        category = ComponentCategory.PROGRESS,
        title = "Custom Progress Styles",
        overview = "Five fully custom-designed progress indicators beyond the standard Circular/Linear " +
            "indicators — dotted step progress, a Canvas-drawn percentage ring, skeleton shimmer " +
            "loading, a segmented multi-step bar, and an animated liquid wave fill. Tap the controls " +
            "below each one to see it animate, and copy its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun CircularPercentageRing(percent: Int, onIncrease: () -> Unit) {
                    val animatedPercent by animateIntAsState(percent)
                    val sweep by animateFloatAsState(percent / 100f)

                    Box(contentAlignment = Alignment.Center) {
                        Canvas(Modifier.size(120.dp)) {
                            drawArc(trackColor, -90f, 360f, useCenter = false, style = Stroke(12.dp.toPx()))
                            drawArc(activeColor, -90f, 360f * sweep, useCenter = false, style = Stroke(12.dp.toPx()))
                        }
                        Text("${'$'}animatedPercent%")
                    }
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("step / totalSteps", "Int", "required", "Current completed count and total dot/segment count for the step-based designs."),
            ComponentProperty("percent", "Int (0-100)", "required", "Drives the animated sweep angle, center label, and wave fill height."),
            ComponentProperty("shimmerX animation", "State<Float> via rememberInfiniteTransition", "n/a", "Drifts the shimmer gradient across the skeleton placeholders on an infinite loop."),
            ComponentProperty("phase animation", "State<Float> via rememberInfiniteTransition", "n/a", "Advances the sine-wave phase for the liquid progress's horizontal drift.")
        ),
        events = listOf("onNext/onBack, onIncrease, onAdvance/onReset — fired by each design's control buttons to change the underlying progress value."),
        bestPractices = listOf(
            "Drive Canvas-based indicators (ring, wave) from animateFloatAsState/animateIntAsState rather than the raw percent so value jumps still read as smooth motion.",
            "Keep skeleton shimmer shapes matched to the real content's approximate layout so the loading state doesn't visually 'jump' once data arrives."
        ),
        commonMistakes = listOf(
            "Redrawing the entire Canvas path every frame with expensive allocations instead of reusing a Path object across recompositions.",
            "Letting a step indicator's dot/segment count diverge from the actual number of steps in the flow it represents."
        ),
        accessibilityNotes = listOf(
            "Canvas-drawn progress conveys no semantics automatically — add `Modifier.semantics { progressBarRangeInfo = ProgressBarRangeInfo(percent / 100f, 0f..1f) }` for TalkBack.",
            "Skeleton shimmer placeholders should be marked so screen readers announce 'Loading' rather than reading empty/meaningless shapes."
        ),
        performanceNotes = listOf(
            "rememberInfiniteTransition (shimmer, wave drift) keeps animating while composed — dispose or pause offscreen instances in a real list.",
            "Prefer drawArc/drawPath directly in a Canvas over stacking many small Box gradients, which is cheaper for continuously animating visuals."
        ),
        relatedComponentIds = listOf("progress-circular", "progress-linear"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(circular, linear, progressCustomStyles)
}
