package io.rafaelribeiro.spendless.presentation.screens.transactions.create

import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty
import io.rafaelribeiro.spendless.data.repository.UserPreferences
import io.rafaelribeiro.spendless.domain.preferences.CurrencySymbol
import io.rafaelribeiro.spendless.domain.preferences.DecimalSeparator
import io.rafaelribeiro.spendless.domain.preferences.ExpenseFormat
import io.rafaelribeiro.spendless.domain.preferences.ThousandSeparator
import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.domain.transaction.TransactionType

data class CreateTransactionUiState(
    val transaction: TransactionUiState = TransactionUiState(),
    val preferences: TransactionPreferencesUiState = TransactionPreferencesUiState(),
)

data class TransactionUiState(
    val transactionType: TransactionType = TransactionType.EXPENSE,
    val category: TransactionCategory = TransactionCategory.HOME,
    val amountDisplay: String = "",
    val description: String = "",
    val note: String? = null,
    val createdButtonEnabled: Boolean = false,
    val errorMessage: UiText = Empty,
)

data class TransactionPreferencesUiState(
    val expensesFormat: ExpenseFormat = ExpenseFormat.NEGATIVE,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.COMMA,
    val currencySymbol: CurrencySymbol = CurrencySymbol.DOLLAR,
) {
    fun fromUserPreferences(userPreferences: UserPreferences): TransactionPreferencesUiState {
        return copy(
            currencySymbol = CurrencySymbol.fromName(userPreferences.currencyName),
            expensesFormat = ExpenseFormat.fromName(userPreferences.expensesFormatName),
            decimalSeparator = DecimalSeparator.fromName(userPreferences.decimalSeparatorName),
            thousandSeparator = ThousandSeparator.fromName(userPreferences.thousandsSeparatorName)
        )
    }
}
