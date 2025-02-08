package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.asUiText
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel @Inject constructor(
	private val authRepository: AuthRepository,
) : ViewModel() {
	private val _uiState: MutableStateFlow<RegistrationUiState> = MutableStateFlow(RegistrationUiState())
	val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

	private val _actionEvent = Channel<RegistrationActionEvent>()
	val actionEvent = _actionEvent.receiveAsFlow()

	companion object {
		const val ERROR_MESSAGE_DURATION = 2000L
		const val USERNAME_MAX_SIZE = 14
		const val USERNAME_MIN_SIZE = 3
		const val PIN_MAX_SIZE = 5
	}

	fun checkUserName(username: String) {
		viewModelScope.launch {
			setNextButtonEnabled(false)
			when (val result = authRepository.checkUserName(username)) {
				is Result.Success -> {
					sendActionEvent(RegistrationActionEvent.UsernameCheckSuccess)
					updateStage(RegistrationStage.PIN_CREATION)
				}
				is Result.Failure -> {
					showErrorMessage(result.error.asUiText())
				}
			}
		}
	}

	fun usernameChanged(username: String) {
		val trimmedUsername = username.take(USERNAME_MAX_SIZE)
		updateUsername(trimmedUsername)
		setNextButtonEnabled(trimmedUsername.length >= USERNAME_MIN_SIZE)
	}

	fun pinChanged(digit: String) {
		if ((_uiState.value.pin + digit).length == PIN_MAX_SIZE) {
			updateStage(RegistrationStage.PIN_CONFIRMATION)
			sendActionEvent(RegistrationActionEvent.PinCreated)
		} else {
			_uiState.update { it.copy(pin = _uiState.value.pin + digit) }
		}
	}

	fun pinConfirmationChanged(digit: String) {
		if ((_uiState.value.pinConfirmation + digit).length == PIN_MAX_SIZE) {
			if (_uiState.value.pin == _uiState.value.pinConfirmation) {
				sendActionEvent(RegistrationActionEvent.PinConfirmed)
			} else {
				_uiState.update { it.copy(pin = "", pinConfirmation = "") }
				sendActionEvent(RegistrationActionEvent.PinMismatch)
				showErrorMessage(UiText.StringResource(R.string.pin_mismatch))
			}
		} else {
			_uiState.update { it.copy(pinConfirmation = _uiState.value.pinConfirmation + digit) }
		}
	}

	fun backspacePinTapped() {
		_uiState.update { it.copy(pin = _uiState.value.pin.dropLast(n = 1)) }
	}

	fun backspaceConfirmationPinTapped() {
		_uiState.update { it.copy(pinConfirmation = _uiState.value.pinConfirmation.dropLast(n = 1)) }
	}

	private fun showErrorMessage(text: UiText) {
		_uiState.update { it.copy(errorMessage = text) }
		viewModelScope.launch {
			delay(ERROR_MESSAGE_DURATION)
			_uiState.update { it.copy(errorMessage = UiText.Empty) }
		}
	}

	private fun setNextButtonEnabled(enabled: Boolean) {
		_uiState.update { it.copy(nextButtonEnabled = enabled) }
	}

	private fun updateUsername(username: String) {
		_uiState.update { it.copy(username = username) }
	}

	private fun updateStage(stage: RegistrationStage) {
		_uiState.update { it.copy(registrationStage = stage) }
	}

	private fun sendActionEvent(actionEvent: RegistrationActionEvent) {
		viewModelScope.launch { _actionEvent.send(actionEvent) }
	}
}

sealed interface RegistrationActionEvent {
	data object UsernameCheckSuccess : RegistrationActionEvent
	data object PinCreated : RegistrationActionEvent
	data object PinConfirmed : RegistrationActionEvent
	data object PinMismatch : RegistrationActionEvent
}
