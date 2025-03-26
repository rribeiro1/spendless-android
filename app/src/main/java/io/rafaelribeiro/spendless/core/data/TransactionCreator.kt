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
        private val expenseDescriptionsByCategory = mapOf(
            "Gym" to TransactionCategory.HEALTH,
            "School" to TransactionCategory.EDUCATION,
            "Kebab" to TransactionCategory.FOOD,
            "McDonalds" to TransactionCategory.FOOD,
            "Taxi" to TransactionCategory.TRANSPORTATION,
            "Groceries" to TransactionCategory.FOOD,
            "Spotify" to TransactionCategory.ENTERTAINMENT,
            "Netflix" to TransactionCategory.ENTERTAINMENT,
            "Zara" to TransactionCategory.CLOTHING,
            "Books" to TransactionCategory.EDUCATION,
            "Gas" to TransactionCategory.TRANSPORTATION,
            "Uber" to TransactionCategory.TRANSPORTATION,
            "Haircut" to TransactionCategory.PERSONAL_CARE,
            "Dinner" to TransactionCategory.FOOD,
            "Snacks" to TransactionCategory.FOOD,
            "Coffee" to TransactionCategory.FOOD,
            "Train" to TransactionCategory.TRANSPORTATION,
            "Pharmacy" to TransactionCategory.HEALTH,
            "Bakery" to TransactionCategory.FOOD,
            "Lunch" to TransactionCategory.FOOD,
            "Salary" to TransactionCategory.INCOME,
            "Freelance" to TransactionCategory.INCOME,
        )

        private val expenseAmounts = listOf(
            -10.0, -15.5, -7.99, -1120.0, -45.0, -89.99, -12.5, -60.0, -33.33, -99.9,
            -25.0, -50.0, -18.0, -75.25, -22.0, -5.0, -30.0, -55.5, -11.11, -90.0
        )

        private val incomeAmounts = listOf(1099.00, 1500.50, 499.50, 1120.0, 450.0, 899.99)

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
            val (description, category) = expenseDescriptionsByCategory.entries.random()
            val includeNote = Random.nextBoolean()
            val type = if (category == TransactionCategory.INCOME) TransactionType.INCOME else TransactionType.EXPENSE
            val amount = if (type == TransactionType.INCOME) incomeAmounts.random() else expenseAmounts.random()
            val amountDisplay = amount.toString()
            val note = if (includeNote) "This is a note for $description." else null

            return TransactionTest(
                description = description,
                category = category,
                type = type,
                amount = amount,
                amountDisplay = amountDisplay,
                createdAt = randomTimestamp(),
                note = note
            )
        }

        private fun randomTimestamp(): Long {
            val daysAgo = Random.nextInt(0, 120) // Approx. 4 months
            return Instant.now().minus(daysAgo.toLong(), ChronoUnit.DAYS).toEpochMilli()
        }
    }
}
