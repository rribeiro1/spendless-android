package io.rafaelribeiro.spendless.presentation.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.navOptions
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.ErrorDialog
import io.rafaelribeiro.spendless.core.presentation.SpendLessButton
import io.rafaelribeiro.spendless.core.presentation.SpendLessTextField
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.navigation.Screen
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginRootScreen(
    navigationState: NavigationState,
    modifier: Modifier,
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit = {},
) {
    Box {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {},
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = MaterialTheme.colorScheme.background,
                    )
                )
            }
        ) { innerPadding ->
            LoginScreen(
                modifier = modifier.padding(innerPadding),
                uiState = uiState,
                onEvent = onEvent,
                onNewAccountClick = {
                    navigationState.navigateTo(
                        route = Screen.RegistrationUsername.route,
                        navOptions = navOptions {
                            popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        }
                    )
                },
            )
        }
        ErrorDialog(
            modifier = modifier.align(Alignment.BottomCenter),
            errorMessage = uiState.errorMessage,
        )
    }
}

@Composable
fun LoginScreen(
    modifier: Modifier,
    uiState: LoginUiState,
    onEvent: (LoginUiEvent) -> Unit,
    onNewAccountClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.icon),
            contentDescription = "App Icon",
            modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
        )
        Text(
            text = stringResource(R.string.welcome_back),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp),
        )
        Text(
            text = stringResource(R.string.enter_details),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 36.dp),
        )
        SpendLessTextField(
            text = uiState.username,
            textPlaceholder = stringResource(R.string.username_placeholder),
            onValueChange = { onEvent(LoginUiEvent.UsernameChanged(it)) },
            isFocused = uiState.isUsernameFocused,
            onFocusChange = { onEvent(LoginUiEvent.UsernameFocusChanged(it)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) },
            ),
            focusRequester = focusRequester,
            modifier = Modifier.padding(bottom = 16.dp),
        )
        SpendLessTextField(
            text = uiState.pin,
            textPlaceholder = stringResource(R.string.pin_placeholder),
            onValueChange = { onEvent(LoginUiEvent.PinChanged(it)) },
            isFocused = uiState.isPinFocused,
            onFocusChange = { onEvent(LoginUiEvent.PinFocusChanged(it)) },
            isPassword = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onEvent(LoginUiEvent.ActionButtonLoginClicked)
                },
            ),
            modifier = Modifier.padding(bottom = 24.dp),
        )
        SpendLessButton(
            text = stringResource(R.string.login),
            modifier = Modifier.padding(horizontal = 16.dp),
            enabled = uiState.loginButtonEnabled,
            onClick = { onEvent(LoginUiEvent.ActionButtonLoginClicked) }
        )
        Text(
            text = stringResource(R.string.new_to_spendless),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 40.dp)
                .clickable(onClick = onNewAccountClick),
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    SpendLessTheme {
        LoginScreen(
            modifier = Modifier.fillMaxSize(),
            uiState = LoginUiState(),
            onEvent = {},
        )
    }
}
