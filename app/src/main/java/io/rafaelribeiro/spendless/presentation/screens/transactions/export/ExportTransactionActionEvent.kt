package io.rafaelribeiro.spendless.presentation.screens.transactions.export

sealed interface ExportTransactionActionEvent {
    data object CancelExportCreation : ExportTransactionActionEvent
    data object TransactionExportSuccess : ExportTransactionActionEvent
    data object TransactionExportFailed: ExportTransactionActionEvent
}