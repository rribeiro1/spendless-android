package io.rafaelribeiro.spendless.presentation.screens.transactions.create

import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.domain.transaction.TransactionRecurrence
import io.rafaelribeiro.spendless.domain.transaction.TransactionType

sealed interface CreateTransactionUiEvent {
    data object OnCreatedClicked : CreateTransactionUiEvent
    data object OnCancelClicked : CreateTransactionUiEvent
    data class OnTransactionTypeSelected(val transactionType: TransactionType) : CreateTransactionUiEvent
    data class OnCategorySelected(val transactionCategory: TransactionCategory) : CreateTransactionUiEvent
    data class OnNoteChanged(val transactionNote: String) : CreateTransactionUiEvent
    data class OnDescriptionChanged(val transactionDescription: String) : CreateTransactionUiEvent
    data class OnAmountChanged(val amount: String) : CreateTransactionUiEvent
    data class OnRecurrenceSelected(val recurrence: TransactionRecurrence) : CreateTransactionUiEvent
}
