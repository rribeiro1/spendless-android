package io.rafaelribeiro.spendless.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.PIN_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MIN_SIZE
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
) : ViewModel() {

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
                    delay(111) // in order to able to see last pin digit
                    // TODO: Login Checking from DataStore if PIN prompt is correct!

                    //TOdo: Then pop back stack and navigate where it came from
                    // popBackStack?

                    //TODO: Once PIN was inserted incorrectly 3 (editable in settings) times, the screen
                    // should enter a locked state:
                    // - The digit buttons are disabled
                    // - Greeting text changes to: “Too many failed attempts”
                    // - Show text that the user can try again and show the current countdown
                    // of when they can retry (duration is editable in settings)
                }
            }
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

}


sealed interface LoginUiEvent {
    data class UsernameChanged(val username: String) : LoginUiEvent
    data class PinChanged(val pin: String) : LoginUiEvent
    data object ActionButtonLoginClicked : LoginUiEvent
    data class PinDigitTapped(val digit: String) : LoginUiEvent
    data object PinBackspaceTapped : LoginUiEvent
}
