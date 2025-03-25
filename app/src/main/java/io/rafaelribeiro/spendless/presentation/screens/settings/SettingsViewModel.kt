package io.rafaelribeiro.spendless.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.data.TransactionCreator
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.domain.UserSessionRepository
import io.rafaelribeiro.spendless.domain.transaction.TransactionRepository
import io.rafaelribeiro.spendless.domain.user.UserPreferencesRepository
import io.rafaelribeiro.spendless.domain.user.UserSessionState
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel.Companion.ERROR_MESSAGE_DURATION
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnBackClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnLogoutClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnPreferencesClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnSecurityClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnAccountClicked
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val transactionRepository: TransactionRepository,
    private val userSessionRepository: UserSessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiState()
        )

    private val _actionEvents = Channel<SettingsActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.PreferencesClicked -> sendActionEvent(OnPreferencesClicked)
            is SettingsUiEvent.SecurityClicked -> sendActionEvent(OnSecurityClicked)
            is SettingsUiEvent.LogoutClicked -> sendActionEvent(OnLogoutClicked)
            is SettingsUiEvent.AccountClicked -> sendActionEvent(OnAccountClicked)
            is SettingsUiEvent.BackClicked -> sendActionEvent(OnBackClicked)
            is SettingsUiEvent.DeleteAccountClicked -> deleteAccount()
            is SettingsUiEvent.AddFakeTransactionsClicked -> addFakeTransactions()
            is SettingsUiEvent.DeleteFakeTransactionsClicked -> deleteAllTransactions()
        }
    }

    private fun sendActionEvent(event: SettingsActionEvent) {
        viewModelScope.launch {
            _actionEvents.send(event)
        }
    }

    private fun updateUiState(callback: (SettingsUiState) -> SettingsUiState) {
        _uiState.update { callback(it) }
    }

    private fun deleteAccount() {
        viewModelScope.launch {
            transactionRepository.deleteAllTransactions()
            userPreferencesRepository.clearAllPreferences()
            userSessionRepository.updateSessionState(UserSessionState.Idle)
        }
    }

    private fun deleteAllTransactions() {
        viewModelScope.launch {
            transactionRepository.deleteAllTransactions()
            showMessage(UiText.StringResource(R.string.transactions_deleted))
        }
    }

    private fun addFakeTransactions() {
        viewModelScope.launch {
            val transactions = TransactionCreator.createTransactions(quantity = 100)
            transactions.forEach { transactionRepository.saveTransaction(it) }
            showMessage(UiText.StringResource(R.string.fake_transactions_created))
        }
    }

    private fun showMessage(text: UiText, isError: Boolean = false) {
        updateUiState { it.copy(message = text, isError = isError) }
        viewModelScope.launch {
            delay(ERROR_MESSAGE_DURATION)
            updateUiState { it.copy(message = UiText.Empty, isError = false) }
        }
    }
}

sealed interface SettingsActionEvent {
    data object OnPreferencesClicked: SettingsActionEvent
    data object OnSecurityClicked: SettingsActionEvent
    data object OnLogoutClicked: SettingsActionEvent
    data object OnAccountClicked: SettingsActionEvent
    data object OnBackClicked: SettingsActionEvent
}

sealed interface SettingsUiEvent {
    data object PreferencesClicked: SettingsUiEvent
    data object SecurityClicked: SettingsUiEvent
    data object LogoutClicked: SettingsUiEvent
    data object AccountClicked: SettingsUiEvent
    data object BackClicked: SettingsUiEvent

    data object DeleteAccountClicked: SettingsUiEvent
    data object AddFakeTransactionsClicked: SettingsUiEvent
    data object DeleteFakeTransactionsClicked: SettingsUiEvent
}
