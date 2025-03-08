package io.rafaelribeiro.spendless.presentation.screens.transactions.create

import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.domain.TransactionType

data class CreateTransactionUiState(
    val transaction: TransactionState = TransactionState(),
    val preferences: TransactionPreferences = TransactionPreferences(),
)

data class TransactionState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val category: TransactionCategory = TransactionCategory.HOME,
    val amountDisplay: String = "",
    val description: String = "",
    val note: String = "",
    val createdButtonEnabled: Boolean = false,
    val errorMessage: UiText = Empty,
)

data class TransactionPreferences(
    val transactionCurrency: CurrencySymbol = CurrencySymbol.DOLLAR,
    val transactionFormat: ExpenseFormat = ExpenseFormat.NEGATIVE,
    val transactionDecimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
)
