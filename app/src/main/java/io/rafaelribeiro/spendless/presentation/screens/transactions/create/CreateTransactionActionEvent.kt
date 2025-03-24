package io.rafaelribeiro.spendless.presentation.screens.transactions.create

sealed interface CreateTransactionActionEvent {
    data object CancelTransactionCreation : CreateTransactionActionEvent
    data object TransactionCreated : CreateTransactionActionEvent
}
