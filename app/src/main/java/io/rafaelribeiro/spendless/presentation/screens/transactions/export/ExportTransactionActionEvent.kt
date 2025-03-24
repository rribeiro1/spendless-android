package io.rafaelribeiro.spendless.presentation.screens.transactions.export

sealed interface ExportTransactionActionEvent {
    data object CancelExportCreation : ExportTransactionActionEvent
}