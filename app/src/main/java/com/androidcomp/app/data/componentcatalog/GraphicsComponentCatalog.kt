package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object GraphicsComponentCatalog {

    private val canvas = ComponentSpec(
        id = "graphics-canvas",
        category = ComponentCategory.GRAPHICS,
        title = "Canvas",
        overview = "A Compose primitive that exposes a `DrawScope` for drawing arbitrary vector " +
            "graphics — shapes, paths, arcs, and text — directly onto the screen without needing " +
            "a custom `View`.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Canvas(modifier = Modifier.size(200.dp)) {
                    drawCircle(
                        color = Color.Blue,
                        radius = size.minDimension / 2,
                        center = center
                    )
                    drawLine(
                        color = Color.Red,
                        start = Offset(0f, 0f),
                        end = Offset(size.width, size.height),
                        strokeWidth = 4f
                    )
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = """
            val chartPoints: StateFlow<List<Offset>> = viewModel.chartPoints
            // Canvas reads chartPoints.collectAsState() and draws a path through them.
        """.trimIndent(),
        properties = listOf(
            ComponentProperty("modifier", "Modifier", "Modifier", "Sizing/positioning applied to the drawing surface."),
            ComponentProperty("onDraw", "DrawScope.() -> Unit", "required", "Lambda receiver where all draw calls (drawCircle, drawRect, drawPath, ...) occur."),
            ComponentProperty("size (via DrawScope)", "Size", "n/a", "Available drawing bounds, read inside the onDraw lambda."),
            ComponentProperty("center (via DrawScope)", "Offset", "n/a", "Center point of the drawing area, useful for radial shapes.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Precompute expensive geometry (e.g. `Path` objects) outside the draw lambda with `remember` so it isn't rebuilt every frame.",
            "Prefer `drawWithCache` when the drawing depends on size but not on frequently-changing state, to avoid recreating Paint/Path objects unnecessarily."
        ),
        commonMistakes = listOf(
            "Allocating new Paint, Path, or Brush objects inside the onDraw lambda on every recomposition instead of hoisting/remembering them.",
            "Assuming Canvas participates in layout like a normal composable child — it only draws; content sizing must come from Modifier.size or a parent constraint."
        ),
        accessibilityNotes = listOf(
            "Canvas content is invisible to screen readers by default — overlay a semantics-described composable or `Modifier.semantics` if the drawing conveys information.",
            "Do not rely on color alone to convey meaning in charts drawn on Canvas; pair with text labels or patterns for low-vision users."
        ),
        performanceNotes = listOf(
            "DrawScope operations run on every recomposition/draw pass — keep the lambda allocation-free for smooth 60/120fps animations.",
            "Use `graphicsLayer` for transforms (rotation/scale/alpha) instead of recomputing draw geometry, since layer transforms are handled by the rendering layer, not redrawn geometry."
        ),
        relatedComponentIds = listOf("graphics-drawbehind"),
        minApi = 21
    )

    private val drawBehind = ComponentSpec(
        id = "graphics-drawbehind",
        category = ComponentCategory.GRAPHICS,
        title = "Modifier.drawBehind / graphicsLayer",
        overview = "`Modifier.drawBehind` lets any composable draw custom content behind itself " +
            "(e.g. a custom background or underline) without needing a separate Canvas; " +
            "`Modifier.graphicsLayer` applies GPU-accelerated transforms (scale, rotate, alpha, " +
            "shadow) to a composable's rendered layer.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Text(
                    text = "Underlined",
                    modifier = Modifier.drawBehind {
                        val strokeWidthPx = 2.dp.toPx()
                        val y = size.height - strokeWidthPx
                        drawLine(
                            color = Color.Black,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidthPx
                        )
                    }
                )

                Box(
                    modifier = Modifier.graphicsLayer(
                        scaleX = 1.1f,
                        scaleY = 1.1f,
                        alpha = 0.9f,
                        rotationZ = 5f
                    )
                )
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("onDraw (drawBehind)", "DrawScope.() -> Unit", "required", "Draw content positioned behind the composable's own content."),
            ComponentProperty("scaleX/scaleY", "Float", "1f", "Horizontal/vertical scale applied to the layer."),
            ComponentProperty("alpha", "Float", "1f", "Layer opacity from 0f (transparent) to 1f (opaque)."),
            ComponentProperty("rotationZ", "Float", "0f", "Rotation in degrees around the layer's center."),
            ComponentProperty("shadowElevation", "Float", "0f", "Elevation used to draw a shadow for this layer.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Use graphicsLayer for animated transforms instead of animating layout-affecting properties (like padding), since layer transforms skip re-layout entirely.",
            "Combine `drawBehind` with `remember`-ed values when the drawn content depends on expensive calculations, to avoid recomputation on unrelated recompositions."
        ),
        commonMistakes = listOf(
            "Using `Modifier.scale`/`rotate` (which affect layout measurement) when a purely visual, non-layout-affecting transform via graphicsLayer was intended.",
            "Forgetting that drawBehind draws UNDER the composable's own content — content drawn wants drawWithContent or Modifier.drawWithContent for overlay effects."
        ),
        accessibilityNotes = listOf(
            "Purely decorative drawBehind content should not carry semantic meaning; keep meaningful text as real Text composables, not drawn glyphs.",
            "graphicsLayer alpha animations should still leave interactive targets fully opaque/visible above WCAG AA minimums when settled."
        ),
        performanceNotes = listOf(
            "graphicsLayer transforms are composited on the GPU and avoid triggering measure/layout passes, making them cheaper than layout-based animations.",
            "Overusing shadowElevation across many layers increases compositing cost — keep elevation subtle and limited per the design system's low-elevation guidance."
        ),
        relatedComponentIds = listOf("graphics-canvas"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(canvas, drawBehind)
}
