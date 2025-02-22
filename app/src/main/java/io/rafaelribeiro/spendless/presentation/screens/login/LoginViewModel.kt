package io.rafaelribeiro.spendless.presentation.screens.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.PIN_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MIN_SIZE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            is LoginUiEvent.PinChanged -> pinChanged(event.pin)
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

}


sealed interface LoginUiEvent {
    data class UsernameChanged(val username: String) : LoginUiEvent
    data class PinChanged(val pin: String) : LoginUiEvent
}
