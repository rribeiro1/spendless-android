package io.rafaelribeiro.spendless.presentation.screens.settings.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.data.repository.SecurityPreferences
import io.rafaelribeiro.spendless.domain.LockoutDuration
import io.rafaelribeiro.spendless.domain.SessionExpiryDuration
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import io.rafaelribeiro.spendless.presentation.screens.settings.security.SettingsSecurityActionEvent.OnBackClicked
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsSecurityViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    companion object{
        const val WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS = 5000L
    }

    private val _uiState: MutableStateFlow<SecurityUiState> = MutableStateFlow(SecurityUiState())
    val uiState: StateFlow<SecurityUiState> = _uiState
        .onStart { subscribeToSecurityPreferences() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = SecurityUiState()
        )

    private val _actionEvents = Channel<SettingsSecurityActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    private val securityPreferences = userPreferencesRepository.securityPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = SecurityPreferences()
        )

    private fun sendActionEvent(event: SettingsSecurityActionEvent) {
        viewModelScope.launch {
            _actionEvents.send(event)
        }
    }

    private fun updateState(state: (SecurityUiState) -> SecurityUiState) {
        _uiState.update { state(it) }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update { SecurityUiState() }
    }


    private fun subscribeToSecurityPreferences() {
        securityPreferences
            .onEach { updateUiState(it) }
            .launchIn(viewModelScope)
    }

    private fun updateUiState(preferences: SecurityPreferences) {
        updateState {
            it.copy(
                sessionExpiryDuration = SessionExpiryDuration.fromValue(preferences.sessionExpiryDuration),
                lockoutDuration = LockoutDuration.fromValue(preferences.lockedOutDuration)
            )
        }
    }

    private fun saveSecurityPreferencesToDataStore() {
        viewModelScope.launch {
            val securityPreferences = SecurityPreferences(
                sessionExpiryDuration = _uiState.value.sessionExpiryDuration.value,
                lockedOutDuration = _uiState.value.lockoutDuration.value
            )
            userPreferencesRepository.saveSecurityPreferences(securityPreferences)
        }
    }

    fun onSecurityEvent(event: SettingsSecurityUiEvent) {
        when (event) {
            is SettingsSecurityUiEvent.SessionExpiryDurationSelected -> {
                updateState { it.copy(sessionExpiryDuration = event.duration) }
            }
            is SettingsSecurityUiEvent.LockedOutDurationSelected -> {
                updateState { it.copy(lockoutDuration = event.duration) }
            }
            is SettingsSecurityUiEvent.SaveClicked -> {
                saveSecurityPreferencesToDataStore()
                sendActionEvent(OnBackClicked)
            }
        }
    }
}

sealed interface SettingsSecurityActionEvent {
    data object OnBackClicked : SettingsSecurityActionEvent
}


sealed interface SettingsSecurityUiEvent {
    data object SaveClicked : SettingsSecurityUiEvent
    data class SessionExpiryDurationSelected(val duration: SessionExpiryDuration) : SettingsSecurityUiEvent
    data class LockedOutDurationSelected(val duration: LockoutDuration) : SettingsSecurityUiEvent
}
