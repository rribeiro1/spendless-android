package io.rafaelribeiro.spendless.presentation.screens.transactions

sealed interface TransactionsUiEvent {
    data class TransactionNoteClicked(val transactionId: Long) : TransactionsUiEvent
    data object AddTransactionClicked : TransactionsUiEvent
    data object DownloadTransactionsClicked : TransactionsUiEvent
}