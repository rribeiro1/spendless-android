package io.rafaelribeiro.spendless.presentation.screens.dashboard

sealed interface DashboardActionEvent {
    data object OnSettingsClicked : DashboardActionEvent
    data object ExportTransactions : DashboardActionEvent
    data object AddTransaction : DashboardActionEvent
    data object ShowAllTransactions : DashboardActionEvent
}