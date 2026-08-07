package com.androidcomp.app.features.text.customstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun CustomTextsShowcase(
    state: CustomTextsState,
    onCycleGradient: () -> Unit,
    onToggleExpanded: () -> Unit,
    onTriggerCounter: () -> Unit,
    onToggleHighlight: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        TextStyleRow(
            "1. Gradient Text",
            "Tap to cycle through vibrant gradient fills applied via a TextStyle brush.",
            { GradientText(variant = state.gradientVariant, onTap = onCycleGradient) },
            gradientTextCode
        )
        TextStyleRow(
            "2. Typewriter Reveal",
            "Tap to replay the text animating in character-by-character.",
            { TypewriterRevealText() },
            typewriterCode
        )
        TextStyleRow(
            "3. Expandable \"Read More\" Text",
            "A paragraph clamped to 2 lines that animates its height open and closed.",
            { ExpandableReadMoreText(expanded = state.expanded, onToggle = onToggleExpanded) },
            expandableCode
        )
        TextStyleRow(
            "4. Animated Counter Text",
            "Tap to count up (or reset) with a smooth animated value.",
            { AnimatedCounterText(target = state.counterTarget, onTap = onTriggerCounter) },
            counterCode
        )
        TextStyleRow(
            "5. Highlight-on-Tap Text",
            "Tapping toggles an animated highlight background behind a word.",
            { HighlightOnTapText(highlighted = state.highlighted, onToggle = onToggleHighlight) },
            highlightCode
        )
    }
}

@Composable
private fun TextStyleRow(
    title: String,
    description: String,
    content: @Composable () -> Unit,
    code: String
) {
    Column(Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
        Text(title, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
        Text(
            description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 2.dp, bottom = 10.dp)
        )
        Column(Modifier.padding(bottom = 10.dp)) {
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val gradientTextCode = """
    val colors = listOf(Color(0xFF6C5CE7), Color(0xFF00B4D8))

    Text(
        "Design Beautifully",
        style = TextStyle(
            brush = Brush.linearGradient(colors),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.clickable { cycleGradient() }
    )
""".trimIndent()

private val typewriterCode = """
    var visibleChars by remember { mutableIntStateOf(fullText.length) }
    var trigger by remember { mutableIntStateOf(0) }

    LaunchedEffect(trigger) {
        if (trigger == 0) return@LaunchedEffect
        visibleChars = 0
        while (visibleChars < fullText.length) {
            delay(45)
            visibleChars++
        }
    }

    Text(
        fullText.take(visibleChars),
        modifier = Modifier.clickable { trigger++ }
    )
""".trimIndent()

private val expandableCode = """
    var expanded by remember { mutableStateOf(false) }

    Column(Modifier.animateContentSize(animationSpec = tween(300))) {
        Text(
            body,
            maxLines = if (expanded) Int.MAX_VALUE else 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            if (expanded) "Show less" else "Read more",
            modifier = Modifier.clickable { expanded = !expanded }
        )
    }
""".trimIndent()

private val counterCode = """
    var target by remember { mutableIntStateOf(0) }
    val animated by animateIntAsState(
        targetValue = target,
        animationSpec = tween(1200)
    )

    Text(
        "${'$'}animated",
        modifier = Modifier.clickable { target = if (target == 0) 2847 else 0 }
    )
""".trimIndent()

private val highlightCode = """
    var highlighted by remember { mutableStateOf(false) }
    val highlightColor by animateColorAsState(
        if (highlighted) Color(0xFFFFE066) else Color.Transparent
    )

    val annotated = buildAnnotatedString {
        append("Tap the word ")
        withStyle(SpanStyle(background = highlightColor)) { append("Compose") }
        append(" to highlight it.")
    }

    Text(annotated, modifier = Modifier.clickable { highlighted = !highlighted })
""".trimIndent()
