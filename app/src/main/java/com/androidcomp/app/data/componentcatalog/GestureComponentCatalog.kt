package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object GestureComponentCatalog {

    private val tapGestures = ComponentSpec(
        id = "gesture-tap",
        category = ComponentCategory.GESTURES,
        title = "detectTapGestures (pointerInput)",
        overview = "Attaches low-level tap, double-tap, long-press, and press gesture detection " +
            "to any composable via `Modifier.pointerInput`, for cases where `clickable`'s " +
            "single-tap semantics aren't enough.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Box(
                    modifier = Modifier.pointerInput(Unit) {
                        detectTapGestures(
                            onTap = { /* single tap */ },
                            onDoubleTap = { /* double tap */ },
                            onLongPress = { /* long press */ }
                        )
                    }
                )
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            fun onImageDoubleTapped(itemId: String) {
                viewModel.toggleLike(itemId)
            }
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("onTap", "((Offset) -> Unit)?", "null", "Called once a single tap is confirmed (after waiting to rule out a double-tap)."),
            ComponentProperty("onDoubleTap", "((Offset) -> Unit)?", "null", "Called when two taps occur within the double-tap timeout."),
            ComponentProperty("onLongPress", "((Offset) -> Unit)?", "null", "Called after the pointer is held down past the long-press timeout."),
            ComponentProperty("onPress", "(suspend PressGestureScope.(Offset) -> Unit)", "{}", "Called immediately on press-down, useful for press/ripple feedback before the gesture resolves.")
        ),
        events = listOf(
            "onTap / onDoubleTap / onLongPress / onPress — each receives the local Offset of the pointer event."
        ),
        bestPractices = listOf(
            "Prefer `Modifier.clickable`/`combinedClickable` for standard single/long-press/double-click cases — they come with built-in accessibility and ripple support; reach for `detectTapGestures` only when you need custom multi-gesture handling.",
            "Key the `pointerInput` block with a stable key (e.g. `Unit` or a specific id) so the gesture detection coroutine isn't needlessly restarted on every recomposition."
        ),
        commonMistakes = listOf(
            "Adding both `Modifier.clickable` and a `pointerInput` tap detector on the same node, causing duplicate or conflicting gesture handling.",
            "Passing a changing lambda or object as the `pointerInput` key, which cancels and restarts the gesture-detection coroutine on every recomposition and can drop in-flight gestures."
        ),
        accessibilityNotes = listOf(
            "Raw `pointerInput` gesture detectors are invisible to TalkBack and other assistive tech — there is no automatic click/long-click semantics, so pair with `Modifier.semantics { onClick { ... } }` or use `combinedClickable` instead when accessibility matters.",
            "Custom gesture-only interactions (e.g. double-tap-to-like) should always have an accessible alternative action, since double-tap timing is hard for some users to perform."
        ),
        performanceNotes = listOf(
            "`onTap` intentionally waits out the double-tap timeout before firing, adding latency versus `clickable`; only use it when double-tap detection is actually needed.",
            "Gesture detection runs inside a suspend function per pointer event stream — keep the lambdas lightweight and dispatch heavy work (e.g. ViewModel calls) rather than doing it inline."
        ),
        relatedComponentIds = listOf("gesture-drag"),
        minApi = 21
    )

    private val dragGestures = ComponentSpec(
        id = "gesture-drag",
        category = ComponentCategory.GESTURES,
        title = "detectDragGestures (pointerInput)",
        overview = "Attaches low-level free-form drag detection to a composable via " +
            "`Modifier.pointerInput`, giving per-frame drag deltas for custom drag-and-drop, " +
            "swipe-to-dismiss, or canvas drawing interactions.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                var offsetX by remember { mutableFloatStateOf(0f) }
                var offsetY by remember { mutableFloatStateOf(0f) }

                Box(
                    modifier = Modifier
                        .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                        .pointerInput(Unit) {
                            detectDragGestures { change, dragAmount ->
                                change.consume()
                                offsetX += dragAmount.x
                                offsetY += dragAmount.y
                            }
                        }
                )
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("onDragStart", "(Offset) -> Unit", "{}", "Called once when the drag gesture begins, with the initial local position."),
            ComponentProperty("onDragEnd", "() -> Unit", "{}", "Called when the pointer is released or the gesture is cancelled cleanly."),
            ComponentProperty("onDragCancel", "() -> Unit", "{}", "Called if the gesture is cancelled abnormally (e.g. consumed by a parent)."),
            ComponentProperty("onDrag", "(PointerInputChange, Offset) -> Unit", "required", "Called per pointer move with the raw `PointerInputChange` and the drag delta since the last event.")
        ),
        events = listOf(
            "onDragStart / onDrag / onDragEnd / onDragCancel — lifecycle callbacks for a single continuous drag gesture."
        ),
        bestPractices = listOf(
            "Prefer `Modifier.draggable` (single-axis) when only horizontal or vertical movement is needed — it integrates with `rememberDraggableState` and handles fling/decay more simply than raw `detectDragGestures`.",
            "Always call `change.consume()` inside `onDrag` when your composable owns the gesture, to prevent ancestor scrollables from also reacting to the same pointer events."
        ),
        commonMistakes = listOf(
            "Forgetting `change.consume()`, causing a parent `LazyColumn` or `ScrollableColumn` to scroll simultaneously with the custom drag.",
            "Mutating layout-affecting state (like padding or size) directly from `onDrag` instead of using `Modifier.offset { }` with a lambda, causing a full measure/layout pass on every drag frame."
        ),
        accessibilityNotes = listOf(
            "Free-form drag interactions have no built-in accessibility mapping — provide an alternative, discrete action (e.g. buttons or a slider) for users who cannot perform a drag gesture.",
            "If the drag represents a meaningful state change (e.g. reordering or dismissing an item), announce the result via `Modifier.semantics` or a custom accessibility action once the drag completes."
        ),
        performanceNotes = listOf(
            "Using `Modifier.offset { IntOffset(...) }` (the lambda-based offset) instead of a plain `Modifier.offset(x, y)` avoids triggering recomposition of the composable on every drag frame — only layout placement updates.",
            "For drag targets in a `LazyColumn`, avoid recreating the `pointerInput` lambda/key per item on every recomposition, or gesture detection will restart and drop touch continuity during list reflows."
        ),
        relatedComponentIds = listOf("gesture-tap"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(tapGestures, dragGestures)
}
