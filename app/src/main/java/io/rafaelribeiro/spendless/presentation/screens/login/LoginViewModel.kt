package io.rafaelribeiro.spendless.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.data.UserPreferencesRepository
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.PIN_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MIN_SIZE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    init {
        // Collect user preferences and update uiState
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesFlow.collect { userPreferences ->
                _uiState.update { it.copy(username = userPreferences.userName) }
            }
        }
    }

    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.UsernameChanged -> usernameChanged(event.username)
            is LoginUiEvent.PinChanged -> pinChanged(event.pin)
            is LoginUiEvent.ActionButtonLoginClicked -> loginClicked()
        }
    }

    private fun usernameChanged(username: String) {
        val trimmedUsername = username.take(USERNAME_MAX_SIZE)
        _uiState.update {
            it.copy(username = trimmedUsername)
        }
        setLoginButtonEnabled()
    }

    private fun pinChanged(digit: String) {
        val trimmedPin = digit.take(PIN_MAX_SIZE)
        _uiState.update { it.copy(pin = trimmedPin) }
        setLoginButtonEnabled()
    }

    private fun setLoginButtonEnabled() {
        _uiState.update {
            it.copy(loginButtonEnabled = it.pin.length == PIN_MAX_SIZE && it.username.length >= USERNAME_MIN_SIZE)
        }
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
    data object ActionButtonLoginClicked: LoginUiEvent
}
