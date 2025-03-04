package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.PreferencesUiState
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ThousandSeparator

data class RegistrationUiState(
    val username: String = "",
    val errorMessage: UiText = Empty,
    val pin: String = "",
    val pinConfirmation: String = "",
    val nextButtonEnabled: Boolean = false,
    val preferences: PreferencesUiState = PreferencesUiState(
        exampleExpenseFormat = "-$10,382.45",
        expensesFormat = ExpenseFormat.NEGATIVE,
        decimalSeparator = DecimalSeparator.DOT,
        thousandSeparator = ThousandSeparator.COMMA,
        currencySymbol = CurrencySymbol.DOLLAR,
    ),
)

