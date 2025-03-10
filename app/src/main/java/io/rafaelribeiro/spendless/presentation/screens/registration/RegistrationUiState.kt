package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty
import io.rafaelribeiro.spendless.domain.preferences.CurrencySymbol
import io.rafaelribeiro.spendless.domain.preferences.DecimalSeparator
import io.rafaelribeiro.spendless.domain.preferences.ExpenseFormat
import io.rafaelribeiro.spendless.domain.preferences.ThousandSeparator

data class RegistrationUiState(
    val username: String = "",
    val errorMessage: UiText = Empty,
    val pin: String = "",
    val pinConfirmation: String = "",
    val nextButtonEnabled: Boolean = false,
    val preferences: RegistrationPreferencesUiState = RegistrationPreferencesUiState(),
)

data class RegistrationPreferencesUiState(
    val exampleExpenseFormat: String = "-$10,382.45",
    val expensesFormat: ExpenseFormat = ExpenseFormat.NEGATIVE,
    val decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    val thousandSeparator: ThousandSeparator = ThousandSeparator.COMMA,
    val currencySymbol: CurrencySymbol = CurrencySymbol.DOLLAR,
    val buttonEnabled: Boolean = true,
)
