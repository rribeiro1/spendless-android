package io.rafaelribeiro.spendless.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.data.DataStoreUserPreferencesRepository
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.PIN_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MAX_SIZE
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.USERNAME_MIN_SIZE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreUserPreferencesRepository: DataStoreUserPreferencesRepository,
) : ViewModel() {

    private val initialUiState = LoginUiState(isLoading = true)
    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(initialUiState)
    val uiState: StateFlow<LoginUiState> = _uiState
        .onStart { loadData() }
        .stateIn(
            scope = viewModelScope,
            initialValue = initialUiState,
            started = SharingStarted.WhileSubscribed(5000L),
        )

    private suspend fun loadData() {
        val username = authRepository.userName.first()
        _uiState.update { it.copy(username = username, isLoading = false) }

        // todo: this can be removed later:
        val userPreferences = dataStoreUserPreferencesRepository.userPreferences.first()
        val currencySymbol = CurrencySymbol.entries.first { it.name == userPreferences.currencyName }
        println("userPreferences: $userPreferences, currencySymbol: $currencySymbol")
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
