package com.androidcomp.app.features.selectioncontrols.toggles

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
fun ToggleStylesShowcase(
    state: ToggleStylesState,
    onFluidSpringChange: (Boolean) -> Unit,
    onDayNightChange: (Boolean) -> Unit,
    onCyberpunkNeonChange: (Boolean) -> Unit,
    onNeumorphicChange: (Boolean) -> Unit,
    onElasticPillChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        ToggleStyleRow(
            "1. Material You Fluid Spring",
            "Indigo track, spring-bounce thumb, X morphs into a checkmark.",
            { FluidSpringToggle(state.fluidSpring, onFluidSpringChange) },
            fluidSpringCode
        )
        ToggleStyleRow(
            "2. Day & Night Morph",
            "Sky-blue to midnight-navy, sun rotates and morphs into a moon.",
            { DayNightToggle(state.dayNight, onDayNightChange) },
            dayNightCode
        )
        ToggleStyleRow(
            "3. Cyberpunk Neon",
            "Dark box with a glowing cyan border, mechanical snap — no bounce.",
            { CyberpunkNeonToggle(state.cyberpunkNeon, onCyberpunkNeonChange) },
            cyberpunkNeonCode
        )
        ToggleStyleRow(
            "4. Soft Neumorphic UI",
            "Carved-in track, soft glide, inner LED lights up emerald when active.",
            { NeumorphicToggle(state.neumorphic, onNeumorphicChange) },
            neumorphicCode
        )
        ToggleStyleRow(
            "5. Minimalist Elastic Pill",
            "Flat gray-to-emerald track, thumb squishes like rubber mid-slide.",
            { ElasticPillToggle(state.elasticPill, onElasticPillChange) },
            elasticPillCode
        )
    }
}

@Composable
private fun ToggleStyleRow(
    title: String,
    description: String,
    toggle: @Composable () -> Unit,
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
            toggle()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val fluidSpringCode = """
    val trackColor by animateColorAsState(
        if (checked) Color(0xFF3949AB) else Color(0xFF8B93A8)
    )
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )
    Box(
        Modifier
            .size(56.dp, 32.dp)
            .background(trackColor, RoundedCornerShape(50))
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(Modifier.offset(x = thumbOffset).size(24.dp).background(Color.White, CircleShape)) {
            AnimatedContent(checked) { isChecked ->
                Icon(if (isChecked) Icons.Outlined.Check else Icons.Outlined.Close, null)
            }
        }
    }
""".trimIndent()

private val dayNightCode = """
    val trackColor by animateColorAsState(
        if (checked) Color(0xFF0D1B4C) else Color(0xFF4FC3F7),
        tween(500)
    )
    val rotation by animateFloatAsState(if (checked) 360f else 0f, tween(500))
    Box(
        Modifier
            .size(56.dp, 32.dp)
            .background(trackColor, RoundedCornerShape(50))
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(Modifier.offset(x = thumbOffset).graphicsLayer { rotationZ = rotation }) {
            AnimatedContent(checked) { isChecked ->
                Icon(if (isChecked) Icons.Outlined.Bedtime else Icons.Outlined.WbSunny, null)
            }
        }
    }
""".trimIndent()

private val cyberpunkNeonCode = """
    val glowColor by animateColorAsState(
        if (checked) Color(0xFF00E5FF) else Color(0xFF3A3F4B),
        tween(150, easing = LinearEasing)
    )
    Box(
        Modifier
            .size(56.dp, 32.dp)
            .background(Color(0xFF0A0A0F), RoundedCornerShape(8.dp))
            .border(2.dp, glowColor, RoundedCornerShape(8.dp))
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(
            Modifier
                .offset(x = thumbOffset)
                .size(24.dp)
                .background(thumbColor, RoundedCornerShape(4.dp))
        )
    }
""".trimIndent()

private val neumorphicCode = """
    val ledColor by animateColorAsState(
        if (checked) Color(0xFF2ECC71) else Color(0xFF444444)
    )
    Box(
        Modifier
            .size(56.dp, 32.dp)
            .background(Color(0xFFE4E4E7), RoundedCornerShape(50))
            .border(1.dp, Color(0xFFCFCFD4), RoundedCornerShape(50))
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(Modifier.offset(x = thumbOffset).size(24.dp).background(Color(0xFFFAFAFA), CircleShape)) {
            Box(Modifier.size(6.dp).background(ledColor, CircleShape))
        }
    }
""".trimIndent()

private val elasticPillCode = """
    val trackColor by animateColorAsState(
        if (checked) Color(0xFF2ECC71) else Color(0xFFD0D0D0)
    )
    val scaleX by animateFloatAsState(
        1f, keyframes { durationMillis = 300; 1f at 0; 1.6f at 150; 1f at 300 }
    )
    val scaleY by animateFloatAsState(
        1f, keyframes { durationMillis = 300; 1f at 0; 0.7f at 150; 1f at 300 }
    )
    Box(
        Modifier
            .size(56.dp, 32.dp)
            .background(trackColor, RoundedCornerShape(50))
            .clickable { onCheckedChange(!checked) }
    ) {
        Box(
            Modifier
                .offset(x = thumbOffset)
                .graphicsLayer { this.scaleX = scaleX; this.scaleY = scaleY }
                .size(24.dp)
                .background(Color.White, CircleShape)
        )
    }
""".trimIndent()
