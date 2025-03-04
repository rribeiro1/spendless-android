package io.rafaelribeiro.spendless.presentation.screens.settings.preferences

import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ThousandSeparator

data class PreferencesUiState(
    val exampleExpenseFormat: String,
    val expensesFormat: ExpenseFormat,
    val decimalSeparator: DecimalSeparator,
    val thousandSeparator: ThousandSeparator,
    val currencySymbol: CurrencySymbol,
    val buttonEnabled: Boolean = true,
)

