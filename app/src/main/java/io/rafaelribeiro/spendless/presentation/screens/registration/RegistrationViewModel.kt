package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.asUiText
import io.rafaelribeiro.spendless.data.repository.toUserPreferences
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ExpenseFormatter
import io.rafaelribeiro.spendless.domain.Result
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
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
    private val userPreferencesRepository: UserPreferencesRepository,
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
        const val LAST_PIN_DIGIT_DELAY = 111L
	}

	fun onEvent(event: RegistrationUiEvent) {
		when (event) {
			is RegistrationUiEvent.UsernameChanged -> usernameChanged(event.username)
			is RegistrationUiEvent.ActionButtonNextClicked -> checkUserName()
			is RegistrationUiEvent.LoginLinkClicked -> sendActionEvent(RegistrationActionEvent.AlreadyHaveAccount)
			is RegistrationUiEvent.PinDigitTapped -> pinChanged(event.digit)
			is RegistrationUiEvent.PinBackspaceTapped -> backspacePinTapped()
			is RegistrationUiEvent.PinConfirmationDigitTapped -> pinConfirmationChanged(event.digit)
			is RegistrationUiEvent.PinConfirmationBackspaceTapped -> backspaceConfirmationPinTapped()
			is RegistrationUiEvent.ResetPinValues -> resetPinValues()
			is PreferencesUiEvent.ExpensesFormatSelected -> {
				updatePreferencesState { it.copy(expensesFormat = event.expensesFormat) }
                formatExampleExpense()
			}
			is PreferencesUiEvent.DecimalSeparatorSelected -> {
				updatePreferencesState { it.copy(decimalSeparator = event.decimalSeparator) }
                formatExampleExpense()
			}
			is PreferencesUiEvent.ThousandSeparatorSelected -> {
				updatePreferencesState { it.copy(thousandSeparator = event.thousandSeparator) }
                formatExampleExpense()
			}
            is PreferencesUiEvent.CurrencySelected -> {
                updatePreferencesState { it.copy(currencySymbol = event.currency) }
                formatExampleExpense()
            }
            is PreferencesUiEvent.ButtonClicked -> {
                registerUser()
                saveUserPreferences()
                sendActionEvent(RegistrationActionEvent.UserPreferencesSaved)
            }
        }
	}

    private fun saveUserPreferences() {
        viewModelScope.launch {
            val userPreferences = _uiState.value.preferences.toUserPreferences()
            userPreferencesRepository.saveUserPreferences(userPreferences)
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            authRepository.register(_uiState.value.username, _uiState.value.pin)
        }
    }

    /**
     * Formats the example expense with the current preferences and updates the UI.
     * The amount does not really matter, as the goal is to show the user how the expense will be formatted.
     */
    private fun formatExampleExpense() {
        val amount = -10382.45
        val formatter = ExpenseFormatter(
            decimalSeparator = _uiState.value.preferences.decimalSeparator,
            thousandSeparator = _uiState.value.preferences.thousandSeparator,
            currencySymbol = _uiState.value.preferences.currencySymbol,
            expensesFormat = _uiState.value.preferences.expensesFormat
        )
        updatePreferencesState { it.copy(exampleExpenseFormat = formatter.format(amount)) }
        if (isSameSeparator()) {
            enableStartTrackingButton(enabled = false)
        } else {
            enableStartTrackingButton(enabled = true)
        }
    }

    private fun isSameSeparator() = _uiState.value.preferences.thousandSeparator.name == _uiState.value.preferences.decimalSeparator.name

    private fun enableStartTrackingButton(enabled: Boolean) {
        updatePreferencesState { it.copy(buttonEnabled = enabled) }
    }

	private fun updateState(state: (RegistrationUiState) -> RegistrationUiState) {
		_uiState.update { state(it) }
	}

    private fun updatePreferencesState(state: (RegistrationPreferencesUiState) -> RegistrationPreferencesUiState) {
        updateState { it.copy(preferences = state(it.preferences)) }
    }

	private fun checkUserName() {
		viewModelScope.launch {
			setNextButtonEnabled(false)
			when (val result = authRepository.checkUserName(_uiState.value.username)) {
				is Result.Success -> {
                    setNextButtonEnabled(true)
					sendActionEvent(RegistrationActionEvent.UsernameCheckSuccess)
				}
				is Result.Failure -> {
                    setNextButtonEnabled(false)
					showErrorMessage(result.error.asUiText())
				}
			}
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
            updateState { it.copy(pin = currentPin) }
            if (currentPin.length == PIN_MAX_SIZE) {
                viewModelScope.launch {
                    delay(LAST_PIN_DIGIT_DELAY) // in order to able to see last pin digit
                    sendActionEvent(RegistrationActionEvent.PinCreated)
                }
            }
        }
    }

    private fun pinConfirmationChanged(digit: String) {
        val currentPin = _uiState.value.pinConfirmation + digit
        if (currentPin.length <= PIN_MAX_SIZE) {
            updateState { it.copy(pinConfirmation = currentPin) }
            if (currentPin.length == PIN_MAX_SIZE) {
                if (_uiState.value.pin == currentPin) {
                    viewModelScope.launch {
                        delay(LAST_PIN_DIGIT_DELAY) // in order to able to see last pin digit
                        sendActionEvent(RegistrationActionEvent.PinConfirmed)
                    }
                } else {
                    resetPinValues()
                    sendActionEvent(RegistrationActionEvent.PinMismatch)
                    showErrorMessage(UiText.StringResource(R.string.registration_pin_mismatch))
                }
            }
        }
    }

	private fun backspacePinTapped() {
        updateState { it.copy(pin = _uiState.value.pin.dropLast(n = 1)) }
	}

	private fun backspaceConfirmationPinTapped() {
        updateState { it.copy(pinConfirmation = _uiState.value.pinConfirmation.dropLast(n = 1)) }
	}

	private fun resetPinValues() {
        updateState { it.copy(pinConfirmation = "", pin = "") }
	}

	private fun showErrorMessage(text: UiText) {
        updateState { it.copy(errorMessage = text) }
		viewModelScope.launch {
			delay(ERROR_MESSAGE_DURATION)
            updateState { it.copy(errorMessage = UiText.Empty) }
		}
	}

	private fun setNextButtonEnabled(enabled: Boolean) {
        updateState { it.copy(nextButtonEnabled = enabled) }
	}

	private fun updateUsername(username: String) {
        updateState { it.copy(username = username) }
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
	data object AlreadyHaveAccount : RegistrationActionEvent
    data object UserPreferencesSaved : RegistrationActionEvent
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

sealed interface PreferencesUiEvent: RegistrationUiEvent {
    data object ButtonClicked : PreferencesUiEvent
    data class ExpensesFormatSelected(val expensesFormat: ExpenseFormat) : PreferencesUiEvent
    data class DecimalSeparatorSelected(val decimalSeparator: DecimalSeparator) : PreferencesUiEvent
    data class ThousandSeparatorSelected(val thousandSeparator: ThousandSeparator) : PreferencesUiEvent
    data class CurrencySelected(val currency: CurrencySymbol) : PreferencesUiEvent
}
