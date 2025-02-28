package io.rafaelribeiro.spendless.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableStateFlow<DashboardUiState> = MutableStateFlow(DashboardUiState(
        username = "rafael",
        accountBalance = "R$ 1.000,00",
        previousWeekAmount = "R$ 500,00",
        latestTransactions = TransactionCreator.createTransactions(15),
    ))
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            DashboardUiEvent.AddTransactionClicked -> TODO()
            DashboardUiEvent.DownloadTransactionsClicked -> TODO()
            DashboardUiEvent.SettingsClicked -> TODO()
        }
    }
}

sealed interface DashboardUiEvent {
    data object SettingsClicked : DashboardUiEvent
    data object DownloadTransactionsClicked : DashboardUiEvent
    data object AddTransactionClicked : DashboardUiEvent
}
