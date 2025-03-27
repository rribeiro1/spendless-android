package io.rafaelribeiro.spendless.presentation.screens.dashboard

sealed interface DashboardUiEvent {
    data object SettingsClicked : DashboardUiEvent
    data object DownloadTransactionsClicked : DashboardUiEvent
    data object AddTransactionClicked : DashboardUiEvent
    data object ShowAllTransactionsClicked : DashboardUiEvent
    data class TransactionNoteClicked(val transactionId: Long) : DashboardUiEvent
}