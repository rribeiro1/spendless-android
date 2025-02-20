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

	private val _actionEvents = Channel<RegistrationActionEvent>()
	val actionEvents = _actionEvents.receiveAsFlow()

	companion object {
		const val ERROR_MESSAGE_DURATION = 2000L
		const val USERNAME_MAX_SIZE = 14
		const val USERNAME_MIN_SIZE = 3
		const val PIN_MAX_SIZE = 5
	}

	fun onEvent(event: RegistrationUiEvent) {
		when (event) {
			is RegistrationUiEvent.UsernameChanged -> usernameChanged(event.username)
			is RegistrationUiEvent.ActionButtonNextClicked -> checkUserName()
			is RegistrationUiEvent.LoginLinkClicked -> {}
			is RegistrationUiEvent.PinDigitTapped -> pinChanged(event.digit)
			is RegistrationUiEvent.PinBackspaceTapped -> backspacePinTapped()
			is RegistrationUiEvent.PinConfirmationDigitTapped -> pinConfirmationChanged(event.digit)
			is RegistrationUiEvent.PinConfirmationBackspaceTapped -> backspaceConfirmationPinTapped()
			is RegistrationUiEvent.ResetPinValues -> resetPinValues()
		}
	}

	private fun checkUserName() {
		viewModelScope.launch {
			setNextButtonEnabled(false)
			when (val result = authRepository.checkUserName(_uiState.value.username)) {
				is Result.Success -> {
					sendActionEvent(RegistrationActionEvent.UsernameCheckSuccess)
				}
				is Result.Failure -> {
					showErrorMessage(result.error.asUiText())
				}
			}
			setNextButtonEnabled(true)
		}
	}

	private fun usernameChanged(username: String) {
		val trimmedUsername = username.take(USERNAME_MAX_SIZE)
		updateUsername(trimmedUsername)
		setNextButtonEnabled(trimmedUsername.length >= USERNAME_MIN_SIZE)
	}

	private fun pinChanged(digit: String) {
		val currentPin = _uiState.value.pin + digit
		if (currentPin.length <= PIN_MAX_SIZE) {
			_uiState.update { it.copy(pin = currentPin) }
			if (currentPin.length == PIN_MAX_SIZE) {
				sendActionEvent(RegistrationActionEvent.PinCreated)
			}
		}
	}

	private fun pinConfirmationChanged(digit: String) {
		val currentPin = _uiState.value.pinConfirmation + digit
		if (currentPin.length <= PIN_MAX_SIZE) {
			_uiState.update { it.copy(pinConfirmation = currentPin) }
			if (currentPin.length == PIN_MAX_SIZE) {
				if (_uiState.value.pin == currentPin) {
					sendActionEvent(RegistrationActionEvent.PinConfirmed)
				} else {
					resetPinValues()
					sendActionEvent(RegistrationActionEvent.PinMismatch)
					showErrorMessage(UiText.StringResource(R.string.registration_pin_mismatch))
				}
			}
		}
	}

	private fun backspacePinTapped() {
		_uiState.update { it.copy(pin = _uiState.value.pin.dropLast(n = 1)) }
	}

	private fun backspaceConfirmationPinTapped() {
		_uiState.update { it.copy(pinConfirmation = _uiState.value.pinConfirmation.dropLast(n = 1)) }
	}

	private fun resetPinValues() {
		_uiState.update { it.copy(pinConfirmation = "", pin = "") }
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

	private fun sendActionEvent(actionEvent: RegistrationActionEvent) {
		viewModelScope.launch { _actionEvents.send(actionEvent) }
	}
}

sealed interface RegistrationActionEvent {
	data object UsernameCheckSuccess : RegistrationActionEvent
	data object PinCreated : RegistrationActionEvent
	data object PinConfirmed : RegistrationActionEvent
	data object PinMismatch : RegistrationActionEvent
}

sealed interface RegistrationUiEvent {
	data class UsernameChanged(val username: String) : RegistrationUiEvent
	data object ActionButtonNextClicked : RegistrationUiEvent
	data object LoginLinkClicked : RegistrationUiEvent

	data class PinDigitTapped(val digit: String) : RegistrationUiEvent
	data object PinBackspaceTapped : RegistrationUiEvent
	data class PinConfirmationDigitTapped(val digit: String) : RegistrationUiEvent
	data object PinConfirmationBackspaceTapped : RegistrationUiEvent
	data object ResetPinValues : RegistrationUiEvent
}
