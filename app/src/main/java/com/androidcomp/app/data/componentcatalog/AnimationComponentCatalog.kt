package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object AnimationComponentCatalog {

    private val animatedVisibility = ComponentSpec(
        id = "animation-visibility",
        category = ComponentCategory.ANIMATIONS,
        title = "AnimatedVisibility",
        overview = "Animates the appearance and disappearance of its content using combinable " +
            "enter/exit transitions such as fade, slide, and expand/shrink.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var visible by remember { mutableStateOf(true) }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Text("Now you see me")
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("visible", "Boolean", "required", "Whether the content should be shown; toggling triggers enter/exit."),
            ComponentProperty("enter", "EnterTransition", "fadeIn() + expandIn()", "Transition(s) played when `visible` becomes true."),
            ComponentProperty("exit", "ExitTransition", "fadeOut() + shrinkOut()", "Transition(s) played when `visible` becomes false."),
            ComponentProperty("label", "String", "\"AnimatedVisibility\"", "Debug label shown in the Layout Inspector / animation tooling.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Combine transitions with `+` (e.g. `fadeIn() + slideInVertically()`) rather than nesting multiple AnimatedVisibility composables.",
            "Keep enter/exit durations short (150-300ms) per the design system's subtle-motion guidance — avoid attention-grabbing, lengthy animations."
        ),
        commonMistakes = listOf(
            "Conditionally emitting the child composable with a plain `if (visible)` instead of using AnimatedVisibility, which skips animation entirely.",
            "Forgetting that content remains composed (and thus still runs side effects) during the exit animation, causing unexpected work after 'removal'."
        ),
        accessibilityNotes = listOf(
            "Respect the system's reduced-motion setting where possible; consider shorter or disabled transitions when `Settings.Global.ANIMATOR_DURATION_SCALE` is 0.",
            "Ensure content that appears/disappears doesn't strand focus — move accessibility focus explicitly if a AnimatedVisibility hides the currently focused element."
        ),
        performanceNotes = listOf(
            "The child composable stays in composition for the full exit duration; avoid triggering expensive recompositions or launched effects tied only to 'is visible' checks during that window.",
            "Avoid deeply nested AnimatedVisibility trees, since each one adds its own transition scope and measurement pass."
        ),
        relatedComponentIds = listOf("animation-float-state"),
        minApi = 21
    )

    private val animateFloatAsState = ComponentSpec(
        id = "animation-float-state",
        category = ComponentCategory.ANIMATIONS,
        title = "animateFloatAsState",
        overview = "A low-level animation API that produces a `State<Float>` which automatically " +
            "animates toward a target value whenever that target changes — commonly used for " +
            "custom scale, alpha, or offset transitions.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var pressed by remember { mutableStateOf(false) }
                val scale by animateFloatAsState(
                    targetValue = if (pressed) 0.9f else 1f,
                    animationSpec = tween(durationMillis = 150),
                    label = "buttonScale"
                )

                Box(
                    modifier = Modifier.graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                )
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("targetValue", "Float", "required", "The value the animation continuously chases toward."),
            ComponentProperty("animationSpec", "AnimationSpec<Float>", "spring()", "Controls easing/duration, e.g. `tween()` or `spring()`."),
            ComponentProperty("visibilityThreshold", "Float?", "null", "Minimum delta below which the animation is considered finished."),
            ComponentProperty("label", "String", "\"FloatAnimation\"", "Debug label shown in tooling."),
            ComponentProperty("finishedListener", "((Float) -> Unit)?", "null", "Called once the animation settles on the target value.")
        ),
        events = listOf("finishedListener — invoked with the final value once the animation completes."),
        bestPractices = listOf(
            "Drive purely visual properties (scale, alpha, offset via `graphicsLayer`) with this API instead of animating layout-affecting properties directly, to stay on the fast compositing path.",
            "Reuse a single `animateFloatAsState` per property rather than launching a new `Animatable` coroutine manually unless you need cancellation control it doesn't provide."
        ),
        commonMistakes = listOf(
            "Reading the animated value in a way that triggers full recomposition (e.g. passing it into a `Modifier.size()`) instead of `graphicsLayer`, causing layout thrash every frame.",
            "Changing `animationSpec` on every recomposition with a newly-allocated instance, which can restart or jank the animation."
        ),
        accessibilityNotes = listOf(
            "Purely visual animations like scale/alpha carry no semantic meaning — ensure the underlying state change (e.g. 'pressed') is also exposed via semantics for assistive tech.",
            "Keep animated motion subtle in line with the design system's guidance; avoid using this API for large, attention-grabbing movements."
        ),
        performanceNotes = listOf(
            "Animating via `Modifier.graphicsLayer` runs on the compositing thread and skips layout/measure, making it far cheaper than animating size or padding directly.",
            "`animateFloatAsState` restarts its underlying `Animatable` each time `targetValue` changes; rapid target flips (e.g. from fast repeated taps) can cause visible stutter — debounce the source state if needed."
        ),
        relatedComponentIds = listOf("animation-visibility"),
        minApi = 21
    )


    private val animationCustomStyles = ComponentSpec(
        id = "animation-custom-styles",
        category = ComponentCategory.ANIMATIONS,
        title = "Custom Animation Techniques",
        overview = "Five reusable animation techniques beyond a single property tween — staggered " +
            "list entrance, exaggerated spring bounce feedback, a particle confetti burst, an " +
            "in-place expanding card, and an infinite shimmer sweep — each demonstrating a " +
            "different Compose animation API in a small, self-contained composable.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                val alpha = remember(replayKey) { Animatable(0f) }
                val offsetX = remember(replayKey) { Animatable(40f) }

                LaunchedEffect(replayKey) {
                    delay(index * 80L)
                    launch { alpha.animateTo(1f, tween(300)) }
                    launch { offsetX.animateTo(0f, tween(300)) }
                }

                Row(Modifier.graphicsLayer { this.alpha = alpha.value; translationX = offsetX.value }) {
                    Text(label)
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("Animatable<Float>", "class", "n/a", "Used per-item for the stagger and per-particle for the confetti burst; supports animateTo/snapTo with individual specs."),
            ComponentProperty("spring(dampingRatio, stiffness)", "AnimationSpec<Float>", "n/a", "Produces the exaggerated overshoot for the bounce-feedback button via Spring.DampingRatioHighBouncy."),
            ComponentProperty("Modifier.animateContentSize", "Modifier", "n/a", "Smoothly interpolates a container's size when its content changes, used for the in-place expand card."),
            ComponentProperty("rememberInfiniteTransition", "InfiniteTransition", "n/a", "Drives the continuously-looping shimmer sweep with infiniteRepeatable + tween.")
        ),
        events = listOf(
            "onReplayStagger / onExpandCardExpandedChange — hoisted callbacks that let a parent replay or reset each demo's transient animation state."
        ),
        bestPractices = listOf(
            "Key per-item `Animatable`/`LaunchedEffect` state on a shared 'replay' token when you need a whole list to visibly restart together, rather than tracking each item's animation independently.",
            "Reach for `Modifier.animateContentSize()` for simple expand/collapse height changes — it's far less code than manually measuring and animating a Dp height."
        ),
        commonMistakes = listOf(
            "Launching all stagger delays with the same coroutine instead of one `launch` per item, which serializes the animation instead of interleaving it.",
            "Forgetting to reset `Animatable` values with `snapTo(0f)` before re-triggering a burst/replay animation, causing it to animate from the previous end state instead of the start."
        ),
        accessibilityNotes = listOf(
            "Purely decorative motion (confetti, shimmer) carries no semantic meaning and should never be the sole indicator of a state change — pair it with text or an icon that assistive tech can read.",
            "Respect the system's reduced-motion preference where practical; large bounce/particle effects should be toned down or skipped when the user has disabled animations."
        ),
        performanceNotes = listOf(
            "All five demos animate via `graphicsLayer` (translation, scale, alpha) or `animateContentSize`, avoiding full layout passes on every animation frame.",
            "An infinite shimmer transition keeps recomposing while visible — scope it to only the composable that needs it, and stop rendering it once its content has actually loaded."
        ),
        relatedComponentIds = listOf("animation-visibility", "animation-float-state"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(animatedVisibility, animateFloatAsState, animationCustomStyles)
}
