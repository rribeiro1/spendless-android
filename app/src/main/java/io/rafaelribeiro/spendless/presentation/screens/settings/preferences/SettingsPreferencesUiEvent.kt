package io.rafaelribeiro.spendless.presentation.screens.settings.preferences

import io.rafaelribeiro.spendless.domain.preferences.CurrencySymbol
import io.rafaelribeiro.spendless.domain.preferences.DecimalSeparator
import io.rafaelribeiro.spendless.domain.preferences.ExpenseFormat
import io.rafaelribeiro.spendless.domain.preferences.ThousandSeparator

sealed interface SettingsPreferencesUiEvent {
    data object ButtonClicked : SettingsPreferencesUiEvent
    data class ExpensesFormatSelected(val expensesFormat: ExpenseFormat) : SettingsPreferencesUiEvent
    data class DecimalSeparatorSelected(val decimalSeparator: DecimalSeparator) : SettingsPreferencesUiEvent
    data class ThousandSeparatorSelected(val thousandSeparator: ThousandSeparator) : SettingsPreferencesUiEvent
    data class CurrencySelected(val currency: CurrencySymbol) : SettingsPreferencesUiEvent
}