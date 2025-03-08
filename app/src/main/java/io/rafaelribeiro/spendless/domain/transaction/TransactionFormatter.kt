package io.rafaelribeiro.spendless.domain

import io.rafaelribeiro.spendless.data.repository.UserPreferences

interface TransactionFormatter {
    fun formatAmount(amount: Double, preferences: UserPreferences, excludeExpenseFormat: Boolean = false): String
    fun formatDateTime(timestamp: Long): String
}
