package com.androidcomp.app.features.permissions.customstyles

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
fun PermissionStylesShowcase(
    state: PermissionStylesState,
    onRationaleGrantedChange: (Boolean) -> Unit,
    onStatusChipCycle: () -> Unit,
    onSettingsBannerToggle: () -> Unit,
    onChecklistToggle: (String) -> Unit,
    onShieldGrantedChange: (Boolean) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        PermissionStyleRow(
            "1. Rationale Card",
            "Explains why access is needed; Allow animates the card into a Granted state.",
            {
                RationaleCard(
                    granted = state.rationaleGranted,
                    onAllow = { onRationaleGrantedChange(true) },
                    onNotNow = { onRationaleGrantedChange(false) }
                )
            },
            rationaleCardCode
        )
        PermissionStyleRow(
            "2. Status Chip",
            "Small chip showing Granted / Denied / Not requested; tap to cycle through states.",
            { StatusChip(state.statusChipState, onStatusChipCycle) },
            statusChipCode
        )
        PermissionStyleRow(
            "3. Settings Redirect Banner",
            "Warning banner shown when denied, with a mock \"Open Settings\" action.",
            { SettingsRedirectBanner(state.settingsBannerDismissed, onSettingsBannerToggle) },
            settingsBannerCode
        )
        PermissionStyleRow(
            "4. Permission Checklist",
            "List of permissions; tap a row to toggle its granted state, checkmark animates in.",
            { PermissionChecklist(state.checklist, onChecklistToggle) },
            permissionChecklistCode
        )
        PermissionStyleRow(
            "5. Animated Shield Prompt",
            "Centered shield icon pulses gently above a permission request message.",
            {
                AnimatedShieldPrompt(
                    granted = state.shieldPromptGranted,
                    onAllow = { onShieldGrantedChange(true) }
                )
            },
            shieldPromptCode
        )
    }
}

@Composable
private fun PermissionStyleRow(
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

private val rationaleCardCode = """
    val borderColor by animateColorAsState(
        if (granted) Color(0xFF2ECC71) else Color(0xFFF2F2F2)
    )
    Box(
        Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        AnimatedContent(granted) { isGranted ->
            if (isGranted) {
                // checkmark + "Access Granted" row
            } else {
                // icon + rationale text + Allow / Not now buttons
            }
        }
    }
""".trimIndent()

private val statusChipCode = """
    val (label, bg, fg) = when (state) {
        PermissionGrantState.GRANTED -> Triple("Granted", Color(0xFFE8F8EF), Color(0xFF2ECC71))
        PermissionGrantState.DENIED -> Triple("Denied", Color(0xFFFDEBEB), Color(0xFFE74C3C))
        PermissionGrantState.NOT_REQUESTED -> Triple("Not requested", Color(0xFFF2F2F2), Color(0xFF6B7280))
    }
    val bgColor by animateColorAsState(bg)
    val fgColor by animateColorAsState(fg)
    Box(
        Modifier
            .background(bgColor, RoundedCornerShape(50))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        AnimatedContent(label) { text -> Text(text, color = fgColor) }
    }
""".trimIndent()

private val settingsBannerCode = """
    AnimatedVisibility(
        visible = !dismissed,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(Color(0xFFFFF6E5), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Icon(Icons.Outlined.Warning, null, tint = Color(0xFFE67E22))
            Column(Modifier.weight(1f)) {
                Text("Permission required")
                Text("Enable this permission in Settings to continue.")
            }
            Text("Open Settings", modifier = Modifier.clickable { onOpenSettings() })
        }
    }
""".trimIndent()

private val permissionChecklistCode = """
    Column {
        items.forEach { item ->
            Row(Modifier.clickable { onToggle(item.label) }) {
                Icon(iconFor(item.label), null)
                Text(item.label, modifier = Modifier.weight(1f))
                AnimatedContent(item.granted) { granted ->
                    Icon(
                        if (granted) Icons.Outlined.CheckCircle else Icons.Outlined.Circle,
                        null,
                        tint = if (granted) Color(0xFF2ECC71) else Color(0xFFD0D0D4)
                    )
                }
            }
        }
    }
""".trimIndent()

private val shieldPromptCode = """
    val infiniteTransition = rememberInfiniteTransition()
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        Modifier
            .size(64.dp)
            .graphicsLayer {
                scaleX = if (granted) 1f else pulseScale
                scaleY = if (granted) 1f else pulseScale
            }
            .background(if (granted) Color(0xFFE8F8EF) else Color(0xFFEAF2FF), CircleShape)
    ) {
        Icon(if (granted) Icons.Outlined.Shield else Icons.Outlined.Lock, null)
    }
""".trimIndent()
