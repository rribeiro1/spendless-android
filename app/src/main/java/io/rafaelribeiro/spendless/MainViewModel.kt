package io.rafaelribeiro.spendless

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.UserSessionState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _actionEvents = Channel<MainActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    val sessionState: StateFlow<UserSessionState> = authRepository.sessionState
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5_000),
            initialValue = UserSessionState.Idle,
        )

    private fun sendActionEvent(actionEvent: MainActionEvent) {
        viewModelScope.launch { _actionEvents.send(actionEvent) }
    }

    fun startSession() {
        viewModelScope.launch {
            authRepository.updateSessionState(UserSessionState.Active)
            sendActionEvent(MainActionEvent.StartUserSession)
        }
    }

    fun terminateSession() {
        viewModelScope.launch {
            authRepository.updateSessionState(UserSessionState.Inactive)
            sendActionEvent(MainActionEvent.CancelUserSession)
        }
    }

    fun expireSession() {
        viewModelScope.launch {
            authRepository.updateSessionState(UserSessionState.Expired)
            println("Session timed out!")
            sendActionEvent(MainActionEvent.SessionExpired)
        }
    }
}

sealed interface MainActionEvent {
    data object SessionExpired : MainActionEvent
    data object StartUserSession : MainActionEvent
    data object CancelUserSession : MainActionEvent
}
