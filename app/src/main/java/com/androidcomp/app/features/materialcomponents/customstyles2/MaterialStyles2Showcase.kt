package com.androidcomp.app.features.materialcomponents.customstyles2

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
fun MaterialStyles2Showcase(state: MaterialStyles2State, viewModel: MaterialStyles2ViewModel) {
    Column(Modifier.fillMaxWidth()) {
        MaterialStyleRow(
            "1. Calendar Date Picker Grid",
            "A DatePicker-styled month grid with an animated selection circle.",
            {
                CalendarGridPicker(state.selectedDate, viewModel::selectDate)
            },
            calendarGridCode
        )
        MaterialStyleRow(
            "2. Clock Dial Time Picker",
            "A TimePicker-styled analog dial whose hand animates to the tapped hour.",
            {
                ClockDialPicker(state.selectedHour, viewModel::selectHour)
            },
            clockDialCode
        )
        MaterialStyleRow(
            "3. Animated Navigation Rail",
            "A vertical NavigationRail with a pill indicator that slides between destinations.",
            {
                AnimatedNavigationRail(state.selectedRailIndex, viewModel::selectRailIndex)
            },
            navigationRailCode
        )
        MaterialStyleRow(
            "4. Swipe-to-Dismiss Snackbar",
            "A Snackbar with an action that can also be swiped away, fading as it goes.",
            {
                SwipeToDismissSnackbarDemo(state.snackbarVisible, viewModel::showSnackbar, viewModel::dismissSnackbar)
            },
            swipeSnackbarCode
        )
        MaterialStyleRow(
            "5. Animated Dropdown Select",
            "An ExposedDropdownMenu-style field whose chevron rotates as options expand.",
            {
                AnimatedDropdownSelect(
                    state.dropdownExpanded,
                    state.selectedOption,
                    viewModel::toggleDropdown,
                    viewModel::selectOption
                )
            },
            dropdownSelectCode
        )
    }
}

@Composable
private fun MaterialStyleRow(
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
        Row(Modifier.padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val calendarGridCode = """
    days.chunked(7).forEach { week ->
        Row {
            week.forEach { day ->
                val selected = day == selectedDate
                val bgColor by animateColorAsState(
                    if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
                )
                Box(
                    Modifier.size(32.dp).clip(CircleShape).background(bgColor)
                        .clickable { onSelectDate(day) }
                ) { Text(day.toString()) }
            }
        }
    }
""".trimIndent()

private val clockDialCode = """
    val animatedAngle by animateFloatAsState((selectedHour % 12) * 30f - 90f)
    Canvas(Modifier.size(212.dp)) {
        val angleRad = Math.toRadians(animatedAngle.toDouble())
        val handEnd = Offset(
            center.x + handLength * cos(angleRad).toFloat(),
            center.y + handLength * sin(angleRad).toFloat()
        )
        drawLine(color = handColor, start = center, end = handEnd, strokeWidth = 6f)
    }
    // 12 tappable hour labels positioned around the circle via trig
""".trimIndent()

private val navigationRailCode = """
    val indicatorOffset by animateDpAsState(itemHeight * selectedIndex)
    Box {
        Box(
            Modifier.offset(y = indicatorOffset).width(56.dp).height(40.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Column {
            items.forEachIndexed { index, (icon, label) ->
                Column(Modifier.clickable { onSelect(index) }) {
                    Icon(icon, contentDescription = label)
                    Text(label)
                }
            }
        }
    }
""".trimIndent()

private val swipeSnackbarCode = """
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    Row(
        Modifier
            .offset { IntOffset(offsetX.value.roundToInt(), 0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        scope.launch {
                            if (abs(offsetX.value) > 250f) {
                                offsetX.animateTo(if (offsetX.value > 0) 800f else -800f)
                                onDismiss()
                                offsetX.snapTo(0f)
                            } else {
                                offsetX.animateTo(0f)
                            }
                        }
                    }
                ) { change, dragAmount ->
                    change.consume()
                    scope.launch { offsetX.snapTo(offsetX.value + dragAmount) }
                }
            }
    ) {
        Text("Message archived")
        Text("UNDO", modifier = Modifier.clickable { onDismiss() })
    }
""".trimIndent()

private val dropdownSelectCode = """
    val chevronRotation by animateFloatAsState(if (expanded) 180f else 0f)

    Row(Modifier.clickable { onToggle() }) {
        Text(selected)
        Icon(Icons.Outlined.ExpandMore, contentDescription = null, modifier = Modifier.rotate(chevronRotation))
    }
    AnimatedVisibility(visible = expanded, enter = expandVertically() + fadeIn(), exit = shrinkVertically() + fadeOut()) {
        Column {
            options.forEach { option ->
                Row(Modifier.clickable { onSelectOption(option) }) { Text(option) }
            }
        }
    }
""".trimIndent()
