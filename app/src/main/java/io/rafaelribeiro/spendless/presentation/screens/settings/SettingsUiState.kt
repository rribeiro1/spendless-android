package io.rafaelribeiro.spendless.presentation.screens.settings

import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.PreferencesUiState
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.LockoutDuration
import io.rafaelribeiro.spendless.domain.SessionExpiryDuration
import io.rafaelribeiro.spendless.domain.ThousandSeparator

data class SettingsUiState(
    val sessionExpiryDuration: SessionExpiryDuration = SessionExpiryDuration.MINUTES_5,
    val lockoutDuration: LockoutDuration = LockoutDuration.SECONDS_30,
    val preferences: PreferencesUiState = PreferencesUiState(
        exampleExpenseFormat = "-$10,382.45",
        expensesFormat = ExpenseFormat.NEGATIVE,
        decimalSeparator = DecimalSeparator.DOT,
        thousandSeparator = ThousandSeparator.COMMA,
        currencySymbol = CurrencySymbol.DOLLAR,
    ),
)
