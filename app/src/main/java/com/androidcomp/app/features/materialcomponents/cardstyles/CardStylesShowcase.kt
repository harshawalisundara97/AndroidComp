package com.androidcomp.app.features.materialcomponents.cardstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.CodeBlock
import com.androidcomp.app.domain.model.CodeLanguage
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun CardStylesShowcase(
    state: CardStylesState,
    onStatCardIncrement: () -> Unit,
    onImageCardToggleSaved: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        CardStyleRow(
            "1. Stat Card",
            "Tap to simulate a live-updating metric.",
            { StatCard(value = state.statValue, onIncrement = onStatCardIncrement) },
            statCardCode
        )
        CardStyleRow(
            "2. Image Card",
            "Tap the bookmark icon to toggle saved state.",
            { ImageCard(saved = state.imageSaved, onToggleSaved = onImageCardToggleSaved) },
            imageCardCode
        )
        CardStyleRow(
            "3. Gradient Card",
            "Vibrant gradient surface with a press glow.",
            { GradientCard(onClick = {}) },
            gradientCardCode
        )
        CardStyleRow(
            "4. Minimal Bordered Card",
            "Flat, thin border, list-item style row.",
            { MinimalBorderedCard(onClick = {}) },
            minimalBorderedCardCode
        )
        CardStyleRow(
            "5. Elevated Interactive Card",
            "Elevation rises and falls on press.",
            { ElevatedInteractiveCard(onClick = {}) },
            elevatedInteractiveCardCode
        )
    }
}

@Composable
private fun CardStyleRow(
    title: String,
    description: String,
    card: @Composable () -> Unit,
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
        Row(Modifier.padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            card()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val statCardCode = """
    Box(
        Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF5F6FA))
            .clickable { onIncrement() }
            .padding(20.dp)
    ) {
        Column {
            Text("Total Downloads", color = Color(0xFF6B7280))
            AnimatedContent(value) { v -> Text("${'$'}v", fontSize = 28.sp) }
            Row {
                Icon(Icons.Outlined.TrendingUp, null, tint = Color(0xFF2ECC71))
                Text(" +12% this week", color = Color(0xFF2ECC71))
            }
        }
    }
""".trimIndent()

private val imageCardCode = """
    Column(
        Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFEDEEF2), RoundedCornerShape(24.dp))
    ) {
        Box(Modifier.fillMaxWidth().height(110.dp).background(Color(0xFFE0E7FF))) {
            Icon(Icons.Outlined.Image, contentDescription = null)
        }
        Row(Modifier.padding(14.dp)) {
            Text("Mountain Sunrise")
            IconButton(onClick = onToggleSaved) {
                Icon(if (saved) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder, null)
            }
        }
    }
""".trimIndent()

private val gradientCardCode = """
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val glowElevation by animateDpAsState(if (pressed) 14.dp else 4.dp)

    Box(
        Modifier
            .shadow(glowElevation, RoundedCornerShape(24.dp), spotColor = Color(0xFF8B5CF6))
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFFEC4899))))
            .clickable(interactionSource, indication = null) { onClick() }
            .padding(20.dp)
    ) {
        Column {
            Text("Premium Plan", color = Color.White)
            Text("Unlock all features", color = Color.White.copy(alpha = 0.85f))
        }
    }
""".trimIndent()

private val minimalBorderedCardCode = """
    Row(
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text("Account Settings")
        Icon(Icons.Outlined.ArrowForward, contentDescription = null)
    }
""".trimIndent()

private val elevatedInteractiveCardCode = """
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(if (pressed) 2.dp else 8.dp)

    Box(
        Modifier
            .shadow(elevation, RoundedCornerShape(24.dp), ambientColor = Color(0xFFB8BCC8))
            .clip(RoundedCornerShape(24.dp))
            .background(Color.White)
            .clickable(interactionSource, indication = null) { onClick() }
            .padding(20.dp)
    ) {
        Row {
            Icon(Icons.Outlined.TrendingUp, contentDescription = null)
            Column {
                Text("View Analytics")
                Text("Updated 2m ago")
            }
        }
    }
""".trimIndent()
