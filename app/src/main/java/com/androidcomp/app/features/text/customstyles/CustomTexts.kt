package com.androidcomp.app.features.text.customstyles

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val gradientCombos = listOf(
    listOf(Color(0xFF6C5CE7), Color(0xFF00B4D8)),
    listOf(Color(0xFFFF6B6B), Color(0xFFFFA751)),
    listOf(Color(0xFF11998E), Color(0xFF38EF7D))
)

/** 1. Gradient Text — tap to cycle through vibrant gradient fills. */
@Composable
fun GradientText(variant: Int, onTap: () -> Unit) {
    val colors = gradientCombos[variant % gradientCombos.size]
    Text(
        "Design Beautifully",
        style = TextStyle(
            brush = Brush.linearGradient(colors),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        ),
        modifier = Modifier.clickable { onTap() }
    )
}

/** 2. Typewriter Reveal — text animates in character-by-character on tap. */
@Composable
fun TypewriterRevealText() {
    val fullText = "Great UI takes patience."
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
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .clickable { trigger++ }
            .padding(vertical = 4.dp)
    )
}

/** 3. Expandable "Read More" Text — clamped paragraph that animates height on toggle. */
@Composable
fun ExpandableReadMoreText(expanded: Boolean, onToggle: () -> Unit) {
    val body = "Jetpack Compose lets you build native UI with a declarative Kotlin API. " +
        "It combines a reactive programming model with the conciseness and ease of use of the " +
        "Kotlin language, making it possible to write beautiful, performant apps faster."

    Column(Modifier.animateContentSize(animationSpec = tween(300))) {
        Text(
            body,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = if (expanded) Int.MAX_VALUE else 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            if (expanded) "Show less" else "Read more",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .clickable { onToggle() }
                .padding(top = 6.dp)
        )
    }
}

/** 4. Animated Counter Text — number count-up animates when tapped. */
@Composable
fun AnimatedCounterText(target: Int, onTap: () -> Unit) {
    val animated by animateIntAsState(
        targetValue = target,
        animationSpec = tween(1200),
        label = "counterAnimation"
    )
    Text(
        "$animated",
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.clickable { onTap() }
    )
}

/** 5. Highlight-on-Tap Text — tapping toggles an animated highlight background behind a word. */
@Composable
fun HighlightOnTapText(highlighted: Boolean, onToggle: () -> Unit) {
    val highlightColor by animateColorAsState(
        if (highlighted) Color(0xFFFFE066) else Color.Transparent,
        label = "highlightColor"
    )
    val annotated = buildAnnotatedString {
        append("Tap the word ")
        withStyle(SpanStyle(background = highlightColor)) {
            append("Compose")
        }
        append(" to highlight it.")
    }
    Text(
        annotated,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.clickable { onToggle() }
    )
}
