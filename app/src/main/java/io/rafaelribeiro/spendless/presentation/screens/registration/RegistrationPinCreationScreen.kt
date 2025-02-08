package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.PinPromptScreen

@Composable
fun RegistrationPinPromptScreen(
	uiState: RegistrationUiState,
	onEvent: (RegistrationUiEvent) -> Unit,
	modifier: Modifier,
) {
	PinPromptScreen(
		modifier = modifier,
		title = stringResource(id = R.string.create_pin),
		description = stringResource(id = R.string.use_pin_to_login),
		currentPinSize = uiState.pin.length,
		onNumberClick = { onEvent(RegistrationUiEvent.PinDigitTapped(it)) },
		onBackspaceClick = { onEvent(RegistrationUiEvent.PinBackspaceTapped) },
	)
}

@Composable
fun RegistrationPinConfirmationScreen(
	uiState: RegistrationUiState,
	onEvent: (RegistrationUiEvent) -> Unit,
	modifier: Modifier,
) {
	PinPromptScreen(
		modifier = modifier,
		title = stringResource(id = R.string.repeat_your_pin),
		description = stringResource(id = R.string.enter_your_pin_again),
		currentPinSize = uiState.pinConfirmation.length,
		onNumberClick = { onEvent(RegistrationUiEvent.PinConfirmationDigitTapped(it)) },
		onBackspaceClick = { onEvent(RegistrationUiEvent.PinConfirmationBackspaceTapped) },
	)
}
