package io.rafaelribeiro.spendless.presentation.screens.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.navigation.Screen
import io.rafaelribeiro.spendless.navigation.rememberNavigationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinPromptRootScreen(
    navigationState: NavigationState,
    uiState: LoginUiState,
    modifier: Modifier,
) {
    Box {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {},
                    actions = {
                        FilledTonalIconButton(
                            colors = IconButtonColors(
                                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                                contentColor = MaterialTheme.colorScheme.error,
                                disabledContainerColor = Color.Transparent,
                                disabledContentColor = MaterialTheme.colorScheme.error.copy(alpha = 0.3f),
                            ),
                            shape = RoundedCornerShape(32.dp),
                            onClick = {
                                // todo: trigger LOGOUT (remove saved username and pin from DataStore)
                                navigationState.popBackStack() // this will clear the back stack due to triggerPinPromptScreen() navOptions
                                navigationState.navigateTo(Screen.LoginScreen.route) // Navigate to LoginScreen
                            },
                        ) {
                            Icon(
                                tint = MaterialTheme.colorScheme.error,
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Go back"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.background,
                    )
                )
            }
        ) { innerPadding ->
            PinPromptScreen(
                modifier = modifier.padding(innerPadding),
                title = "Hello, ${uiState.username}",
                description = stringResource(id = R.string.registration_pin_confirmation_description),
                currentPinSize = uiState.pin.length,
//                onNumberClick = { onEvent(RegistrationUiEvent.PinDigitTapped(it)) }, //todo wip
//                onBackspaceClick = { onEvent(RegistrationUiEvent.PinBackspaceTapped) },
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
    PinPromptRootScreen(
        navigationState = rememberNavigationState(),
        uiState = LoginUiState(),
        modifier = Modifier,
    )
}
