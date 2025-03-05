package io.rafaelribeiro.spendless.presentation.screens.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty
import io.rafaelribeiro.spendless.data.repository.DataStoreUserPreferencesRepository
import io.rafaelribeiro.spendless.data.repository.SecurityPreferences
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.ERROR_MESSAGE_DURATION
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.PIN_MAX_SIZE
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthPinPromptViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    dataStoreUserPreferencesRepository: DataStoreUserPreferencesRepository,
) : ViewModel() {
    companion object {
        const val PIN_MAX_WRONG_COUNT = 2
        const val PIN_LOCK_TIMER_INTERVAL = 1000L
        const val CLEAR_PIN_DIGIT_DELAY = 111L
    }
    private var pinLockTimerJob: Job? = null

    private val securityPreferences: StateFlow<SecurityPreferences> = dataStoreUserPreferencesRepository.securityPreferences
        .stateIn(
            scope = viewModelScope,
            initialValue = SecurityPreferences(),
            started = SharingStarted.WhileSubscribed(5000L),
        )

    private val initialUiState = AuthPinUiState(isLoading = true)
    private val _authPinUiState: MutableStateFlow<AuthPinUiState> = MutableStateFlow(initialUiState)
    val authPinUiState: StateFlow<AuthPinUiState> = _authPinUiState
        .onStart { subscribeToData() }
        .stateIn(
            scope = viewModelScope,
            initialValue = initialUiState,
            started = SharingStarted.WhileSubscribed(5000L),
        )

    private val _actionEvents = Channel<AuthPinActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    private fun subscribeToData() {
        getPinPromptData()
            .onEach { _authPinUiState.value = it  }
            .launchIn(viewModelScope)
    }

    private fun getPinPromptData(): Flow<AuthPinUiState> {
        return combine(
            securityPreferences,
            authRepository.userName,
        ) { securityPreferences, username ->
            AuthPinUiState(
                isLoading = false,
                username = username,
                totalPinLockDuration = securityPreferences.lockedOutDuration,
            )
        }
    }

    fun onEvent(event: AuthPinUiEvent) {
        when (event) {
            is AuthPinUiEvent.PinDigitTapped -> pinChanged(event.digit)
            is AuthPinUiEvent.PinBackspaceTapped -> backspacePinTapped()
        }
    }

    private fun backspacePinTapped() {
        updateUiState { it.copy(pin = _authPinUiState.value.pin.dropLast(n = 1)) }
    }

    private fun updateUiState(callback: (AuthPinUiState) -> AuthPinUiState) {
        _authPinUiState.update { callback(it) }
    }

    private fun pinChanged(digit: String) {
        val currentPin = _authPinUiState.value.pin + digit
        if (currentPin.length <= PIN_MAX_SIZE) {
            updateUiState { it.copy(pin = currentPin) }
            if (currentPin.length == PIN_MAX_SIZE) {
                viewModelScope.launch {
                    resetPinValues(true)
                    if (authRepository.isPinCorrect(currentPin)) {
                        _actionEvents.send(AuthPinActionEvent.CorrectPinEntered)
                    } else {
                        handleWrongPinUiState()
                    }
                }
            }
        }
    }

    private fun handleWrongPinUiState() {
        showErrorMessage(UiText.StringResource(R.string.wrong_pin))
        if (_authPinUiState.value.wrongPinCount < PIN_MAX_WRONG_COUNT) {
            updateUiState { it.copy(wrongPinCount = it.wrongPinCount + 1) }
        } else {
            updateUiState { it.copy(pinPadEnabled = false) } // Disable PinPad
            startPinLockTimer() // try your PIN again in 00:30
        }
    }

    private fun startPinLockTimer() {
        pinLockTimerJob?.cancel()
        pinLockTimerJob = viewModelScope.launch {
            val lockDurationSeconds = _authPinUiState.value.totalPinLockDuration
            for (seconds in lockDurationSeconds downTo 0) {
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
        updateUiState { it.copy(errorMessage = text) }
        viewModelScope.launch {
            delay(ERROR_MESSAGE_DURATION)
            updateUiState { it.copy(errorMessage = Empty) }
        }
    }
}

data class AuthPinUiState(
    val isLoading: Boolean = false,
    val errorMessage: UiText = Empty,
    val username: String = "",
    val pin: String = "",
    val wrongPinCount: Int = 0,
    val pinPadEnabled: Boolean = true,
    val pinLockRemainingSeconds: Int = 0,
    val totalPinLockDuration: Int = 0,
)

sealed interface AuthPinUiEvent {
    data class PinDigitTapped(val digit: String) : AuthPinUiEvent
    data object PinBackspaceTapped : AuthPinUiEvent
}

sealed interface AuthPinActionEvent {
    data object CorrectPinEntered : AuthPinActionEvent
}
