package io.rafaelribeiro.spendless.presentation.screens.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.data.BiometricPromptManager
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty
import io.rafaelribeiro.spendless.data.repository.DataStoreUserPreferencesRepository
import io.rafaelribeiro.spendless.data.repository.SecurityPreferences
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.PinLockTickerRepository
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.ERROR_MESSAGE_DURATION
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.PIN_MAX_SIZE
import io.rafaelribeiro.spendless.workers.PinLockWorker.Companion.WORKER_NAME
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
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
    val dataStoreUserPreferencesRepository: DataStoreUserPreferencesRepository,
    internal val biometricManager: BiometricPromptManager,
    val pinLockTickerRepository: PinLockTickerRepository,
) : ViewModel() {
    companion object {
        const val PIN_MAX_WRONG_COUNT = 2
        const val CLEAR_PIN_DIGIT_DELAY = 111L
    }

    private val securityPreferences: StateFlow<SecurityPreferences> = dataStoreUserPreferencesRepository.securityPreferences
        .stateIn(
            scope = viewModelScope,
            initialValue = SecurityPreferences(),
            started = SharingStarted.WhileSubscribed(5000L),
        )

    private val pinLockRemainingSeconds: StateFlow<Int?> = dataStoreUserPreferencesRepository.pinLockStatePreferences
        .stateIn(
            scope = viewModelScope,
            initialValue = null,
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

    var biometricEvents = biometricManager.promptResults
        private set

    private fun subscribeToData() {
        getPinPromptData()
            .onEach { _authPinUiState.value = it  }
            .launchIn(viewModelScope)
        checkPinLockTimer()
    }

    private fun checkPinLockTimer() {
        viewModelScope.launch {
            pinLockRemainingSeconds.firstOrNull()?.let { remainingSeconds ->
                setupPinLockTimer(remainingSeconds)
            }
        }
    }

    private fun getPinPromptData(): Flow<AuthPinUiState> {
        return combine(
            securityPreferences,
            pinLockRemainingSeconds,
            authRepository.userName,
        ) { securityPreferences, pinLockRemainingSeconds, username ->
            val isPinPadEnabled = pinLockRemainingSeconds == null || pinLockRemainingSeconds == 0
            AuthPinUiState(
                isLoading = false,
                username = username,
                totalPinLockDuration = securityPreferences.lockedOutDuration,
                biometricsEnabled = securityPreferences.isBiometricEnabled && isPinPadEnabled,
                pinLockRemainingSeconds = pinLockRemainingSeconds ?: 0,
                pinPadEnabled = isPinPadEnabled,
            )
        }
    }

    fun onEvent(event: AuthPinUiEvent) {
        when (event) {
            is AuthPinUiEvent.PinDigitTapped -> pinChanged(event.digit)
            is AuthPinUiEvent.PinBackspaceTapped -> backspacePinTapped()
            is AuthPinUiEvent.BiometricsTapped -> biometricsTapped()
            is AuthPinUiEvent.CorrectBiometricsEntered -> correctPinEntered()
            is AuthPinUiEvent.LogoutTapped -> logoutTapped()
        }
    }

    private fun logoutTapped() {
        viewModelScope.launch {
            _actionEvents.send(AuthPinActionEvent.LogoutClicked)
        }
    }

    private fun correctPinEntered() {
        viewModelScope.launch {
            _actionEvents.send(AuthPinActionEvent.CorrectPinEntered)
        }
    }

    private fun biometricsTapped() {
        viewModelScope.launch {
            _actionEvents.send(AuthPinActionEvent.BiometricsTriggered)
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
                        correctPinEntered()
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
            viewModelScope.launch {
                val totalPinLockDuration = _authPinUiState.value.totalPinLockDuration
                dataStoreUserPreferencesRepository.savePinLockState(totalPinLockDuration)
                setupPinLockTimer(totalPinLockDuration)
            }
        }
    }

    private fun setupPinLockTimer(remainingSeconds: Int) {
        Log.d(WORKER_NAME, "remainingSeconds: $remainingSeconds")
        if (remainingSeconds > 0)
            pinLockTickerRepository.startSession(remainingSeconds)
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
    val biometricsEnabled: Boolean = false,
)

sealed interface AuthPinUiEvent {
    data class PinDigitTapped(val digit: String) : AuthPinUiEvent
    data object PinBackspaceTapped : AuthPinUiEvent
    data object BiometricsTapped : AuthPinUiEvent
    data object CorrectBiometricsEntered : AuthPinUiEvent
    data object LogoutTapped : AuthPinUiEvent
}

sealed interface AuthPinActionEvent {
    data object CorrectPinEntered : AuthPinActionEvent
    data object BiometricsTriggered : AuthPinActionEvent
    data object LogoutClicked : AuthPinActionEvent
}
