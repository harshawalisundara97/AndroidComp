package com.androidcomp.app.features.selectioncontrols.customstyles

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
fun SelectionStylesShowcase(state: SelectionStylesState, viewModel: SelectionStylesViewModel) {
    Column(Modifier.fillMaxWidth()) {
        SelectionStyleRow(
            "1. Card-Style Selectable Option",
            "A plan-picker card that highlights border and background on selection.",
            { CardStyleSelectableOption(state.selectedPlan, viewModel::selectPlan) },
            cardStyleCode
        )
        SelectionStyleRow(
            "2. Star Rating Selector",
            "Animated star fill with a tap-to-rate interaction.",
            { StarRatingSelector(state.starRating, viewModel::setStarRating) },
            starRatingCode
        )
        SelectionStyleRow(
            "3. Color Swatch Selector",
            "A row of color swatches with an animated selection ring.",
            { ColorSwatchSelector(state.selectedColorIndex, viewModel::selectColor) },
            colorSwatchCode
        )
        SelectionStyleRow(
            "4. Segmented Toggle Button Group",
            "A sliding-indicator segmented control.",
            { SegmentedToggleButtonGroup(state.segmentedIndex, viewModel::selectSegment) },
            segmentedCode
        )
        SelectionStyleRow(
            "5. Stepper Selector",
            "Increment/decrement counter with animated value change.",
            { StepperSelector(state.stepperCount, viewModel::incrementStepper, viewModel::decrementStepper) },
            stepperCode
        )
    }
}

@Composable
private fun SelectionStyleRow(
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
        Column(Modifier.padding(bottom = 10.dp)) { content() }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val cardStyleCode = """
    val selected = plan == selectedPlan
    val borderColor by animateColorAsState(
        if (selected) MaterialTheme.colorScheme.primary else Color(0xFFF2F2F2)
    )
    Card(
        modifier = Modifier.border(2.dp, borderColor, RoundedCornerShape(24.dp)).clickable { onSelect(plan) }
    ) { /* plan details */ }
""".trimIndent()

private val starRatingCode = """
    Row {
        (1..5).forEach { index ->
            val scale by animateFloatAsState(if (index <= rating) 1.15f else 1f)
            Icon(
                imageVector = if (index <= rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                contentDescription = null,
                modifier = Modifier.scale(scale).clickable { onRatingChange(index) }
            )
        }
    }
""".trimIndent()

private val colorSwatchCode = """
    Row {
        colors.forEachIndexed { index, color ->
            val ringWidth by animateDpAsState(if (index == selectedIndex) 3.dp else 0.dp)
            Box(
                Modifier
                    .size(40.dp)
                    .border(ringWidth, MaterialTheme.colorScheme.primary, CircleShape)
                    .background(color, CircleShape)
                    .clickable { onSelect(index) }
            )
        }
    }
""".trimIndent()

private val segmentedCode = """
    val indicatorOffset by animateDpAsState(segmentWidth * selectedIndex)
    Box {
        Box(Modifier.offset(x = indicatorOffset).background(MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp)))
        Row {
            options.forEachIndexed { index, label ->
                Text(label, modifier = Modifier.clickable { onSelect(index) })
            }
        }
    }
""".trimIndent()

private val stepperCode = """
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDecrement) { Icon(Icons.Outlined.Remove, contentDescription = "Decrease") }
        AnimatedContent(targetState = count) { value -> Text("${'$'}value") }
        IconButton(onClick = onIncrement) { Icon(Icons.Outlined.Add, contentDescription = "Increase") }
    }
""".trimIndent()
