package com.androidcomp.app.features.textinputs.customstyles

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
fun CustomTextInputsShowcase(
    state: CustomTextInputsState,
    onFloatingLabelChange: (String) -> Unit,
    onSearchChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTogglePasswordVisible: () -> Unit,
    onUnderlineChange: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        InputStyleRow(
            "1. Floating Label Animated Border",
            "The border color and width animate when the field gains focus.",
            { FloatingLabelAnimatedBorderInput(state.floatingLabelValue, onFloatingLabelChange) },
            floatingLabelCode
        )
        InputStyleRow(
            "2. Search Bar with Clear Icon",
            "A pill-shaped input whose clear (X) icon fades in only once there's text.",
            { SearchBarWithClearInput(state.searchValue, onSearchChange) },
            searchBarCode
        )
        InputStyleRow(
            "3. PIN/OTP Code Input",
            "Boxed digit inputs backed by one string; each box lights up as digits are typed.",
            { OtpCodeInput(state.otpValue, onOtpChange) },
            otpCode
        )
        InputStyleRow(
            "4. Password Input with Visibility Toggle",
            "Trailing eye icon crossfades between shown/hidden as the transformation toggles.",
            {
                PasswordVisibilityInput(
                    value = state.password,
                    visible = state.passwordVisible,
                    onValueChange = onPasswordChange,
                    onToggleVisible = onTogglePasswordVisible
                )
            },
            passwordCode
        )
        InputStyleRow(
            "5. Minimal Underline Input",
            "No border box — just a bottom underline that thickens and colors on focus.",
            { MinimalUnderlineInput(state.underlineValue, onUnderlineChange) },
            underlineCode
        )
    }
}

@Composable
private fun InputStyleRow(
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
        Column(Modifier.fillMaxWidth().padding(bottom = 10.dp)) {
            content()
        }
        CodeBlock(CodeSample(language = CodeLanguage.COMPOSE, code = code))
    }
}

private val floatingLabelCode = """
    val focused by interactionSource.collectIsFocusedAsState()
    val borderColor by animateColorAsState(if (focused) Color(0xFF2F6FED) else Color(0xFFDBDEE6))
    val borderWidth by animateDpAsState(if (focused) 2.dp else 1.dp)

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text("Full Name") },
        interactionSource = interactionSource,
        modifier = Modifier.border(borderWidth, borderColor, RoundedCornerShape(16.dp))
    )
""".trimIndent()

private val searchBarCode = """
    Row(
        Modifier.clip(RoundedCornerShape(50)).background(Color(0xFFF2F2F2)).padding(horizontal = 16.dp)
    ) {
        Icon(Icons.Outlined.Search, contentDescription = null)
        BasicTextField(value = value, onValueChange = onValueChange, modifier = Modifier.weight(1f))
        AnimatedVisibility(visible = value.isNotEmpty(), enter = fadeIn(), exit = fadeOut()) {
            Icon(
                Icons.Outlined.Close,
                contentDescription = "Clear",
                modifier = Modifier.clickable { onValueChange("") }
            )
        }
    }
""".trimIndent()

private val otpCode = """
    BasicTextField(
        value = value,
        onValueChange = { new -> if (new.length <= 6 && new.all { it.isDigit() }) onValueChange(new) },
        textStyle = TextStyle(color = Color.Transparent),
        decorationBox = {
            Row {
                repeat(6) { index ->
                    val filled = index < value.length
                    val boxColor by animateColorAsState(if (filled) Color(0xFF2F6FED) else Color(0xFFDBDEE6))
                    Box(
                        Modifier.size(40.dp, 48.dp).border(1.5.dp, boxColor, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(value.getOrNull(index)?.toString() ?: "")
                    }
                }
            }
        }
    )
""".trimIndent()

private val passwordCode = """
    var visible by remember { mutableStateOf(false) }

    TextField(
        value = value,
        onValueChange = onValueChange,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { visible = !visible }) {
                AnimatedVisibility(visible, enter = fadeIn(), exit = fadeOut()) {
                    Icon(Icons.Outlined.Visibility, contentDescription = "Hide password")
                }
                AnimatedVisibility(!visible, enter = fadeIn(), exit = fadeOut()) {
                    Icon(Icons.Outlined.VisibilityOff, contentDescription = "Show password")
                }
            }
        }
    )
""".trimIndent()

private val underlineCode = """
    val focused by interactionSource.collectIsFocusedAsState()
    val underlineColor by animateColorAsState(if (focused) Color(0xFF2F6FED) else Color(0xFFC7CAD1))
    val underlineHeight by animateDpAsState(if (focused) 2.dp else 1.dp, tween(250))

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        interactionSource = interactionSource,
        decorationBox = { inner ->
            Column {
                inner()
                Box(Modifier.fillMaxWidth().height(underlineHeight).background(underlineColor))
            }
        }
    )
""".trimIndent()
