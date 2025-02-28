package io.rafaelribeiro.spendless.data

import io.rafaelribeiro.spendless.domain.Transaction
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.domain.TransactionRepository
import io.rafaelribeiro.spendless.domain.TransactionType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.random.Random

class FakeTransactionRepository @Inject constructor() : TransactionRepository {
    override suspend fun getTransactions(): Flow<Transaction> {
        return flow {
            TransactionCreator.createTransactions(20).forEach {
                emit(it)
            }
        }
    }
}

class TransactionCreator {
    companion object {
        data class TransactionTest(
            val description: String,
            val category: TransactionCategory,
            val type: TransactionType,
            val amount: Double,
            val amountDisplay: String,
            val date: LocalDateTime,
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
                id = Random.nextInt(),
                amount = transaction.amount,
                amountDisplay = transaction.amountDisplay,
                description = transaction.description,
                note = transaction.note,
                category = transaction.category,
                type = transaction.type,
                date = transaction.date
            )
        }

        private fun randomTransaction(): TransactionTest {
            val transactions = listOf(
                TransactionTest("Amazon", TransactionCategory.HOME, TransactionType.EXPENSE, 100.00, "-$100.00", LocalDateTime.now()),
                TransactionTest("McDonald's", TransactionCategory.FOOD, TransactionType.EXPENSE, 50.00, "-$50.00", LocalDateTime.now(), "I was hungry today so I bought quite everything from the menu, I should stop doing this"),
                TransactionTest("Netflix Monthly Subscription from Brazil", TransactionCategory.ENTERTAINMENT, TransactionType.EXPENSE, 10.00, "-$10.00", LocalDateTime.now(), "Netflix subscription for the month of January"),
                TransactionTest("Zara", TransactionCategory.CLOTHING, TransactionType.EXPENSE, 12492.50, "-$12,492.50", LocalDateTime.now(), "Bought a new suit for the wedding"),
                TransactionTest("Gym - Monthly Membership John Reed", TransactionCategory.HEALTH, TransactionType.EXPENSE, 100.00, "-$100.00", LocalDateTime.now().minusDays(1)),
                TransactionTest("Haircut", TransactionCategory.PERSONAL_CARE, TransactionType.EXPENSE, 50.00, "-$50.00", LocalDateTime.now().minusDays(1)),
                TransactionTest("Uber", TransactionCategory.TRANSPORTATION, TransactionType.EXPENSE, 10.00, "-$10.00", LocalDateTime.now().minusDays(2), "Uber ride to work"),
                TransactionTest("Udemy", TransactionCategory.EDUCATION, TransactionType.EXPENSE, 140.50, "-$140.50", LocalDateTime.now().minusDays(3)),
                TransactionTest("Rick's share - Birthday Present from Rafael", TransactionCategory.SAVINGS, TransactionType.INCOME, 100.00, "$100.00", LocalDateTime.now(), "Birthday present from Rafael it was just a little but it is fine I will probably invest it."),
            )
            return transactions.random()
        }
    }
}
