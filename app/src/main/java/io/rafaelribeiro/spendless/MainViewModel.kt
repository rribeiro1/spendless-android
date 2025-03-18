package io.rafaelribeiro.spendless

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.data.repository.SecurityPreferences
import io.rafaelribeiro.spendless.domain.UserSessionRepository
import io.rafaelribeiro.spendless.domain.user.UserPreferencesRepository
import io.rafaelribeiro.spendless.domain.user.UserSessionState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSessionRepository: UserSessionRepository,
    userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    companion object {
        const val WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS = 5_000L
    }

    private val _actionEvents = Channel<MainActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    val sessionState: StateFlow<UserSessionState> = userSessionRepository
        .sessionState
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = UserSessionState.Idle,
        )

    val securityPreferences: StateFlow<SecurityPreferences> = userPreferencesRepository
        .securityPreferences
        .stateIn(
            scope = viewModelScope,
            initialValue = SecurityPreferences(),
            started = WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
        )

    init {
        viewModelScope.launch {
            sessionState.collect { state ->
                if (state == UserSessionState.Expired) {
                    sendActionEvent(MainActionEvent.SessionExpired)
                }
            }
        }
    }

    private fun sendActionEvent(actionEvent: MainActionEvent) {
        viewModelScope.launch { _actionEvents.send(actionEvent) }
    }

    fun startSession() {
        viewModelScope.launch {
            userSessionRepository.updateSessionState(UserSessionState.Active)
            sendActionEvent(MainActionEvent.StartUserSession)
        }
    }

    fun terminateSession() {
        viewModelScope.launch {
            userSessionRepository.updateSessionState(UserSessionState.Inactive)
            sendActionEvent(MainActionEvent.CancelUserSession)
        }
    }
}

sealed interface MainActionEvent {
    data object SessionExpired : MainActionEvent
    data object StartUserSession : MainActionEvent
    data object CancelUserSession : MainActionEvent
}
