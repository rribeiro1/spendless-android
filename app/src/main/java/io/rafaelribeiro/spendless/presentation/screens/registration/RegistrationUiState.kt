package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty

data class RegistrationUiState(
	val username: String = "",
	val errorMessage: UiText = Empty,
	val pin: String = "",
	val pinConfirmation: String = "",
	val nextButtonEnabled: Boolean = false,
    val preferences: RegistrationPreferencesUiState = RegistrationPreferencesUiState(
        entryFormat = "-$10,382.45",
        expensesFormat = ExpensesFormat.NEGATIVE,
        decimalSeparator = DecimalSeparator.DOT,
        thousandSeparator = ThousandSeparator.DOT,
        currencySymbol = CurrencySymbol.DOLLAR,
    ),
)

data class RegistrationPreferencesUiState(
    val entryFormat: String,
    val expensesFormat: ExpensesFormat,
    val decimalSeparator: DecimalSeparator,
    val thousandSeparator: ThousandSeparator,
    val currencySymbol: CurrencySymbol,
    val startTrackingButtonEnabled: Boolean = false,
)

enum class ExpensesFormat(val value: String) {
    NEGATIVE("-$10"),
    PARENTHESES("($10)")
}

enum class DecimalSeparator(val value: String, val symbol: String) {
    DOT("1.00", "."),
    COMMA("1,00", ","),
}

enum class ThousandSeparator(val value: String, val symbol: String) {
    DOT("1.000", "."),
    COMMA("1,000", ","),
    SPACE("1 000", " "),
}

enum class CurrencySymbol(val value: String) {
    DOLLAR("$"),
    EURO("€")
}
