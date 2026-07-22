package com.androidcomp.app.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.androidcomp.app.core.ui.theme.LocalAppColors
import com.androidcomp.app.core.ui.theme.AppRadii
import com.androidcomp.app.domain.model.CodeSample

@Composable
fun CodeBlock(codeSample: CodeSample) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val border = LocalAppColors.current.border

    OutlinedCard(
        shape = RoundedCornerShape(AppRadii.card),
        border = BorderStroke(1.dp, border),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.outlinedCardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = codeSample.code,
                fontFamily = FontFamily.Monospace,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { clipboardManager.setText(AnnotatedString(codeSample.code)) }) {
                Icon(Icons.Outlined.ContentCopy, contentDescription = "Copy code")
            }
        }
    }
}
