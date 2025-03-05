package io.rafaelribeiro.spendless.presentation.screens.settings.preferences

import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ThousandSeparator

data class SettingsPreferencesUiState(
    val exampleExpenseFormat: String = "-$10,382.45",
    val expensesFormat: ExpenseFormat = ExpenseFormat.NEGATIVE,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.COMMA,
    val currencySymbol: CurrencySymbol = CurrencySymbol.DOLLAR,
    val buttonEnabled: Boolean = true,
)

