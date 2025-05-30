package io.rafaelribeiro.spendless.core.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.presentation.theme.Success

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SpendLessMessageDialog(
    message: UiText,
    isError: Boolean = true,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = message.isNotEmpty(),
        enter = fadeIn(animationSpec = tween(durationMillis = 300)),
        exit = fadeOut(animationSpec = tween(durationMillis = 300)),
        modifier = modifier.imePadding(),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(if (isError) MaterialTheme.colorScheme.error else Success)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = message.asString(),
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
                    modifier = Modifier.align(Alignment.Center),
                )
            }

            AnimatedVisibility(visible = !WindowInsets.isImeVisible) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(16.dp)
                        .background(if (isError) MaterialTheme.colorScheme.error else Success)
                )
            }
        }
    }
}

@Preview
@Composable
fun ErrorDialogPreview() {
    SpendLessMessageDialog(message = UiText.DynamicString("Error message"))
}

@Preview
@Composable
fun SuccessDialogPreview() {
    SpendLessMessageDialog(
        message = UiText.DynamicString("Transaction saved!"),
        isError = false
    )
}

