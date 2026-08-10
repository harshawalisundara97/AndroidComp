package com.androidcomp.app.features.textinputs.customstyles

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** 1. Floating Label Animated Border — border color/width animates on focus. */
@Composable
fun FloatingLabelAnimatedBorderInput(value: String, onValueChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(
        if (focused) Color(0xFF2F6FED) else Color(0xFFDBDEE6),
        label = "floatingLabelBorderColor"
    )
    val borderWidth by animateDpAsState(
        if (focused) 2.dp else 1.dp,
        label = "floatingLabelBorderWidth"
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Full Name") },
        singleLine = true,
        interactionSource = interactionSource,
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        ),
        modifier = Modifier.border(borderWidth, borderColor, RoundedCornerShape(16.dp))
    )
}

/** 2. Search Bar with Clear Icon — trailing clear icon fades in only when text is non-empty. */
@Composable
fun SearchBarWithClearInput(value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(Color(0xFFF2F2F2))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Outlined.Search, contentDescription = null, tint = Color(0xFF8A8F98), modifier = Modifier.size(20.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 12.dp)
                .weight(1f)
        )
        AnimatedVisibility(visible = value.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
            Icon(
                Icons.Outlined.Close,
                contentDescription = "Clear",
                tint = Color(0xFF8A8F98),
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { onValueChange("") }
            )
        }
    }
}

/** 3. PIN/OTP Code Input — boxed digit inputs backed by a single string, real keyboard capture. */
@Composable
fun OtpCodeInput(value: String, onValueChange: (String) -> Unit, length: Int = 6) {
    BasicTextField(
        value = value,
        onValueChange = { new -> if (new.length <= length && new.all { it.isDigit() }) onValueChange(new) },
        textStyle = TextStyle(color = Color.Transparent),
        decorationBox = {
            Row {
                repeat(length) { index ->
                    val filled = index < value.length
                    val digit = value.getOrNull(index)?.toString() ?: ""
                    val boxColor by animateColorAsState(
                        if (filled) Color(0xFF2F6FED) else Color(0xFFDBDEE6),
                        label = "otpBoxColor$index"
                    )
                    Box(
                        modifier = Modifier
                            .padding(end = if (index != length - 1) 8.dp else 0.dp)
                            .size(40.dp, 48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .border(1.5.dp, boxColor, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(digit, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    )
}

/** 4. Password Input with Visibility Toggle — trailing eye icon crossfades open/closed. */
@Composable
fun PasswordVisibilityInput(value: String, visible: Boolean, onValueChange: (String) -> Unit, onToggleVisible: () -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Password") },
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggleVisible) {
                AnimatedVisibility(visible = visible, enter = fadeIn(), exit = fadeOut()) {
                    Icon(Icons.Outlined.Visibility, contentDescription = "Hide password")
                }
                AnimatedVisibility(visible = !visible, enter = fadeIn(), exit = fadeOut()) {
                    Icon(Icons.Outlined.VisibilityOff, contentDescription = "Show password")
                }
            }
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color(0xFFF2F2F2),
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        )
    )
}

/** 5. Minimal Underline Input — bottom underline animates thin/gray to thick/colored on focus. */
@Composable
fun MinimalUnderlineInput(value: String, onValueChange: (String) -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()
    val underlineColor by animateColorAsState(
        if (focused) Color(0xFF2F6FED) else Color(0xFFC7CAD1),
        label = "underlineColor"
    )
    val underlineHeight by animateDpAsState(
        if (focused) 2.dp else 1.dp,
        animationSpec = tween(250),
        label = "underlineHeight"
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        interactionSource = interactionSource,
        textStyle = TextStyle(fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface),
        decorationBox = { inner ->
            Column(Modifier.fillMaxWidth()) {
                Box(Modifier.padding(vertical = 10.dp)) {
                    if (value.isEmpty()) {
                        Text("Username", fontSize = 16.sp, color = Color(0xFFA6ABB3))
                    }
                    inner()
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(underlineHeight)
                        .background(underlineColor)
                )
            }
        }
    )
}
