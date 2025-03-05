package io.rafaelribeiro.spendless.presentation.screens.settings.preferences

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.data.repository.UserPreferences
import io.rafaelribeiro.spendless.data.repository.toUserPreferences
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ExpenseFormatter
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.SettingsPreferencesUiEvent.ButtonClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.SettingsPreferencesUiEvent.CurrencySelected
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.SettingsPreferencesUiEvent.DecimalSeparatorSelected
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.SettingsPreferencesUiEvent.ExpensesFormatSelected
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.SettingsPreferencesUiEvent.ThousandSeparatorSelected
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.SettingsPreferencesActionEvent.OnBackClicked
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
class SettingsPreferencesViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
) : ViewModel() {
    companion object {
        const val WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS = 5000L
    }

    private val _uiState: MutableStateFlow<SettingsPreferencesUiState> = MutableStateFlow(SettingsPreferencesUiState())
    val uiState: StateFlow<SettingsPreferencesUiState> = _uiState
        .onStart {
            subscribeToUserPreferences()
        }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = SettingsPreferencesUiState()
        )
    private val _actionEvents = Channel<SettingsPreferencesActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    private val userPreferences = userPreferencesRepository.userPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = UserPreferences()
        )

    private fun sendActionEvent(event: SettingsPreferencesActionEvent) {
        viewModelScope.launch {
            _actionEvents.send(event)
        }
    }

    private fun updateState(state: (SettingsPreferencesUiState) -> SettingsPreferencesUiState) {
        _uiState.update { state(it) }
    }

    private fun subscribeToUserPreferences() {
        userPreferences
            .onEach {
                updateUiState(it)
                formatExampleExpense()
            }
            .launchIn(viewModelScope)
    }

    private fun updateUiState(preferences: UserPreferences) {
        updateState {
            it.copy(
                expensesFormat = ExpenseFormat.fromName(preferences.expensesFormatName),
                decimalSeparator = DecimalSeparator.fromName(preferences.decimalSeparatorName),
                thousandSeparator = ThousandSeparator.fromName(preferences.thousandsSeparatorName),
                currencySymbol = CurrencySymbol.fromName(preferences.currencyName),
            )
        }
    }

    private fun saveSecurityPreferencesToDataStore() {
        viewModelScope.launch {
            val userPreferences = _uiState.value.toUserPreferences()
            userPreferencesRepository.saveUserPreferences(userPreferences)
        }
    }

    private fun formatExampleExpense() {
        val amount = -10382.45
        val formatter = ExpenseFormatter(
            decimalSeparator = _uiState.value.decimalSeparator,
            thousandSeparator = _uiState.value.thousandSeparator,
            currencySymbol = _uiState.value.currencySymbol,
            expensesFormat = _uiState.value.expensesFormat
        )
        updateState { it.copy(exampleExpenseFormat = formatter.format(amount)) }
        if (isSameSeparator()) {
            enableSaveButton(enabled = false)
        } else {
            enableSaveButton(enabled = true)
        }
    }

    private fun isSameSeparator() = _uiState.value.thousandSeparator.name == _uiState.value.decimalSeparator.name

    private fun enableSaveButton(enabled: Boolean) {
        updateState { it.copy(buttonEnabled = enabled) }
    }

    fun onEvent(event: SettingsPreferencesUiEvent) {
        when (event) {

            is ExpensesFormatSelected -> {
                updateState { it.copy(expensesFormat = event.expensesFormat) }
                formatExampleExpense()
            }
            is DecimalSeparatorSelected -> {
                updateState { it.copy(decimalSeparator = event.decimalSeparator) }
                formatExampleExpense()
            }
            is ThousandSeparatorSelected -> {
                updateState { it.copy(thousandSeparator = event.thousandSeparator) }
                formatExampleExpense()
            }
            is CurrencySelected -> {
                updateState { it.copy(currencySymbol = event.currency) }
                formatExampleExpense()
            }
            is ButtonClicked -> {
                saveSecurityPreferencesToDataStore()
                sendActionEvent(OnBackClicked)
            }

        }
    }
}

sealed interface SettingsPreferencesActionEvent {
    data object OnBackClicked : SettingsPreferencesActionEvent
}

sealed interface SettingsPreferencesUiEvent {
    data object ButtonClicked : SettingsPreferencesUiEvent
    data class ExpensesFormatSelected(val expensesFormat: ExpenseFormat) : SettingsPreferencesUiEvent
    data class DecimalSeparatorSelected(val decimalSeparator: DecimalSeparator) : SettingsPreferencesUiEvent
    data class ThousandSeparatorSelected(val thousandSeparator: ThousandSeparator) : SettingsPreferencesUiEvent
    data class CurrencySelected(val currency: CurrencySymbol) : SettingsPreferencesUiEvent
}
