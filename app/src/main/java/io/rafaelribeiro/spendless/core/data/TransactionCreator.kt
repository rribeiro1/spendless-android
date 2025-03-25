package io.rafaelribeiro.spendless.core.data

import io.rafaelribeiro.spendless.data.repository.UserPreferences
import io.rafaelribeiro.spendless.domain.preferences.CurrencySymbol
import io.rafaelribeiro.spendless.domain.preferences.DecimalSeparator
import io.rafaelribeiro.spendless.domain.preferences.ExpenseFormat
import io.rafaelribeiro.spendless.service.ExpenseFormatter
import io.rafaelribeiro.spendless.domain.preferences.ThousandSeparator
import io.rafaelribeiro.spendless.domain.transaction.Transaction
import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.domain.transaction.TransactionFormatter
import io.rafaelribeiro.spendless.domain.transaction.TransactionRecurrenceType
import io.rafaelribeiro.spendless.domain.transaction.TransactionType
import io.rafaelribeiro.spendless.domain.transaction.toUIModel
import io.rafaelribeiro.spendless.presentation.screens.dashboard.TransactionUiModel
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId.systemDefault
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.random.Random

/**
 * Helper class to create transactions for tests.
 */
class TransactionCreator {
    companion object {
        private val testTransactionFormatter: TransactionFormatter = object : TransactionFormatter {
            override fun formatAmount(amount: Double, preferences: UserPreferences, amountOnly: Boolean): String {
                val formatter = ExpenseFormatter(
                    thousandSeparator = ThousandSeparator.DOT,
                    decimalSeparator = DecimalSeparator.COMMA,
                    expensesFormat = ExpenseFormat.NEGATIVE,
                    currencySymbol = CurrencySymbol.DOLLAR,
                )
                return formatter.format(amount)
            }

            override fun formatDateTime(timestamp: Long): String {
                val instant = Instant.ofEpochMilli(timestamp)
                val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
                return LocalDateTime.ofInstant(instant, systemDefault()).format(formatter)
            }
        }

        data class TransactionTest(
            val description: String,
            val category: TransactionCategory,
            val type: TransactionType,
            val amount: Double,
            val amountDisplay: String,
            val createdAt: Long,
            val note: String? = null
        )

        fun createTransactions(quantity: Int): List<Transaction> {
            return List(quantity) { createTransaction() }
        }

        fun createTransaction(): Transaction {
            val transaction = randomTransaction()
            return Transaction(
                id = Random.nextLong(),
                amount = transaction.amount,
                description = transaction.description,
                note = transaction.note,
                category = transaction.category,
                type = transaction.type,
                recurrence = TransactionRecurrenceType.NONE,
                createdAt = transaction.createdAt
            )
        }

        fun createTransactionUiModel(): TransactionUiModel {
            return createTransaction().toUIModel(testTransactionFormatter, UserPreferences())
        }

        fun createTransactionUiModels(quantity: Int): List<TransactionUiModel> {
            return List(quantity) { createTransactionUiModel() }
        }

        private fun randomTransaction(): TransactionTest {
            val transactions = listOf(
                TransactionTest(
                    description = "Amazon",
                    category = TransactionCategory.HOME,
                    type = TransactionType.EXPENSE,
                    amount = -100.00,
                    amountDisplay = "-$100.00",
                    createdAt = randomTimestamp()
                ),
                TransactionTest(
                    description = "McDonald's",
                    category = TransactionCategory.FOOD,
                    type = TransactionType.EXPENSE,
                    amount = -1450.00,
                    amountDisplay = "-$1450.00",
                    createdAt = randomTimestamp(),
                    note = "I was hungry today so I bought quite everything from the menu, I should stop doing this"
                ),
                TransactionTest(
                    description = "Netflix Monthly Subscription from Brazil",
                    category = TransactionCategory.ENTERTAINMENT,
                    type = TransactionType.EXPENSE,
                    amount = -10.00,
                    amountDisplay = "-$10.00",
                    createdAt = randomTimestamp()
                ),
                TransactionTest(
                    description = "Zara",
                    category = TransactionCategory.CLOTHING,
                    type = TransactionType.EXPENSE,
                    amount = -12492.50,
                    amountDisplay = "-$12,492.50",
                    createdAt = randomTimestamp()
                ),
                TransactionTest(
                    description = "Gym - Monthly Membership John Reed",
                    category = TransactionCategory.HEALTH,
                    type = TransactionType.EXPENSE,
                    amount = -100.00,
                    amountDisplay = "-$100.00",
                    createdAt = randomTimestamp(),
                    note = "I am trying to get back in shape, let's see how it goes. But I am more like an gym investor because I just pay and don't go."
                ),
                TransactionTest(
                    description = "Haircut",
                    category = TransactionCategory.PERSONAL_CARE,
                    type = TransactionType.EXPENSE,
                    amount = -50.00,
                    amountDisplay = "-$50.00",
                    createdAt = randomTimestamp(),
                    note = "I was looking like a caveman, so I decided to cut my hair."
                ),
                TransactionTest(
                    description = "Uber",
                    category = TransactionCategory.TRANSPORTATION,
                    type = TransactionType.EXPENSE,
                    amount = -10.00,
                    amountDisplay = "-$10.00",
                    createdAt = randomTimestamp(),
                    note = "I was late for a meeting, so I had to take an Uber."
                ),
                TransactionTest(
                    description = "Udemy",
                    category = TransactionCategory.EDUCATION,
                    type = TransactionType.EXPENSE,
                    amount = -140.50,
                    amountDisplay = "-$140.50",
                    createdAt = randomTimestamp(),
                    note = "I am learning this android thing, let's see if I can get a job with this."
                ),
                TransactionTest(
                    description = "Rick's share - Birthday Present from Rafael",
                    category = TransactionCategory.SAVINGS,
                    type = TransactionType.INCOME,
                    amount = 100.00,
                    amountDisplay = "$100.00",
                    createdAt = randomTimestamp(),
                    note = "Birthday present from Rafael."
                ),
            )
            return transactions.random()
        }

        private fun randomTimestamp(): Long {
            val daysAgo = Random.nextInt(0, 30)
            return Instant.now().minus(daysAgo.toLong(), ChronoUnit.DAYS).toEpochMilli()
        }
    }
}
