package com.androidcomp.app.features.networking.customstyles

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.SignalCellularAlt
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/** 1. Connection Status Banner — slides in from the top, red when offline, green when back online. */
@Composable
fun ConnectionStatusBanner(isOnline: Boolean, onToggle: () -> Unit) {
    val bannerColor by animateColorAsState(
        targetValue = if (isOnline) Color(0xFF2ECC71) else Color(0xFFE74C3C),
        label = "connectionBannerColor"
    )
    Column(Modifier.fillMaxWidth()) {
        AnimatedContent(
            targetState = isOnline,
            label = "connectionBannerContent",
            transitionSpec = {
                (slideInVertically { -it } + fadeIn()) togetherWith (slideOutVertically { -it } + fadeOut())
            }
        ) { online ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bannerColor, RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    if (online) Icons.Outlined.CloudDone else Icons.Outlined.CloudOff,
                    contentDescription = null,
                    tint = Color.White
                )
                Text(
                    if (online) "Back online" else "You're offline",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        Row(
            Modifier
                .padding(top = 10.dp)
                .clickable { onToggle() }
        ) {
            Text(
                "Toggle connection",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/** 2. Retry Card on Failure — shows an error state, tap Retry to briefly spin then alternate success/failure. */
@Composable
fun RetryFailureCard(loading: Boolean, failed: Boolean, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(targetState = Triple(loading, failed, Unit), label = "retryCardIcon") { (isLoading, isFailed, _) ->
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.size(36.dp), strokeWidth = 3.dp)
                isFailed -> Icon(
                    Icons.Outlined.ErrorOutline,
                    contentDescription = null,
                    tint = Color(0xFFE74C3C),
                    modifier = Modifier.size(36.dp)
                )
                else -> Icon(
                    Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF2ECC71),
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Text(
            when {
                loading -> "Retrying..."
                failed -> "Couldn't load data"
                else -> "Data loaded successfully"
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 12.dp, bottom = 16.dp)
        )
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(18.dp))
                .clickable(enabled = !loading) { onRetry() }
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(Icons.Outlined.Refresh, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Text("Retry", color = Color.White, style = MaterialTheme.typography.labelLarge)
        }
    }
}

/** 3. Shimmer Loading for Network Content — shimmering placeholder rows while "waiting for API response". */
@Composable
fun ShimmerLoadingContent(loaded: Boolean, onSimulate: () -> Unit) {
    Column(Modifier.fillMaxWidth()) {
        AnimatedContent(targetState = loaded, label = "shimmerContent") { isLoaded ->
            if (isLoaded) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Text("Response received", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurface)
                    Text(
                        "Here is the freshly loaded content from the API.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(3) { index ->
                        ShimmerRow(widthFraction = if (index == 2) 0.5f else 1f)
                    }
                }
            }
        }
        Row(
            Modifier
                .padding(top = 14.dp)
                .clickable { onSimulate() }
        ) {
            Text(
                "Simulate response arriving",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun ShimmerRow(widthFraction: Float) {
    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val alpha by transition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmerAlpha"
    )
    Box(
        Modifier
            .fillMaxWidth(widthFraction)
            .height(16.dp)
            .background(
                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = alpha),
                RoundedCornerShape(6.dp)
            )
    )
}

/** 4. Request/Response Demo Card — animated phase sequence: sending -> awaiting -> 200 OK. */
@Composable
fun RequestResponseCard(phase: RequestPhase, onTap: () -> Unit) {
    val phaseColor by animateColorAsState(
        targetValue = when (phase) {
            RequestPhase.DONE -> Color(0xFF2ECC71)
            RequestPhase.IDLE -> MaterialTheme.colorScheme.onSurfaceVariant
            else -> Color(0xFF3498DB)
        },
        label = "requestPhaseColor"
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp))
            .clickable { onTap() }
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.AutoMirrored.Outlined.Send, contentDescription = null, tint = phaseColor, modifier = Modifier.size(28.dp))
        AnimatedContent(
            targetState = phase,
            label = "requestPhaseText",
            transitionSpec = { (fadeIn() + slideInVertically { it / 2 }) togetherWith (fadeOut() + slideOutVertically { -it / 2 }) }
        ) { currentPhase ->
            Text(
                when (currentPhase) {
                    RequestPhase.IDLE -> "Tap to send request"
                    RequestPhase.SENDING -> "Sending request..."
                    RequestPhase.AWAITING -> "Awaiting response..."
                    RequestPhase.DONE -> "200 OK"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = phaseColor,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

/** 5. Network Speed Indicator — signal-bar gauge cycling through Excellent/Good/Poor on tap. */
@Composable
fun NetworkSpeedIndicator(level: SignalLevel, onTap: () -> Unit) {
    val (label, color, activeBars) = when (level) {
        SignalLevel.EXCELLENT -> Triple("Excellent", Color(0xFF2ECC71), 3)
        SignalLevel.GOOD -> Triple("Good", Color(0xFFF39C12), 2)
        SignalLevel.POOR -> Triple("Poor", Color(0xFFE74C3C), 1)
    }
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(16.dp))
            .clickable { onTap() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            repeat(3) { index ->
                val targetHeight = 8.dp + (index * 6).dp
                val barColor by animateColorAsState(
                    targetValue = if (index < activeBars) color else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                    label = "signalBarColor$index"
                )
                val barHeight by animateDpAsState(targetValue = targetHeight, label = "signalBarHeight$index")
                Box(
                    Modifier
                        .width(6.dp)
                        .height(barHeight)
                        .background(barColor, RoundedCornerShape(2.dp))
                )
            }
        }
        Icon(Icons.Outlined.SignalCellularAlt, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Text(label, color = color, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
    }
}
