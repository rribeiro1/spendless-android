package io.rafaelribeiro.spendless.domain

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class ExpenseFormatter(
    private val decimalSeparator: DecimalSeparator,
    private val thousandSeparator: ThousandSeparator,
    private val currencySymbol: CurrencySymbol,
    private val expensesFormat: ExpenseFormat
) {
    fun format(amount: Double): String {
        val formatter = createFormatter()
        val formattedValue = formatter.format(kotlin.math.abs(amount))
        return formatOutput(amount, formattedValue)
    }

    private fun createFormatter(): DecimalFormat {
        val symbols = DecimalFormatSymbols().apply {
            this.decimalSeparator = this@ExpenseFormatter.decimalSeparator.symbol.first()
            this.groupingSeparator = thousandSeparator.symbol.first()
        }
        return DecimalFormat("#,##0.00", symbols)
    }

    private fun formatOutput(amount: Double, formattedValue: String): String {
        val prefix = currencySymbol.symbol
        return when {
            amount < 0 && expensesFormat == ExpenseFormat.PARENTHESES -> "($prefix$formattedValue)"
            amount < 0 -> "-$prefix$formattedValue"
            else -> "$prefix$formattedValue"
        }
    }
}
