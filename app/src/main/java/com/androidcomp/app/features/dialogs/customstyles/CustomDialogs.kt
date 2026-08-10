package com.androidcomp.app.features.dialogs.customstyles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay

/** 1. Bottom Sheet Dialog — opens a Material3 ModalBottomSheet with content and a close action. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDialogDemo(visible: Boolean, onShow: () -> Unit, onDismiss: () -> Unit) {
    Button(onClick = onShow) {
        Text("Open Bottom Sheet")
    }
    if (visible) {
        ModalBottomSheet(onDismissRequest = onDismiss, sheetState = rememberModalBottomSheetState()) {
            Column(Modifier.fillMaxWidth().padding(24.dp)) {
                Text("Notification Settings", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                Text(
                    "Choose how you'd like to be notified about account activity.",
                    color = Color(0xFF6B7280),
                    modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
                )
                Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                    Text("Done")
                }
            }
        }
    }
}

/** 2. Icon Confirmation Dialog — a custom dialog with a large centered icon, title, and actions. */
@Composable
fun IconConfirmationDialogDemo(visible: Boolean, onShow: () -> Unit, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Button(onClick = onShow) {
        Text("Delete Item")
    }
    if (visible) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(shape = RoundedCornerShape(28.dp), color = Color.White) {
                Column(
                    Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFF1F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Outlined.WarningAmber, contentDescription = null, tint = Color(0xFFE74C3C))
                    }
                    Text(
                        "Delete this item?",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        "This action can't be undone.",
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                    )
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Spacer(Modifier.width(12.dp))
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}

/** 3. Success Celebration Dialog — scale+fade entrance with a delayed checkmark reveal. */
@Composable
fun SuccessCelebrationDialogDemo(visible: Boolean, onShow: () -> Unit, onDismiss: () -> Unit) {
    Button(onClick = onShow) {
        Text("Complete Payment")
    }
    if (visible) {
        Dialog(onDismissRequest = onDismiss) {
            var checkVisible by remember { mutableStateOf(false) }
            LaunchedEffect(visible) {
                checkVisible = false
                delay(200)
                checkVisible = true
            }
            Surface(shape = RoundedCornerShape(28.dp), color = Color.White) {
                Column(
                    Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        visible = checkVisible,
                        enter = scaleIn(tween(400)) + fadeIn(tween(400)),
                        exit = scaleOut() + fadeOut()
                    ) {
                        Icon(
                            Icons.Outlined.CheckCircle,
                            contentDescription = "Success",
                            tint = Color(0xFF2ECC71),
                            modifier = Modifier.size(64.dp)
                        )
                    }
                    Text(
                        "Payment Successful",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        "Your receipt has been sent to your email.",
                        color = Color(0xFF6B7280),
                        modifier = Modifier.padding(top = 6.dp, bottom = 20.dp)
                    )
                    Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

/** 4. Input Dialog — a custom Dialog with a TextField plus Save/Cancel actions. */
@Composable
fun InputDialogDemo(
    visible: Boolean,
    text: String,
    onShow: () -> Unit,
    onTextChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    Button(onClick = onShow) {
        Text("Rename Item")
    }
    if (visible) {
        Dialog(onDismissRequest = onDismiss) {
            Surface(shape = RoundedCornerShape(28.dp), color = Color.White) {
                Column(Modifier.padding(24.dp)) {
                    Text("Rename", style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = text,
                        onValueChange = onTextChange,
                        label = { Text("Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 20.dp)
                    )
                    Row(Modifier.fillMaxWidth()) {
                        OutlinedButton(onClick = onDismiss, modifier = Modifier.weight(1f)) {
                            Text("Cancel")
                        }
                        Spacer(Modifier.width(12.dp))
                        Button(onClick = onSave, modifier = Modifier.weight(1f)) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

/** 5. Full-Bleed Image Dialog — a near-fullscreen image dialog with a close overlay button. */
@Composable
fun FullBleedImageDialogDemo(visible: Boolean, onShow: () -> Unit, onDismiss: () -> Unit) {
    Button(onClick = onShow) {
        Text("View Image")
    }
    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF1F2937))
            ) {
                Box(
                    Modifier.fillMaxWidth().fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Outlined.Image,
                        contentDescription = "Full-bleed image placeholder",
                        tint = Color(0xFF6B7280),
                        modifier = Modifier.size(96.dp)
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.4f))
                ) {
                    Icon(Icons.Outlined.Close, contentDescription = "Close", tint = Color.White)
                }
            }
        }
    }
}
