package io.rafaelribeiro.spendless.presentation.screens.transactions.export

import io.rafaelribeiro.spendless.domain.transaction.TransactionExportFormat
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportRange

data class ExportTransactionUiState(
    val exportRange: TransactionExportRange = TransactionExportRange.LAST_THREE_MONTHS,
    val exportFormat: TransactionExportFormat = TransactionExportFormat.CSV
)