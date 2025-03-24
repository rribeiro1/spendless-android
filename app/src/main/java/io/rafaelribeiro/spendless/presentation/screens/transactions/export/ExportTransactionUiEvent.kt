package io.rafaelribeiro.spendless.presentation.screens.transactions.export

import io.rafaelribeiro.spendless.domain.transaction.TransactionExportFormat
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportRange


sealed interface ExportTransactionUiEvent {
    data class OnRangeSelected(val range: TransactionExportRange): ExportTransactionUiEvent
    data class OnFormatSelected(val format: TransactionExportFormat): ExportTransactionUiEvent
    data object OnCancelClicked: ExportTransactionUiEvent
    data object OnExportClicked: ExportTransactionUiEvent
}