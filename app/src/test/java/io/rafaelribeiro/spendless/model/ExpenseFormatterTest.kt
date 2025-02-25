package io.rafaelribeiro.spendless.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ExpenseFormatter
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExpenseFormatterTest {

    @Test
    fun `should format negative expenses with thousand separator COMMA and decimal separator DOT`() {
        val expense = -10382.45
        val formatter = ExpenseFormatter(
            expensesFormat = ExpenseFormat.NEGATIVE,
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            currencySymbol = CurrencySymbol.DOLLAR
        )
        val result = formatter.format(expense)
        assertThat(result).isEqualTo("-$10,382.45")
    }

    @Test
    fun `should format negative expenses with parentheses`() {
        val expense = -10382.45
        val formatter = ExpenseFormatter(
            expensesFormat = ExpenseFormat.PARENTHESES,
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.DOT,
            currencySymbol = CurrencySymbol.EURO
        )
        val result = formatter.format(expense)
        assertThat(result).isEqualTo("(€10.382,45)")
    }

    @Test
    fun `should format negative expenses with space as thousand separator`() {
        val expense = -10382.45
        val formatter = ExpenseFormatter(
            expensesFormat = ExpenseFormat.NEGATIVE,
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.SPACE,
            currencySymbol = CurrencySymbol.POUND
        )
        val result = formatter.format(expense)
        assertThat(result).isEqualTo("-£10 382,45")
    }

    @Test
    fun `should format positive expenses`() {
        val expense = 10382.45
        val formatter = ExpenseFormatter(
            expensesFormat = ExpenseFormat.NEGATIVE,
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            currencySymbol = CurrencySymbol.DOLLAR
        )
        val result = formatter.format(expense)
        assertThat(result).isEqualTo("$10,382.45")
    }

    @Test
    fun `should format numbers with same decimal and thousand separator`() {
        val expense = 10382.45
        val formatter = ExpenseFormatter(
            expensesFormat = ExpenseFormat.NEGATIVE,
            decimalSeparator = DecimalSeparator.COMMA,
            thousandSeparator = ThousandSeparator.COMMA,
            currencySymbol = CurrencySymbol.DOLLAR
        )
        val result = formatter.format(expense)
        assertThat(result).isEqualTo("$10,382,45")
    }
}
