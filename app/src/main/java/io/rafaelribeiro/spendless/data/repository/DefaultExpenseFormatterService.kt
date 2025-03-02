package io.rafaelribeiro.spendless.data.repository

import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ExpenseFormatter
import io.rafaelribeiro.spendless.domain.ExpenseFormatterService
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class DefaultExpenseFormatterService @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ExpenseFormatterService {
    override suspend fun format(amount: Double): String {
        val userPreferences = userPreferencesRepository.userPreferences.first()
        val thousandSeparator = getEnumValue(ThousandSeparator::class.java, userPreferences.thousandsSeparatorName)
        val decimalSeparator = getEnumValue(DecimalSeparator::class.java, userPreferences.decimalSeparatorName)
        val expenseFormat = getEnumValue(ExpenseFormat::class.java, userPreferences.expensesFormatName)
        val currencySymbol = getEnumValue(CurrencySymbol::class.java, userPreferences.currencyName)
        val formatter = ExpenseFormatter(
            thousandSeparator = thousandSeparator,
            decimalSeparator = decimalSeparator,
            expensesFormat = expenseFormat,
            currencySymbol = currencySymbol,
        )
        return formatter.format(amount)
    }
}

private fun <T : Enum<T>> getEnumValue(enumClass: Class<T>, name: String): T =
    enumClass.enumConstants?.firstOrNull { it.name == name } ?: enumClass.enumConstants!!.first()
