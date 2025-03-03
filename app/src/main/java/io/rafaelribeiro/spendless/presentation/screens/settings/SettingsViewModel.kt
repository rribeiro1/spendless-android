package io.rafaelribeiro.spendless.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.data.repository.DefaultExpenseFormatterService
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.TransactionRepository
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnBackClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnLogoutClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnPreferencesClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnSecurityClicked
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository,
    private val expenseFormatterService: DefaultExpenseFormatterService,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SettingsUiState> = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState
        .onStart { loadSettings() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    private val _actionEvents = Channel<SettingsActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()


    private fun loadSettings() {
        // TODO: load settings
    }

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.PreferencesClicked -> sendActionEvent(OnPreferencesClicked)
            is SettingsUiEvent.SecurityClicked -> sendActionEvent(OnSecurityClicked)
            is SettingsUiEvent.LogoutClicked -> sendActionEvent(OnLogoutClicked)
            is SettingsUiEvent.BackClicked -> sendActionEvent(OnBackClicked)
        }

    }

    private fun sendActionEvent(event: SettingsActionEvent) {
        viewModelScope.launch {
            _actionEvents.send(event)
        }
    }
}

sealed interface SettingsActionEvent {
    data object OnPreferencesClicked: SettingsActionEvent
    data object OnSecurityClicked: SettingsActionEvent
    data object OnLogoutClicked: SettingsActionEvent
    data object OnBackClicked: SettingsActionEvent
}

sealed interface SettingsUiEvent {
    data object PreferencesClicked: SettingsUiEvent
    data object SecurityClicked: SettingsUiEvent
    data object LogoutClicked: SettingsUiEvent
    data object BackClicked: SettingsUiEvent
}

