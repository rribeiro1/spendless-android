package io.rafaelribeiro.spendless.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.PinVerifier
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.ERROR_MESSAGE_DURATION
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.PIN_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MIN_SIZE
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val pinVerifier: PinVerifier,
) : ViewModel() {

    companion object {
        const val PIN_MAX_WRONG_COUNT = 2
        const val PIN_LOCK_DURATION_SECONDS = 30
        const val PIN_LOCK_TIMER_INTERVAL = 1000L
        const val CLEAR_PIN_DIGIT_DELAY = 111L
    }

    private var pinLockTimerJob: Job? = null

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UsernameChanged -> usernameChanged(event.username)
            is LoginUiEvent.PinChanged -> passwordPinChanged(event.pin)
            is LoginUiEvent.ActionButtonLoginClicked -> loginClicked()
            is LoginUiEvent.PinDigitTapped -> pinChanged(event.digit)
            is LoginUiEvent.PinBackspaceTapped -> backspacePinTapped()
        }
    }

    private fun updateUiState(callback: (LoginUiState) -> LoginUiState) {
        _uiState.update { callback(it) }
    }

    private fun usernameChanged(username: String) {
        val trimmedUsername = username.take(USERNAME_MAX_SIZE)
        updateUiState { it.copy(username = trimmedUsername) }
        setLoginButtonEnabled()
    }

    private fun passwordPinChanged(digit: String) {
        val trimmedPin = digit.take(PIN_MAX_SIZE)
        updateUiState { it.copy(pin = trimmedPin) }
        setLoginButtonEnabled()
    }

    private fun pinChanged(digit: String) {
        val currentPin = _uiState.value.pin + digit
        if (currentPin.length <= PIN_MAX_SIZE) {
            updateUiState { it.copy(pin = currentPin) }
            if (currentPin.length == PIN_MAX_SIZE) {
                viewModelScope.launch {
                    resetPinValues(true)
                    if (pinVerifier.isPinCorrect(currentPin)) {
                        println("Correct PIN entered!")
                        //TOdo: Then pop back stack and navigate where it came from
                        // triggering popBackStack is enough probably?
                    } else {
                        handleWrongPinUiState()
                    }
                }
            }
        }
    }

    private fun handleWrongPinUiState() {
        showErrorMessage(UiText.StringResource(R.string.wrong_pin))
        if (_uiState.value.wrongPinCount < PIN_MAX_WRONG_COUNT) {
            updateUiState { it.copy(wrongPinCount = it.wrongPinCount + 1) }
        } else {
            updateUiState { it.copy(pinPadEnabled = false) } // Disable PinPad
            startPinLockTimer() // try your PIN again in 00:30
        }
    }

    private fun startPinLockTimer() {
        pinLockTimerJob?.cancel()
        pinLockTimerJob = viewModelScope.launch {
            for (seconds in PIN_LOCK_DURATION_SECONDS downTo 0) {
                updateUiState { it.copy(pinLockRemainingSeconds = seconds) } // Ticking
                if (seconds == 0) break
                delay(PIN_LOCK_TIMER_INTERVAL) // Wait for 1 second
            }
            updateUiState { it.copy(pinPadEnabled = true, wrongPinCount = 0) } // Re-enable PinPad
        }
    }

    private suspend fun resetPinValues(withDelay: Boolean = false) {
        delay(if (withDelay) CLEAR_PIN_DIGIT_DELAY else 0)
        updateUiState { it.copy(pin = "") }
    }

    private fun showErrorMessage(text: UiText) {
        _uiState.update { it.copy(errorMessage = text) }
        viewModelScope.launch {
            delay(ERROR_MESSAGE_DURATION)
            _uiState.update { it.copy(errorMessage = UiText.Empty) }
        }
    }

    private fun backspacePinTapped() {
        updateUiState { it.copy(pin = it.pin.dropLast(n = 1)) }
    }

    private fun setLoginButtonEnabled() {
        updateUiState { it.copy(loginButtonEnabled = it.pin.length == PIN_MAX_SIZE && it.username.length >= USERNAME_MIN_SIZE) }
    }

    private fun loginClicked() {
        val username = _uiState.value.username
        val pin = _uiState.value.pin
        // TODO: Login
        println("Username: $username, Pin: $pin")
    }

    override fun onCleared() {
        super.onCleared()
        pinLockTimerJob?.cancel()
    }

}


sealed interface LoginUiEvent {
    data class UsernameChanged(val username: String) : LoginUiEvent
    data class PinChanged(val pin: String) : LoginUiEvent
    data object ActionButtonLoginClicked : LoginUiEvent
    data class PinDigitTapped(val digit: String) : LoginUiEvent
    data object PinBackspaceTapped : LoginUiEvent
}
