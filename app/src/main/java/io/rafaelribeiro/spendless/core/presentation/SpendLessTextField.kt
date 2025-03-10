package io.rafaelribeiro.spendless.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun SpendLessTextField(
    text: String,
    textPlaceholder: String,
    onValueChange: (String) -> Unit,
    onFocusChange: (Boolean) -> Unit,
    isFocused: Boolean = false,
    isPassword: Boolean = false,
    focusRequester: FocusRequester = remember { FocusRequester() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = modifier
                .height(48.dp)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .focused(isFocused)
                .spendlessShadow()
                .background(MaterialTheme.colorScheme.onPrimary)
                .onFocusChanged { onFocusChange(it.isFocused) }
        ) {
            BasicTextField(
                value = text,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .focusRequester(focusRequester),
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (text.isEmpty()) {
                            Text(
                                text = textPlaceholder,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
fun Modifier.focused(isFocused: Boolean): Modifier {
    return if (isFocused) {
        this.border(
            width = 1.dp,
            color = MaterialTheme.colorScheme.primary,
            shape = RoundedCornerShape(16.dp)
        )
    } else {
        this
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
fun PreviewSpendLessTextField() {
    SpendLessTheme {
        SpendLessTextField(
            text = "",
            textPlaceholder = "Username",
            onValueChange = {},
            isFocused = false,
            onFocusChange = {},
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
fun PreviewSpendLessFocusedTextField() {
    SpendLessTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            SpendLessTextField(
                text = "Username",
                textPlaceholder = "Username",
                onValueChange = {},
                isFocused = true,
                onFocusChange = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
fun PreviewSpendLessPasswordFocusedTextField() {
    SpendLessTheme {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            SpendLessTextField(
                text = "12345",
                textPlaceholder = "PIN",
                onValueChange = {},
                isFocused = true,
                isPassword = true,
                onFocusChange = {},
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
