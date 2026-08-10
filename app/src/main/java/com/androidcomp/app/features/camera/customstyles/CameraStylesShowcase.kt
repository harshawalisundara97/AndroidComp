package com.androidcomp.app.features.camera.customstyles

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
fun CameraStylesShowcase(
    state: CameraStylesState,
    viewModel: CameraStylesViewModel
) {
    Column(Modifier.fillMaxWidth()) {
        CameraStyleRow(
            "1. Capture Button with Press Ring",
            "Shutter button shows an expanding, fading ring while pressed.",
            { CaptureButtonWithPressRing(state.shutterPressed, viewModel::setShutterPressed) },
            captureButtonCode
        )
        CameraStyleRow(
            "2. Viewfinder Grid Overlay",
            "Rule-of-thirds grid drawn on a mock viewfinder, fades in and out on tap.",
            { ViewfinderGridOverlay(state.gridVisible, viewModel::toggleGrid) },
            viewfinderGridCode
        )
        CameraStyleRow(
            "3. Flash Mode Toggle",
            "Cycles Off / Auto / On with a crossfading icon each tap.",
            { FlashModeToggle(state.flashMode, viewModel::cycleFlashMode) },
            flashModeCode
        )
        CameraStyleRow(
            "4. Photo/Video Mode Switcher",
            "Segmented control between Photo and Video with a sliding pill indicator.",
            { PhotoVideoModeSwitcher(state.captureMode, viewModel::setCaptureMode) },
            modeSwitcherCode
        )
        CameraStyleRow(
            "5. Shutter Countdown UI",
            "Tap the timer to run an animated 3-2-1 countdown before a capture flash.",
            { ShutterCountdownUi(state.countdownValue, state.justCaptured, viewModel::startCountdown) },
            countdownCode
        )
    }
}

@Composable
private fun CameraStyleRow(
    title: String,
    description: String,
    demo: @Composable () -> Unit,
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
            demo()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val captureButtonCode = """
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val ringScale by animateFloatAsState(if (isPressed) 1.7f else 1f, tween(350))
    val ringAlpha by animateFloatAsState(if (isPressed) 0f else 0.55f, tween(350))
    val buttonScale by animateFloatAsState(
        if (isPressed) 0.88f else 1f,
        spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(contentAlignment = Alignment.Center) {
        Box(Modifier.size(66.dp * ringScale).border(2.dp, Color.White.copy(alpha = ringAlpha), CircleShape))
        Box(
            Modifier
                .size(66.dp)
                .border(3.dp, Color.White, CircleShape)
                .clickable(interactionSource, indication = null) { onPressedChange(!pressed) }
        ) {
            Box(Modifier.size(48.dp * buttonScale).background(Color.White, CircleShape))
        }
    }
""".trimIndent()

private val viewfinderGridCode = """
    val gridAlpha by animateFloatAsState(if (gridVisible) 0.7f else 0f, tween(350))

    Box(Modifier.background(Color(0xFF16181D), RoundedCornerShape(12.dp)).clickable { onToggle() }) {
        Canvas(Modifier.fillMaxSize()) {
            val thirdW = size.width / 3f
            val thirdH = size.height / 3f
            val lineColor = Color.White.copy(alpha = gridAlpha)
            for (i in 1..2) {
                drawLine(lineColor, Offset(thirdW * i, 0f), Offset(thirdW * i, size.height))
                drawLine(lineColor, Offset(0f, thirdH * i), Offset(size.width, thirdH * i))
            }
        }
    }
""".trimIndent()

private val flashModeCode = """
    val label = when (flashMode) { 1 -> "Auto"; 2 -> "On"; else -> "Off" }

    Box(
        Modifier
            .background(Color(0xFF16181D), RoundedCornerShape(24.dp))
            .clickable { onCycle() }
    ) {
        AnimatedContent(targetState = flashMode) { mode ->
            Icon(
                when (mode) {
                    1 -> Icons.Outlined.FlashAuto
                    2 -> Icons.Outlined.FlashOn
                    else -> Icons.Outlined.FlashOff
                },
                contentDescription = "Flash mode: ${'$'}label"
            )
        }
    }
""".trimIndent()

private val modeSwitcherCode = """
    val indicatorOffset by animateDpAsState(
        if (mode == 0) 0.dp else segmentWidth,
        spring(dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessMedium)
    )

    Box(Modifier.background(Color(0xFF16181D), RoundedCornerShape(20.dp)).padding(3.dp)) {
        Box(
            Modifier
                .offset(x = indicatorOffset)
                .width(segmentWidth - 6.dp)
                .background(Color.White, RoundedCornerShape(18.dp))
        )
        Row {
            Box(Modifier.width(segmentWidth).clickable { onModeChange(0) }) { Text("Photo") }
            Box(Modifier.width(segmentWidth).clickable { onModeChange(1) }) { Text("Video") }
        }
    }
""".trimIndent()

private val countdownCode = """
    fun startCountdown() {
        viewModelScope.launch {
            for (value in 3 downTo 1) {
                _state.value = _state.value.copy(countdownValue = value)
                delay(700)
            }
            _state.value = _state.value.copy(countdownValue = null, justCaptured = true)
            delay(900)
            _state.value = _state.value.copy(justCaptured = false)
        }
    }

    AnimatedContent(
        targetState = countdownValue,
        transitionSpec = {
            (scaleIn(initialScale = 0.4f) + fadeIn()) togetherWith (scaleOut(targetScale = 1.6f) + fadeOut())
        }
    ) { value ->
        Text(text = value.toString(), fontSize = 48.sp, fontWeight = FontWeight.Bold)
    }
""".trimIndent()
