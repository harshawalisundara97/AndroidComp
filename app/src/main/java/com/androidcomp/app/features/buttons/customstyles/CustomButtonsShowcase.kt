package com.androidcomp.app.features.buttons.customstyles

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
fun CustomButtonsShowcase(
    loadState: ButtonLoadState,
    onLoadingButtonClick: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        ButtonStyleRow(
            "1. Gradient Glow",
            "Vibrant gradient pill that glows and scales down slightly on press.",
            { GradientGlowButton(onClick = {}) },
            gradientGlowCode
        )
        ButtonStyleRow(
            "2. Soft Neumorphic Press",
            "Carved-in look that visually \"sinks\" into the surface when pressed.",
            { NeumorphicPressButton(onClick = {}) },
            neumorphicPressCode
        )
        ButtonStyleRow(
            "3. Icon Slide",
            "Leading icon slides forward and rotates on press.",
            { IconSlideButton(onClick = {}) },
            iconSlideCode
        )
        ButtonStyleRow(
            "4. Animated Outline Sweep",
            "Outline fills with color left-to-right while pressed.",
            { AnimatedOutlineSweepButton(onClick = {}) },
            outlineSweepCode
        )
        ButtonStyleRow(
            "5. Loading State",
            "Tap to simulate an async action: idle → spinner → success checkmark → resets.",
            { LoadingStateButton(loadState = loadState, onClick = onLoadingButtonClick) },
            loadingStateCode
        )
    }
}

@Composable
private fun ButtonStyleRow(
    title: String,
    description: String,
    button: @Composable () -> Unit,
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
            button()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val gradientGlowCode = """
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (pressed) 0.96f else 1f)
    val glowElevation by animateDpAsState(if (pressed) 16.dp else 6.dp)

    Box(
        Modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .shadow(glowElevation, RoundedCornerShape(50), spotColor = Color(0xFF8B5CF6))
            .clip(RoundedCornerShape(50))
            .background(Brush.horizontalGradient(listOf(Color(0xFF6C5CE7), Color(0xFF00B4D8))))
            .clickable(interactionSource, indication = null) { onClick() }
            .padding(horizontal = 28.dp, vertical = 14.dp)
    ) {
        Text("Get Started", color = Color.White)
    }
""".trimIndent()

private val neumorphicPressCode = """
    val pressed by interactionSource.collectIsPressedAsState()
    val elevation by animateDpAsState(if (pressed) 0.dp else 6.dp)

    Box(
        Modifier
            .shadow(elevation, RoundedCornerShape(18.dp), ambientColor = Color(0xFFB8BCC8))
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFEDEEF2))
            .border(1.dp, Color(0xFFDBDEE6), RoundedCornerShape(18.dp))
            .clickable(interactionSource, indication = null) { onClick() }
            .padding(horizontal = 28.dp, vertical = 14.dp)
    ) {
        Text("Continue", color = Color(0xFF3A3D46))
    }
""".trimIndent()

private val iconSlideCode = """
    val pressed by interactionSource.collectIsPressedAsState()
    val iconOffset by animateDpAsState(
        if (pressed) 6.dp else 0.dp,
        animationSpec = spring(dampingRatio = 0.5f)
    )
    val iconRotation by animateFloatAsState(if (pressed) 20f else 0f)

    Row(
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF1F2937))
            .clickable(interactionSource, indication = null) { onClick() }
            .padding(horizontal = 24.dp, vertical = 14.dp)
    ) {
        Text("Send Message", color = Color.White)
        Icon(
            Icons.AutoMirrored.Outlined.Send,
            contentDescription = null,
            modifier = Modifier
                .offset(x = iconOffset)
                .graphicsLayer { rotationZ = iconRotation }
        )
    }
""".trimIndent()

private val outlineSweepCode = """
    val pressed by interactionSource.collectIsPressedAsState()
    val fillFraction by animateFloatAsState(if (pressed) 1f else 0f, tween(350))
    val textColor by animateColorAsState(
        if (fillFraction > 0.5f) Color.White else Color(0xFF2F6FED)
    )

    Box(
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .border(2.dp, Color(0xFF2F6FED), RoundedCornerShape(18.dp))
            .clickable(interactionSource, indication = null) { onClick() }
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .graphicsLayer {
                    transformOrigin = TransformOrigin(0f, 0.5f)
                    scaleX = fillFraction
                }
                .background(Color(0xFF2F6FED))
                .width(140.dp)
        )
        Text("Learn More", color = textColor, modifier = Modifier.padding(horizontal = 28.dp, vertical = 14.dp))
    }
""".trimIndent()

private val loadingStateCode = """
    enum class ButtonLoadState { IDLE, LOADING, SUCCESS }

    val containerColor by animateColorAsState(
        if (loadState == ButtonLoadState.SUCCESS) Color(0xFF2ECC71) else Color(0xFF2F6FED)
    )

    Box(
        Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(containerColor)
            .clickable(enabled = loadState == ButtonLoadState.IDLE) {
                scope.launch {
                    loadState = ButtonLoadState.LOADING
                    delay(1500)
                    loadState = ButtonLoadState.SUCCESS
                    delay(1200)
                    loadState = ButtonLoadState.IDLE
                }
            }
            .padding(horizontal = 28.dp, vertical = 14.dp)
    ) {
        AnimatedContent(loadState) { state ->
            when (state) {
                ButtonLoadState.IDLE -> Text("Save Changes", color = Color.White)
                ButtonLoadState.LOADING -> CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp))
                ButtonLoadState.SUCCESS -> Icon(Icons.Outlined.Check, contentDescription = "Saved", tint = Color.White)
            }
        }
    }
""".trimIndent()
