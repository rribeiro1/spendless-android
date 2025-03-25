package io.rafaelribeiro.spendless.domain.transaction

import io.rafaelribeiro.spendless.data.repository.UserPreferences
import io.rafaelribeiro.spendless.domain.preferences.CurrencySymbol
import io.rafaelribeiro.spendless.domain.preferences.DecimalSeparator
import io.rafaelribeiro.spendless.domain.preferences.ExpenseFormat
import io.rafaelribeiro.spendless.domain.preferences.ThousandSeparator
import io.rafaelribeiro.spendless.service.ExpenseFormatter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class DefaultTransactionFormatter @Inject constructor() : TransactionFormatter {
    override fun formatAmount(amount: Double, preferences: UserPreferences, excludeExpenseFormat: Boolean): String {
        val thousandSeparator = getEnumValue(ThousandSeparator::class.java, preferences.thousandsSeparatorName)
        val decimalSeparator = getEnumValue(DecimalSeparator::class.java, preferences.decimalSeparatorName)
        val expenseFormat = getEnumValue(ExpenseFormat::class.java, preferences.expensesFormatName)
        val currencySymbol = getEnumValue(CurrencySymbol::class.java, preferences.currencyName)
        val formatter = ExpenseFormatter(
            thousandSeparator = thousandSeparator,
            decimalSeparator = decimalSeparator,
            expensesFormat = expenseFormat,
            currencySymbol = currencySymbol,
        )
        return formatter.format(amount, excludeExpenseFormat)
    }

    override fun formatDateTime(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).format(formatter)
    }

    private fun <T : Enum<T>> getEnumValue(
        enumClass: Class<T>,
        name: String
    ): T = enumClass.enumConstants?.firstOrNull { it.name == name } ?: enumClass.enumConstants!!.first()
}
