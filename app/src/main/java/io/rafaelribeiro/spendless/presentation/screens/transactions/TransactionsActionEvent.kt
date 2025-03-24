package io.rafaelribeiro.spendless.presentation.screens.transactions

sealed interface TransactionsActionEvent {
    data object NavigateToAddTransaction : TransactionsActionEvent
    data object NavigateToDownloadTransaction: TransactionsActionEvent
}