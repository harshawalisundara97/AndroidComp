package com.androidcomp.app.features.dialogs.customstyles

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
fun DialogStylesShowcase(state: DialogStylesState, viewModel: DialogStylesViewModel) {
    Column(Modifier.fillMaxWidth()) {
        DialogStyleRow(
            "1. Bottom Sheet Dialog",
            "Opens a Material 3 ModalBottomSheet with actions.",
            {
                Button(onClick = viewModel::showBottomSheet) { Text("Show Bottom Sheet") }
                BottomSheetDialogDemo(state.bottomSheetVisible, viewModel::showBottomSheet, viewModel::hideBottomSheet)
            },
            bottomSheetCode
        )
        DialogStyleRow(
            "2. Icon Confirmation Dialog",
            "A custom AlertDialog with a large centered icon.",
            {
                Button(onClick = viewModel::showIconConfirm) { Text("Show Confirmation") }
                IconConfirmationDialogDemo(state.iconConfirmVisible, viewModel::showIconConfirm, viewModel::hideIconConfirm, viewModel::hideIconConfirm)
            },
            iconConfirmationCode
        )
        DialogStyleRow(
            "3. Success Celebration Dialog",
            "Scale+fade entrance with a delayed checkmark reveal.",
            {
                Button(onClick = viewModel::showSuccessCelebration) { Text("Show Success") }
                SuccessCelebrationDialogDemo(state.successCelebrationVisible, viewModel::showSuccessCelebration, viewModel::hideSuccessCelebration)
            },
            successCelebrationCode
        )
        DialogStyleRow(
            "4. Input Dialog",
            "A custom Dialog containing a TextField and Save/Cancel.",
            {
                Button(onClick = viewModel::showInputDialog) { Text("Show Input Dialog") }
                InputDialogDemo(
                    state.inputDialogVisible,
                    state.inputText,
                    viewModel::showInputDialog,
                    viewModel::updateInputText,
                    viewModel::hideInputDialog,
                    viewModel::hideInputDialog
                )
            },
            inputDialogCode
        )
        DialogStyleRow(
            "5. Full-Bleed Image Dialog",
            "A full-width Dialog with a close button overlay.",
            {
                Button(onClick = viewModel::showFullBleedImage) { Text("Show Full-Bleed") }
                FullBleedImageDialogDemo(state.fullBleedImageVisible, viewModel::showFullBleedImage, viewModel::hideFullBleedImage)
            },
            fullBleedImageCode
        )
    }
}

@Composable
private fun DialogStyleRow(
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

private val bottomSheetCode = """
    var showSheet by remember { mutableStateOf(false) }
    Button(onClick = { showSheet = true }) { Text("Show Bottom Sheet") }

    if (showSheet) {
        ModalBottomSheet(onDismissRequest = { showSheet = false }) {
            Text("Actions", modifier = Modifier.padding(16.dp))
            // list of DropdownMenuItem-style rows...
        }
    }
""".trimIndent()

private val iconConfirmationCode = """
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
            title = { Text("Delete item?") },
            text = { Text("This can't be undone.") },
            confirmButton = { TextButton(onClick = onConfirm) { Text("Delete") } },
            dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
        )
    }
""".trimIndent()

private val successCelebrationCode = """
    AnimatedVisibility(
        visible = showDialog,
        enter = fadeIn() + scaleIn(),
        exit = fadeOut() + scaleOut()
    ) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(shape = RoundedCornerShape(24.dp)) {
                Column {
                    // icon reveals after a short delay via LaunchedEffect
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null)
                    Text("Success!")
                    Button(onClick = onDismiss) { Text("OK") }
                }
            }
        }
    }
""".trimIndent()

private val inputDialogCode = """
    if (showDialog) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(shape = RoundedCornerShape(24.dp)) {
                Column(Modifier.padding(20.dp)) {
                    TextField(value = text, onValueChange = onTextChange)
                    Row {
                        TextButton(onClick = onDismiss) { Text("Cancel") }
                        Button(onClick = onSave) { Text("Save") }
                    }
                }
            }
        }
    }
""".trimIndent()

private val fullBleedImageCode = """
    if (showDialog) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(Modifier.fillMaxSize().background(Color.Black)) {
                // full-bleed image placeholder
                IconButton(onClick = onDismiss, modifier = Modifier.align(Alignment.TopEnd)) {
                    Icon(Icons.Outlined.Close, contentDescription = "Close", tint = Color.White)
                }
            }
        }
    }
""".trimIndent()
