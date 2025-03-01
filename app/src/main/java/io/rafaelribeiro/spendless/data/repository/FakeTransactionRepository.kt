package io.rafaelribeiro.spendless.data.repository

import io.rafaelribeiro.spendless.domain.Transaction
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.domain.TransactionRepository
import io.rafaelribeiro.spendless.domain.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.random.Random

class FakeTransactionRepository @Inject constructor() : TransactionRepository {
    override fun getBalance(): Flow<Double?> {
        return flow {
            emit(1000.toDouble())
        }
    }

    override fun getLatestTransactions(): Flow<List<Transaction>> {
        return flow {
            emit(TransactionCreator.createTransactions(10))
        }
    }

    override fun getTotalAmountLastWeek(): Flow<Double?> {
        return flow {
            emit(10000.toDouble())
        }
    }

    override fun getBiggestTransaction(): Flow<Transaction?> {
        return flow {
            emit(TransactionCreator.createTransaction())
        }
    }

    override fun getMostPopularCategory(): Flow<TransactionCategory?> {
        return flow {
            emit(TransactionCategory.FOOD)
        }
    }

    override suspend fun saveTransaction(transaction: Transaction) {}

    override suspend fun deleteAllTransactions() {}
}

class TransactionCreator {
    companion object {
        data class TransactionTest(
            val description: String,
            val category: TransactionCategory,
            val type: TransactionType,
            val amount: Double,
            val amountDisplay: String,
            val createdAt: Instant,
            val note: String? = null
        )

        fun createTransactions(quantity: Int): List<Transaction> {
            return List(quantity) {
                createTransaction()
            }
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
                createdAt = transaction.createdAt
            )
        }

        private fun randomTransaction(): TransactionTest {
            val transactions = listOf(
                TransactionTest("Amazon", TransactionCategory.HOME, TransactionType.EXPENSE, 100.00, "-$100.00", randomInstant()),
                TransactionTest("McDonald's", TransactionCategory.FOOD, TransactionType.EXPENSE, 50.00, "-$50.00", randomInstant(), "I was hungry today so I bought quite everything from the menu, I should stop doing this"),
                TransactionTest("Netflix Monthly Subscription from Brazil", TransactionCategory.ENTERTAINMENT, TransactionType.EXPENSE, 10.00, "-$10.00",  randomInstant(), "Netflix subscription for the month of January"),
                TransactionTest("Zara", TransactionCategory.CLOTHING, TransactionType.EXPENSE, 12492.50, "-$12,492.50", randomInstant(), "Bought a new suit for the wedding"),
                TransactionTest("Gym - Monthly Membership John Reed", TransactionCategory.HEALTH, TransactionType.EXPENSE, 100.00, "-$100.00",  randomInstant()),
                TransactionTest("Haircut", TransactionCategory.PERSONAL_CARE, TransactionType.EXPENSE, 50.00, "-$50.00", randomInstant()),
                TransactionTest("Uber", TransactionCategory.TRANSPORTATION, TransactionType.EXPENSE, 10.00, "-$10.00", randomInstant(), "Uber ride to work"),
                TransactionTest("Udemy", TransactionCategory.EDUCATION, TransactionType.EXPENSE, 140.50, "-$140.50", randomInstant()),
                TransactionTest("Rick's share - Birthday Present from Rafael", TransactionCategory.SAVINGS, TransactionType.INCOME, 100.00, "$100.00", randomInstant(), "Birthday present from Rafael it was just a little but it is fine I will probably invest it."),
            )
            return transactions.random()
        }

        private fun randomInstant(): Instant {
            val daysAgo = Random.nextInt(0, 30)
            return Instant.now().minus(daysAgo.toLong(), ChronoUnit.DAYS)
        }
    }
}
