package io.rafaelribeiro.spendless

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.domain.UserSessionRepository
import io.rafaelribeiro.spendless.domain.user.UserPreferencesRepository
import io.rafaelribeiro.spendless.domain.user.UserSessionState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSessionRepository: UserSessionRepository,
    val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    companion object {
        const val WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS = 5_000L
    }

    private var idleTimerJob: Job? = null

    private val _actionEvents = Channel<MainActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    val sessionState: StateFlow<UserSessionState> = userSessionRepository
        .sessionState
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = UserSessionState.Idle,
        )

    private val sessionExpiryDuration: StateFlow<Int> = userPreferencesRepository
        .securityPreferences
        .map { it.sessionExpiryDuration }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = 5,
        )

   init {
       viewModelScope.launch {
           combine(
               sessionState,
               sessionExpiryDuration
           ) { session, sessionExpiryDuration ->
               session to sessionExpiryDuration
           }.collect { (session, sessionExpiryDuration) ->
               when (session) {
                   UserSessionState.Inactive -> userSessionRepository.cancelWorker()
                   UserSessionState.Active -> userSessionRepository.startSession(sessionExpiryDuration.toLong())
                   UserSessionState.Idle -> startIdleTimer()
                   else -> {}
               }
           }
       }
       viewModelScope.launch {
           sessionState.filterNot { it == UserSessionState.Idle }.onEach {
               stopIdleTimer()
           }.collect()
       }
   }

    fun startSession() {
        viewModelScope.launch {
            userSessionRepository.updateSessionState(UserSessionState.Active)
        }
    }

    fun terminateSession() {
        viewModelScope.launch {
            userSessionRepository.updateSessionState(UserSessionState.Inactive)
        }
    }

    private fun startIdleTimer() {
        idleTimerJob?.cancel()
        idleTimerJob = viewModelScope.launch {
            delay(1000L)
            // The session is Idle for more than a second
            userSessionRepository.updateSessionState(UserSessionState.NotRegistered)
        }
    }

    private fun stopIdleTimer() {
        idleTimerJob?.cancel()
    }
}

sealed interface MainActionEvent {
    data object SessionExpired : MainActionEvent
}
