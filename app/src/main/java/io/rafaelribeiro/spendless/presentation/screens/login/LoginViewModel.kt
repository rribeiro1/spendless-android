package io.rafaelribeiro.spendless.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.ERROR_MESSAGE_DURATION
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.PIN_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MIN_SIZE
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val initialUiState = LoginUiState(isLoading = true)
    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(initialUiState)
    val uiState: StateFlow<LoginUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            initialValue = initialUiState,
            started = SharingStarted.WhileSubscribed(5000L),
        )

    private val _actionEvents = Channel<LoginActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UsernameChanged -> usernameChanged(event.username)
            is LoginUiEvent.PinChanged -> passwordPinChanged(event.pin)
            is LoginUiEvent.ActionButtonLoginClicked -> loginClicked()
            is LoginUiEvent.UsernameFocusChanged -> updateUiState { it.copy(isUsernameFocused = event.isFocused) }
            is LoginUiEvent.PinFocusChanged -> updateUiState { it.copy(isPinFocused = event.isFocused) }
        }
    }

    private fun updateUiState(callback: (LoginUiState) -> LoginUiState) {
        _uiState.update { callback(it) }
    }

    private fun usernameChanged(username: String) {
        val newUsername = username.take(USERNAME_MAX_SIZE)
        updateUiState { it.copy(username = newUsername) }
        setLoginButtonEnabled()
    }

    private fun passwordPinChanged(digit: String) {
        val trimmedPin = digit.take(PIN_MAX_SIZE)
        updateUiState { it.copy(pin = trimmedPin) }
        setLoginButtonEnabled()
    }

    private fun showErrorMessage(text: UiText) {
        updateUiState { it.copy(errorMessage = text) }
        viewModelScope.launch {
            delay(ERROR_MESSAGE_DURATION)
            updateUiState { it.copy(errorMessage = UiText.Empty) }
        }
    }

    private fun setLoginButtonEnabled() {
        updateUiState { it.copy(loginButtonEnabled = it.pin.length == PIN_MAX_SIZE && it.username.length >= USERNAME_MIN_SIZE) }
    }

    private fun loginClicked() {
        val username = _uiState.value.username
        val pin = _uiState.value.pin
        viewModelScope.launch {
            if (authRepository.authenticateCredentials(pin, username)) {
                sendActionEvent(LoginActionEvent.LoginSucceed(username))
            } else {
                showErrorMessage(UiText.StringResource(R.string.invalid_credentials))
            }
        }
    }

    private fun sendActionEvent(actionEvent: LoginActionEvent) {
        viewModelScope.launch { _actionEvents.send(actionEvent) }
    }
}
