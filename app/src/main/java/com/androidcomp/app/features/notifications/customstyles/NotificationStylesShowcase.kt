package com.androidcomp.app.features.notifications.customstyles

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
fun NotificationStylesShowcase(state: NotificationStylesState, viewModel: NotificationStylesViewModel) {
    Column(Modifier.fillMaxWidth()) {
        NotificationStyleRow(
            "1. Expandable Notification Card",
            "Tap to expand from a one-line summary into a full detail view.",
            {
                ExpandableNotificationCard(state.expandableExpanded, viewModel::toggleExpandable)
            },
            expandableCode
        )
        NotificationStyleRow(
            "2. Snackbar with Undo Action",
            "Slides up with a message and a shrinking countdown bar; Undo cancels it early.",
            {
                SnackbarUndoDemo(
                    state.snackbarVisible,
                    viewModel::showSnackbar,
                    viewModel::dismissSnackbar,
                    viewModel::dismissSnackbar
                )
            },
            snackbarUndoCode
        )
        NotificationStyleRow(
            "3. Badge Counter with Bounce",
            "A numeric badge that bounces with a spring animation each time it increments.",
            {
                BadgeCounterBounceDemo(state.badgeCount, viewModel::incrementBadge)
            },
            badgeBounceCode
        )
        NotificationStyleRow(
            "4. Grouped Notification Stack",
            "Three stacked, offset cards that fan out with a spring animation when tapped.",
            {
                GroupedNotificationStackDemo(state.stackExpanded, viewModel::toggleStack)
            },
            groupedStackCode
        )
        NotificationStyleRow(
            "5. Toast-style Banner with Progress Timer",
            "A banner with a horizontal progress bar that shrinks over ~3s and auto-dismisses.",
            {
                ToastProgressBannerDemo(state.bannerVisible, viewModel::showBanner, viewModel::dismissBanner)
            },
            toastBannerCode
        )
    }
}

@Composable
private fun NotificationStyleRow(
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

private val expandableCode = """
    Surface(
        modifier = Modifier
            .animateContentSize(spring(dampingRatio = Spring.DampingRatioNoBouncy))
            .clickable { expanded = !expanded }
    ) {
        Column {
            Row { /* icon, title, one-line summary when collapsed */ }
            AnimatedVisibility(visible = expanded) {
                Text("Full detail text...")
            }
        }
    }
""".trimIndent()

private val snackbarUndoCode = """
    var fraction by remember { mutableFloatStateOf(1f) }
    val animatedFraction by animateFloatAsState(fraction)

    LaunchedEffect(visible) {
        val total = 3000L
        var elapsed = 0L
        while (elapsed < total) {
            delay(50); elapsed += 50
            fraction = 1f - elapsed.toFloat() / total
        }
        onExpire()
    }

    Row {
        Text("Item deleted")
        TextButton(onClick = onUndo) { Text("UNDO") }
    }
    Box(Modifier.fillMaxWidth(animatedFraction).height(3.dp).background(accentColor))
""".trimIndent()

private val badgeBounceCode = """
    var bump by remember { mutableFloatStateOf(1f) }
    LaunchedEffect(count) { if (count > 0) bump = 1.5f }

    val scale by animateFloatAsState(
        targetValue = bump,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        finishedListener = { bump = 1f }
    )

    Box(Modifier.graphicsLayer(scaleX = scale, scaleY = scale)) {
        Text(count.toString())
    }
""".trimIndent()

private val groupedStackCode = """
    titles.forEachIndexed { index, title ->
        val spacing by animateDpAsState(
            targetValue = if (expanded) 8.dp else (-44).dp,
            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
        )
        Surface(
            modifier = Modifier.padding(top = if (index == 0) 0.dp else spacing)
        ) {
            Row { Icon(...); Text(title) }
        }
    }
""".trimIndent()

private val toastBannerCode = """
    var progress by remember { mutableFloatStateOf(1f) }
    val animatedProgress by animateFloatAsState(progress)

    LaunchedEffect(visible) {
        val total = 3000L
        var elapsed = 0L
        while (elapsed < total) {
            delay(50); elapsed += 50
            progress = (1f - elapsed.toFloat() / total).coerceAtLeast(0f)
        }
        onDismiss()
    }

    Column {
        Text("Changes saved successfully")
        Box(Modifier.fillMaxWidth(animatedProgress).height(3.dp).background(Success))
    }
""".trimIndent()
