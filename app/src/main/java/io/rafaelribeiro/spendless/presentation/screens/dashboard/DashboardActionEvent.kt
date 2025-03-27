package io.rafaelribeiro.spendless.presentation.screens.dashboard

sealed interface DashboardActionEvent {
    data object ShowAllTransactions : DashboardActionEvent
    data object AddTransaction : DashboardActionEvent
    data object OnSettingsClicked : DashboardActionEvent
    data object ExportTransactions : DashboardActionEvent
}