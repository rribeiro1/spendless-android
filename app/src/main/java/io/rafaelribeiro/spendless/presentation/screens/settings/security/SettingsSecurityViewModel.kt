package io.rafaelribeiro.spendless.presentation.screens.settings.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.data.repository.SecurityPreferences
import io.rafaelribeiro.spendless.domain.LockoutDuration
import io.rafaelribeiro.spendless.domain.SessionExpiryDuration
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsSecurityUiEvent
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsUiState
import io.rafaelribeiro.spendless.presentation.screens.settings.security.SettingsSecurityActionEvent.*
import kotlinx.coroutines.channels.Channel
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
class SettingsSecurityViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    companion object{
        const val WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS = 5000L
    }

    private val _uiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState
        .onStart { loadSettings() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = SettingsUiState()
        )

    private val _actionEvents = Channel<SettingsSecurityActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    private fun loadSettings() {
        viewModelScope.launch {
            val securityPreferences = userPreferencesRepository.securityPreferences.first()
            val sessionExpiryDuration = securityPreferences.sessionExpiryDuration
            val lockedOutDuration = securityPreferences.lockedOutDuration
            updateState {
                it.copy(
                    sessionExpiryDuration = SessionExpiryDuration.fromValue(sessionExpiryDuration),
                    lockoutDuration = LockoutDuration.fromValue(lockedOutDuration)
                )
            }
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
                saveSecurityPreferences()
                sendActionEvent(OnBackClicked)
            }
        }
    }

    private fun saveSecurityPreferences() {
        viewModelScope.launch {
            userPreferencesRepository.saveSecurityPreferences(
                SecurityPreferences(
                    sessionExpiryDuration = _uiState.value.sessionExpiryDuration.value,
                    lockedOutDuration = _uiState.value.lockoutDuration.value
                )
            )
        }
    }

    private fun sendActionEvent(event: SettingsSecurityActionEvent) {
        viewModelScope.launch {
            _actionEvents.send(event)
        }
    }

    private fun updateState(state: (SettingsUiState) -> SettingsUiState) {
        _uiState.update { state(it) }
    }

    override fun onCleared() {
        super.onCleared()
        _uiState.update { SettingsUiState() }
    }
}

sealed interface SettingsSecurityActionEvent {
    data object OnBackClicked : SettingsSecurityActionEvent
}
