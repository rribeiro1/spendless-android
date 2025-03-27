package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.domain.preferences.CurrencySymbol
import io.rafaelribeiro.spendless.domain.preferences.DecimalSeparator
import io.rafaelribeiro.spendless.domain.preferences.ExpenseFormat
import io.rafaelribeiro.spendless.domain.preferences.ThousandSeparator

sealed interface RegistrationUiEvent {
    data class UsernameChanged(val username: String) : RegistrationUiEvent
    data object ActionButtonNextClicked : RegistrationUiEvent
    data object LoginLinkClicked : RegistrationUiEvent

    data class PinDigitTapped(val digit: String) : RegistrationUiEvent
    data object PinBackspaceTapped : RegistrationUiEvent
    data class PinConfirmationDigitTapped(val digit: String) : RegistrationUiEvent
    data object PinConfirmationBackspaceTapped : RegistrationUiEvent
    data object ResetPinValues : RegistrationUiEvent

    data object ButtonClicked : RegistrationUiEvent
    data class ExpensesFormatSelected(val expensesFormat: ExpenseFormat) : RegistrationUiEvent
    data class DecimalSeparatorSelected(val decimalSeparator: DecimalSeparator) : RegistrationUiEvent
    data class ThousandSeparatorSelected(val thousandSeparator: ThousandSeparator) : RegistrationUiEvent
    data class CurrencySelected(val currency: CurrencySymbol) : RegistrationUiEvent
}