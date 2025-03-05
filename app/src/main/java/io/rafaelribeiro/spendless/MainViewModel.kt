package io.rafaelribeiro.spendless

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    // TODO: This ViewModel is just for Test Purposes, we need to replace everything here with WorkManager..

    companion object {
        private const val SESSION_TICKER_KEY = "session_ticker"
        private const val SESSION_TIMER_INTERVAL = 1000L
    }
    private var sessionExpiryJob: Job? = null
    private val _actionEvents = Channel<MainActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()
    init {
        startUserSession(600) // 10 mins
    }
    private fun startUserSession(sessionExpiryDurationInSeconds: Int) {
        sessionExpiryJob?.cancel()
        sessionExpiryJob = viewModelScope.launch(Dispatchers.IO) {
            val totalSeconds = savedStateHandle.get<Int>(SESSION_TICKER_KEY) ?: sessionExpiryDurationInSeconds
            for (seconds in totalSeconds downTo 0) {
              savedStateHandle[SESSION_TICKER_KEY] = seconds
                if (seconds == 0) {
                    _actionEvents.send(MainActionEvent.SessionExpired)
                    break
                }
                delay(SESSION_TIMER_INTERVAL)
                println("seconds: $seconds")
            }
        }
    }

}

sealed interface MainActionEvent {
    data object SessionExpired : MainActionEvent
}
