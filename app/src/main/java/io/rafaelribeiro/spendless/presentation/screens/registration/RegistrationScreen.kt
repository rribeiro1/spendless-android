package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.ErrorDialog


@Composable
fun RegistrationScreen(
	modifier: Modifier = Modifier,
) {
	val viewModel: RegistrationViewModel = hiltViewModel()
	val uiState by viewModel.uiState.collectAsState()
	Box(
		contentAlignment = Alignment.BottomEnd,
	) {
		when (uiState.registrationStage) {
			RegistrationStage.INITIAL -> RegistrationUsernameScreen(
				modifier = modifier,
				username = uiState.username,
				onNextClick = viewModel::checkUserName,
				onUsernameChange = viewModel::usernameChanged,
				onLoginLinkClick = {},
				nextButtonEnabled = uiState.nextButtonEnabled,
			)
			RegistrationStage.PIN_CREATION -> RegistrationPinScreen(
				modifier = modifier,
				title = stringResource(R.string.create_pin),
				description = stringResource(R.string.use_pin_to_login),
				currentPinSize = uiState.pin.length,
				onNumberClick = viewModel::pinChanged,
				onBackspaceClick = viewModel::backspacePinTapped,
			)
			RegistrationStage.PIN_CONFIRMATION -> RegistrationPinScreen(
				modifier = modifier,
				title = stringResource(R.string.repeat_your_pin),
				description = stringResource(R.string.enter_your_pin_again),
				currentPinSize = uiState.pinConfirmation.length,
				onNumberClick = viewModel::pinConfirmationChanged,
				onBackspaceClick = viewModel::backspaceConfirmationPinTapped,
			)
			RegistrationStage.PREFERENCES -> PreferencesStageScreen()
		}
		ErrorDialog(errorMessage = uiState.errorMessage)
	}
}

@Composable
fun PreferencesStageScreen() {
	Column {
		Text(text = "Preferences")
	}
}
