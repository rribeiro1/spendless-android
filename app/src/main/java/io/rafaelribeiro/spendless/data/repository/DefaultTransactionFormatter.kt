package io.rafaelribeiro.spendless.data.repository

import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ExpenseFormatter
import io.rafaelribeiro.spendless.domain.TransactionFormatter
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId.systemDefault
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class DefaultTransactionFormatter @Inject constructor() : TransactionFormatter {
    override fun formatAmount(amount: Double, preferences: UserPreferences): String {
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
        return formatter.format(amount)
    }

    override fun formatDateTime(timestamp: Long): String {
        val instant = Instant.ofEpochMilli(timestamp)
        val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
        return LocalDateTime.ofInstant(instant, systemDefault()).format(formatter)
    }
}

private fun <T : Enum<T>> getEnumValue(
    enumClass: Class<T>,
    name: String
): T = enumClass.enumConstants?.firstOrNull { it.name == name } ?: enumClass.enumConstants!!.first()
