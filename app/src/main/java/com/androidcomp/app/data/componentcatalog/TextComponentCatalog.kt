package com.androidcomp.app.data.componentcatalog

import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample
import com.androidcomp.app.domain.model.ComponentCategory
import com.androidcomp.app.domain.model.ComponentProperty
import com.androidcomp.app.domain.model.ComponentSpec

object TextComponentCatalog {

    private val heading = ComponentSpec(
        id = "text-heading",
        category = ComponentCategory.TEXT,
        title = "Heading Text",
        overview = "A prominent text style using Material 3's headline scale, used for screen " +
            "titles and top-level section headings.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Text(
                    text = "Screen Title",
                    style = MaterialTheme.typography.headlineSmall
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textAppearance="?attr/textAppearanceHeadlineSmall"
                    android:text="Screen Title" />
            """.trimIndent()
        ),
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("text", "String", "required", "The text content to display."),
            ComponentProperty("style", "TextStyle", "LocalTextStyle.current", "Typography style, e.g. MaterialTheme.typography.headlineSmall."),
            ComponentProperty("color", "Color", "Color.Unspecified", "Text color; defaults to the style's color or content color."),
            ComponentProperty("maxLines", "Int", "Int.MAX_VALUE", "Maximum number of lines before truncation."),
            ComponentProperty("overflow", "TextOverflow", "TextOverflow.Clip", "How visual overflow is handled, e.g. Ellipsis.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Use MaterialTheme.typography roles rather than hardcoded font sizes to stay consistent across themes.",
            "Reserve headlineSmall/Medium/Large for true page or section titles, not body emphasis."
        ),
        commonMistakes = listOf(
            "Hardcoding fontSize/fontWeight instead of referencing a typography role, breaking Dynamic Type scaling.",
            "Using a heading style for emphasis inside body copy instead of bodyLarge with FontWeight.Bold."
        ),
        accessibilityNotes = listOf(
            "Text composed with sp-based typography scales automatically with the user's font size setting.",
            "Headings should read meaningfully out of context for screen reader users navigating by heading."
        ),
        performanceNotes = listOf(
            "Prefer a single Text with an AnnotatedString over multiple adjacent Text composables when mixing styles inline."
        ),
        relatedComponentIds = listOf("text-body"),
        minApi = 21
    )

    private val body = ComponentSpec(
        id = "text-body",
        category = ComponentCategory.TEXT,
        title = "Body Text",
        overview = "Standard readable text for paragraphs and general content, using Material 3's " +
            "body typography scale.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                Text(
                    text = "This is body copy used for general reading content.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            """.trimIndent()
        ),
        xmlCode = CodeSample(
            language = CodeLanguage.XML,
            code = """
                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:textAppearance="?attr/textAppearanceBodyLarge"
                    android:text="This is body copy used for general reading content." />
            """.trimIndent()
        ),
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("text", "String", "required", "The text content to display."),
            ComponentProperty("style", "TextStyle", "LocalTextStyle.current", "Typography style, e.g. MaterialTheme.typography.bodyLarge."),
            ComponentProperty("softWrap", "Boolean", "true", "Whether the text should break at soft line breaks."),
            ComponentProperty("textAlign", "TextAlign?", "null", "Horizontal alignment of the text.")
        ),
        events = emptyList(),
        bestPractices = listOf(
            "Use bodyLarge for primary reading content and bodyMedium/Small for denser secondary text.",
            "Set a line height via the typography style rather than manually, to keep vertical rhythm consistent."
        ),
        commonMistakes = listOf(
            "Using gray text on gray backgrounds without checking WCAG AA contrast ratios.",
            "Nesting Text inside Text — use AnnotatedString or buildAnnotatedString instead for mixed styling."
        ),
        accessibilityNotes = listOf(
            "Body text must maintain at least 4.5:1 contrast ratio against its background per WCAG AA.",
            "Avoid disabling text scaling by using non-scalable dp units for fontSize."
        ),
        performanceNotes = listOf(
            "Long static strings should be extracted as constants to avoid recreation on every recomposition."
        ),
        relatedComponentIds = listOf("text-heading"),
        minApi = 21
    )

    private val textCustomStyles = ComponentSpec(
        id = "text-custom-styles",
        category = ComponentCategory.TEXT,
        title = "Custom Text Styles",
        overview = "Five interactive text treatments beyond plain typography — gradient fills, a " +
            "typewriter reveal, an expandable \"read more\" paragraph, a count-up number, and a " +
            "tap-to-highlight phrase — each driven by genuine Compose animations. Tap each one " +
            "below to see it respond, and copy its Compose code to reuse directly.",
        composeCode = CodeSample(
            language = CodeLanguage.COMPOSE,
            code = """
                @Composable
                fun GradientText(variant: Int, onTap: () -> Unit) {
                    val colors = gradientCombos[variant % gradientCombos.size]
                    Text(
                        "Design Beautifully",
                        style = TextStyle(brush = Brush.linearGradient(colors), fontSize = 26.sp),
                        modifier = Modifier.clickable { onTap() }
                    )
                }
            """.trimIndent()
        ),
        xmlCode = null,
        viewModelUsage = null,
        properties = listOf(
            ComponentProperty("TextStyle(brush = ...)", "Brush", "n/a", "Fills glyph outlines with a gradient instead of a flat color."),
            ComponentProperty("visibleChars state", "Int", "0", "Drives the typewriter reveal by slicing the source string with take(n)."),
            ComponentProperty("Modifier.animateContentSize()", "AnimationSpec<IntSize>", "spring()", "Animates the expandable text's height when its maxLines changes."),
            ComponentProperty("animateIntAsState target", "Int", "0", "Counts up/down toward the target value for the animated counter.")
        ),
        events = listOf("onTap / onToggle — fired when the user taps the text to trigger its animation or state change."),
        bestPractices = listOf(
            "Keep typewriter and counter animation durations short (under ~1.5s total) so they feel responsive, not sluggish.",
            "Use TextOverflow.Ellipsis with a fixed maxLines for the clamped state so collapse/expand never jumps unexpectedly."
        ),
        commonMistakes = listOf(
            "Restarting a LaunchedEffect-driven animation on every recomposition by keying it on a value that changes too often.",
            "Relying on color alone (e.g. a highlight) to convey meaning without any other visual or textual cue for accessibility."
        ),
        accessibilityNotes = listOf(
            "Interactive text (tap-to-cycle, tap-to-expand) should be at least 48x48dp in touch target size, not just the glyph bounds.",
            "Gradient-filled text can reduce contrast against certain backgrounds — verify WCAG AA against the darkest gradient stop."
        ),
        performanceNotes = listOf(
            "animateIntAsState and animateColorAsState are cheap for single text elements; avoid restarting them unnecessarily via unstable lambda keys.",
            "buildAnnotatedString allocations are lightweight but should be remembered/derived rather than rebuilt on every frame during animation."
        ),
        relatedComponentIds = listOf("text-heading", "text-body"),
        minApi = 21
    )

    val all: List<ComponentSpec> = listOf(heading, body, textCustomStyles)
}
