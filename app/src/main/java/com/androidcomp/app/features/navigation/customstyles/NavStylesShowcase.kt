package com.androidcomp.app.features.navigation.customstyles

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
fun NavStylesShowcase(
    morphingSelectedIndex: Int,
    onSelectMorphing: (Int) -> Unit,
    underlineSelectedIndex: Int,
    onSelectUnderline: (Int) -> Unit,
    segmentedSelectedIndex: Int,
    onSelectSegmented: (Int) -> Unit,
    pillSelectedIndex: Int,
    onSelectPill: (Int) -> Unit,
    railSelectedIndex: Int,
    onSelectRail: (Int) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        NavStyleRow(
            "1. Morphing Indicator Bottom Nav",
            "A pill-shaped indicator glides smoothly between selected items.",
            { MorphingIndicatorBottomNav(morphingSelectedIndex, onSelectMorphing) },
            morphingIndicatorCode
        )
        NavStyleRow(
            "2. Sliding Underline Tabs",
            "A colored underline slides to sit beneath the selected tab.",
            { SlidingUnderlineTabs(underlineSelectedIndex, onSelectUnderline) },
            slidingUnderlineCode
        )
        NavStyleRow(
            "3. Segmented Control",
            "A pill-shaped background slides behind the selected option.",
            { SegmentedControlNav(segmentedSelectedIndex, onSelectSegmented) },
            segmentedControlCode
        )
        NavStyleRow(
            "4. Floating Pill Nav",
            "Compact icon-only bar; the selected item grows and gains a highlight.",
            { FloatingPillNav(pillSelectedIndex, onSelectPill) },
            floatingPillCode
        )
        NavStyleRow(
            "5. Rail Navigation",
            "A vertical narrow rail for tablet/landscape-style navigation.",
            { RailNavigation(railSelectedIndex, onSelectRail) },
            railNavigationCode
        )
    }
}

@Composable
private fun NavStyleRow(
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

private val morphingIndicatorCode = """
    BoxWithConstraints(Modifier.fillMaxWidth().height(64.dp)) {
        val itemWidth = maxWidth / icons.size
        val indicatorOffset by animateDpAsState(
            itemWidth * selectedIndex,
            animationSpec = spring(dampingRatio = 0.7f)
        )
        Box(
            Modifier
                .width(itemWidth)
                .graphicsLayer { translationX = indicatorOffset.toPx() }
                .clip(RoundedCornerShape(50))
                .background(primary)
        )
        Row { icons.forEachIndexed { index, icon -> /* tap -> onSelect(index) */ } }
    }
""".trimIndent()

private val slidingUnderlineCode = """
    BoxWithConstraints(Modifier.fillMaxWidth()) {
        val itemWidth = maxWidth / labels.size
        val underlineOffset by animateDpAsState(itemWidth * selectedIndex)

        Column {
            Row { labels.forEachIndexed { index, label -> /* tap -> onSelect(index) */ } }
            Box(
                Modifier
                    .width(itemWidth)
                    .height(3.dp)
                    .graphicsLayer { translationX = underlineOffset.toPx() }
                    .background(primary)
            )
        }
    }
""".trimIndent()

private val segmentedControlCode = """
    BoxWithConstraints(Modifier.fillMaxWidth().clip(RoundedCornerShape(50)).background(track)) {
        val itemWidth = maxWidth / labels.size
        val segmentOffset by animateDpAsState(itemWidth * selectedIndex)

        Box(
            Modifier
                .width(itemWidth)
                .graphicsLayer { translationX = segmentOffset.toPx() }
                .clip(RoundedCornerShape(50))
                .background(Color.White)
        )
        Row { labels.forEachIndexed { index, label -> /* tap -> onSelect(index) */ } }
    }
""".trimIndent()

private val floatingPillCode = """
    Row(Modifier.clip(RoundedCornerShape(50)).background(Color(0xFF1F2937))) {
        icons.forEachIndexed { index, icon ->
            val selected = selectedIndex == index
            val scale by animateFloatAsState(if (selected) 1.15f else 1f)
            val bgColor by animateColorAsState(if (selected) primary else Color.Transparent)
            Box(
                Modifier
                    .graphicsLayer { scaleX = scale; scaleY = scale }
                    .clip(CircleShape)
                    .background(bgColor)
                    .clickable { onSelect(index) }
            ) {
                Icon(icon, contentDescription = null, tint = Color.White)
            }
        }
    }
""".trimIndent()

private val railNavigationCode = """
    Column(Modifier.width(72.dp)) {
        destinations.forEachIndexed { index, destination ->
            val selected = selectedIndex == index
            val bgColor by animateColorAsState(if (selected) primary.copy(alpha = 0.12f) else Color.Transparent)
            val tint by animateColorAsState(if (selected) primary else Color.Gray)
            Column(
                Modifier
                    .clip(RoundedCornerShape(14.dp))
                    .background(bgColor)
                    .clickable { onSelect(index) }
            ) {
                Icon(destination.icon, contentDescription = destination.label, tint = tint)
                Text(destination.label, color = tint, fontSize = 10.sp)
            }
        }
    }
""".trimIndent()
