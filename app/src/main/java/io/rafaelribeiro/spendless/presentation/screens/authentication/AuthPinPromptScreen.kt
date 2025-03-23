package io.rafaelribeiro.spendless.presentation.screens.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.ErrorDialog
import io.rafaelribeiro.spendless.core.presentation.PinPromptScreen
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthPinPromptScreen(
    uiState: AuthPinUiState,
    onEvent: (AuthPinUiEvent) -> Unit = {},
    modifier: Modifier,
) {
    val isPinLocked = uiState.pinLockRemainingSeconds > 0
    Box {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        FilledTonalIconButton(
                            modifier = modifier.size(44.dp),
                            colors = IconButtonColors(
                                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                                contentColor = MaterialTheme.colorScheme.error,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                            ),
                            shape = RoundedCornerShape(16.dp),
                            onClick = { onEvent(AuthPinUiEvent.LogoutTapped) },
                        ) {
                            Icon(
                                tint = MaterialTheme.colorScheme.error,
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                modifier = Modifier.size(24.dp),
                                contentDescription = "Go back",
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.background,
                    ),
                    modifier = Modifier.padding(end = 12.dp).background(Color.Transparent),
                )
            }
        ) { innerPadding ->
            val title = if (isPinLocked) stringResource(R.string.too_many_failed_attempts) else stringResource(R.string.hello_user, uiState.username)
            val description = if (isPinLocked) stringResource(id = R.string.try_pin_again_in) else stringResource(id = R.string.login_enter_pin)
            PinPromptScreen(
                modifier = modifier.padding(innerPadding),
                title = title,
                description = description,
                pinLockRemainingSeconds = uiState.pinLockRemainingSeconds,
                currentPinSize = uiState.pin.length,
                pinPadEnabled = uiState.pinPadEnabled,
                biometricsEnabled = uiState.biometricsEnabled,
                onNumberClick = { onEvent(AuthPinUiEvent.PinDigitTapped(it)) },
                onBackspaceClick = { onEvent(AuthPinUiEvent.PinBackspaceTapped) },
                onBiometricsClick = { onEvent(AuthPinUiEvent.BiometricsTapped) },
            )
        }
        ErrorDialog(
            modifier = modifier.align(Alignment.BottomCenter),
            errorMessage = uiState.errorMessage,
        )
    }
}

@Preview
@Composable
fun PinPromptScreenPreview() {
    SpendLessTheme {
        AuthPinPromptScreen(
            uiState = AuthPinUiState(),
            modifier = Modifier,
        )
    }
}

@Preview
@Composable
fun PinPromptScreenPreviewLocked() {
    SpendLessTheme {
        AuthPinPromptScreen(
            uiState = AuthPinUiState(
                pinLockRemainingSeconds = 30,
                pinPadEnabled = false,
            ),
            modifier = Modifier,
        )
    }
}
