package com.androidcomp.app.features.notifications.customstyles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

private val CardBg = Color.White
private val Border = Color(0xFFF2F2F2)
private val Primary = Color(0xFF2F6FED)
private val Success = Color(0xFF2AA96A)
private val TextSecondary = Color(0xFF6B7280)

/** 1. Expandable Notification Card — tap to expand from a one-line summary to a full detail view. */
@Composable
fun ExpandableNotificationCard(expanded: Boolean, onToggle: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = CardBg,
        border = BorderStroke(1.dp, Border),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy))
            .clickable { onToggle() }
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(40.dp).clip(CircleShape).background(Primary.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = null, tint = Primary)
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text("New comment on your post", style = MaterialTheme.typography.bodyLarge)
                    if (!expanded) {
                        Text(
                            "Sarah: Great work on this!",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
                Text("2m", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(spring(dampingRatio = Spring.DampingRatioNoBouncy)) + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(Modifier.padding(top = 12.dp)) {
                    Text(
                        "Sarah: Great work on this! The layout really came together nicely, " +
                            "especially the spacing between sections.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                    Row(Modifier.padding(top = 10.dp)) {
                        TextButton(onClick = {}) { Text("Reply") }
                        TextButton(onClick = {}) { Text("Like") }
                    }
                }
            }
        }
    }
}

/** 2. Snackbar with Undo Action — slides up with a message and a shrinking countdown bar. */
@Composable
fun SnackbarUndoDemo(visible: Boolean, onShow: () -> Unit, onUndo: () -> Unit, onExpire: () -> Unit) {
    Button(onClick = onShow) { Text("Delete Item") }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        var fraction by remember { mutableFloatStateOf(1f) }
        val animatedFraction by animateFloatAsState(targetValue = fraction, animationSpec = tween(150), label = "undoFraction")

        LaunchedEffect(visible) {
            if (visible) {
                fraction = 1f
                val totalMs = 3000L
                val stepMs = 50L
                var elapsed = 0L
                while (elapsed < totalMs) {
                    delay(stepMs)
                    elapsed += stepMs
                    fraction = 1f - (elapsed.toFloat() / totalMs)
                }
                onExpire()
            }
        }

        Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFF2B2B2B)) {
            Column(Modifier.padding(16.dp).fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Item deleted", color = Color.White, modifier = Modifier.weight(1f))
                    TextButton(onClick = onUndo) { Text("UNDO", color = Color(0xFF8AB4FF)) }
                }
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(animatedFraction.coerceIn(0f, 1f))
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF8AB4FF))
                    )
                }
            }
        }
    }
}

/** 3. Badge Counter with Bounce — a numeric badge that bounces via spring animation on increment. */
@Composable
fun BadgeCounterBounceDemo(count: Int, onIncrement: () -> Unit) {
    var bump by remember { mutableFloatStateOf(1f) }
    LaunchedEffect(count) {
        if (count > 0) bump = 1.5f
    }
    val animatedScale by animateFloatAsState(
        targetValue = bump,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessMedium),
        label = "badgeBounce",
        finishedListener = { bump = 1f }
    )

    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(contentAlignment = Alignment.TopEnd) {
            Box(
                Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFEFF3FF)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Outlined.Message, contentDescription = null, tint = Primary)
            }
            if (count > 0) {
                Box(
                    Modifier
                        .padding(top = (-4).dp, end = (-4).dp)
                        .size(20.dp)
                        .graphicsLayer(scaleX = animatedScale, scaleY = animatedScale)
                        .clip(CircleShape)
                        .background(Color(0xFFE74C3C)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        count.coerceAtMost(99).toString(),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
        Spacer(Modifier.width(16.dp))
        Button(onClick = onIncrement) { Text("New Message") }
    }
}

/** 4. Grouped Notification Stack — 3 offset cards that fan out with a spring animation on tap. */
@Composable
fun GroupedNotificationStackDemo(expanded: Boolean, onToggle: () -> Unit) {
    val titles = listOf("3 new messages", "2 mentions", "1 reminder")
    Column(
        Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        titles.forEachIndexed { index, title ->
            val spacing by animateDpAsState(
                targetValue = if (expanded) 8.dp else (-44).dp,
                animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
                label = "stackSpacing$index"
            )
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = CardBg,
                border = BorderStroke(1.dp, Border),
                modifier = Modifier
                    .padding(top = if (index == 0) 0.dp else spacing)
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Outlined.Notifications, contentDescription = null, tint = Primary)
                    Spacer(Modifier.width(12.dp))
                    Text(title, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

/** 5. Toast-style Banner with Progress Timer — auto-dismisses after ~3s via a shrinking progress bar. */
@Composable
fun ToastProgressBannerDemo(visible: Boolean, onShow: () -> Unit, onDismiss: () -> Unit) {
    Button(onClick = onShow) { Text("Show Toast") }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
        exit = fadeOut()
    ) {
        var progress by remember { mutableFloatStateOf(1f) }
        val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(200), label = "toastProgress")

        LaunchedEffect(visible) {
            if (visible) {
                progress = 1f
                val totalMs = 3000L
                val stepMs = 50L
                var elapsed = 0L
                while (elapsed < totalMs) {
                    delay(stepMs)
                    elapsed += stepMs
                    progress = (1f - elapsed.toFloat() / totalMs).coerceAtLeast(0f)
                }
                onDismiss()
            }
        }

        Surface(shape = RoundedCornerShape(16.dp), color = Success.copy(alpha = 0.12f)) {
            Column(Modifier.padding(14.dp).fillMaxWidth()) {
                Text("Changes saved successfully", color = Success, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(8.dp))
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Success.copy(alpha = 0.2f))
                ) {
                    Box(
                        Modifier
                            .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                            .height(3.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Success)
                    )
                }
            }
        }
    }
}
