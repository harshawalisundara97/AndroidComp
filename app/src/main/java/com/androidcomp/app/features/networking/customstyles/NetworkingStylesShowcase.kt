package com.androidcomp.app.features.networking.customstyles

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
fun NetworkingStylesShowcase(
    state: NetworkingStylesState,
    onToggleConnection: () -> Unit,
    onRetry: () -> Unit,
    onSimulateResponse: () -> Unit,
    onSendRequest: () -> Unit,
    onCycleSignal: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        NetworkingStyleRow(
            "1. Connection Status Banner",
            "Slides in from the top showing offline (red) or back online (green).",
            { ConnectionStatusBanner(state.isOnline, onToggleConnection) },
            connectionStatusBannerCode
        )
        NetworkingStyleRow(
            "2. Retry Card on Failure",
            "Error icon and message; Retry briefly spins then alternates success/failure.",
            { RetryFailureCard(state.retryLoading, state.retryFailed, onRetry) },
            retryFailureCardCode
        )
        NetworkingStyleRow(
            "3. Shimmer Loading for Network Content",
            "Shimmering placeholder rows while \"waiting for API response\", then fades in content.",
            { ShimmerLoadingContent(state.shimmerContentLoaded, onSimulateResponse) },
            shimmerLoadingCode
        )
        NetworkingStyleRow(
            "4. Request/Response Demo Card",
            "Tap to animate through Sending → Awaiting response → 200 OK.",
            { RequestResponseCard(state.requestPhase, onSendRequest) },
            requestResponseCardCode
        )
        NetworkingStyleRow(
            "5. Network Speed Indicator",
            "Simulated connection quality gauge with colored signal bars, tap to cycle.",
            { NetworkSpeedIndicator(state.signalLevel, onCycleSignal) },
            networkSpeedIndicatorCode
        )
    }
}

@Composable
private fun NetworkingStyleRow(
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
        Row(Modifier.fillMaxWidth().padding(bottom = 10.dp), verticalAlignment = Alignment.CenterVertically) {
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val connectionStatusBannerCode = """
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut()
    ) {
        val bg by animateColorAsState(if (isOnline) Color(0xFFE8F8EF) else Color(0xFFFDEBEB))
        Row(Modifier.fillMaxWidth().background(bg, RoundedCornerShape(16.dp)).padding(16.dp)) {
            Icon(if (isOnline) Icons.Outlined.CloudDone else Icons.Outlined.CloudOff, null)
            Text(if (isOnline) "Back online" else "You're offline")
        }
    }
""".trimIndent()

private val retryFailureCardCode = """
    Column(Modifier.background(Color.White, RoundedCornerShape(24.dp)).padding(20.dp)) {
        Icon(Icons.Outlined.ErrorOutline, null, tint = Color(0xFFE74C3C))
        Text("Couldn't load data")
        Button(onClick = onRetry, enabled = !loading) {
            if (loading) CircularProgressIndicator(Modifier.size(16.dp)) else Text("Retry")
        }
    }
""".trimIndent()

private val shimmerLoadingCode = """
    val shimmerAlpha by rememberInfiniteTransition().animateFloat(
        0.3f, 0.9f,
        infiniteRepeatable(tween(700), RepeatMode.Reverse)
    )
    AnimatedVisibility(visible = !loaded) {
        Column { repeat(3) { Box(Modifier.fillMaxWidth().height(16.dp).alpha(shimmerAlpha).background(Color(0xFFE4E4E7))) } }
    }
    AnimatedVisibility(visible = loaded, enter = fadeIn()) { Text("Response received") }
""".trimIndent()

private val requestResponseCardCode = """
    Column(Modifier.clickable { onTap() }.background(Color.White, RoundedCornerShape(24.dp)).padding(20.dp)) {
        AnimatedContent(phase) { p ->
            Text(
                when (p) {
                    RequestPhase.SENDING -> "Sending request..."
                    RequestPhase.AWAITING -> "Awaiting response..."
                    RequestPhase.DONE -> "200 OK"
                    RequestPhase.IDLE -> "Tap to send request"
                }
            )
        }
    }
""".trimIndent()

private val networkSpeedIndicatorCode = """
    Row(Modifier.clickable { onTap() }) {
        Icon(Icons.Outlined.SignalCellularAlt, null, tint = colorFor(level))
        AnimatedContent(level) { l -> Text(labelFor(l)) }
    }
""".trimIndent()
