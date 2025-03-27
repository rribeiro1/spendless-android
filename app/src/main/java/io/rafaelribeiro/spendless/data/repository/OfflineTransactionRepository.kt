package io.rafaelribeiro.spendless.data.repository

import io.rafaelribeiro.spendless.data.database.TransactionDao
import io.rafaelribeiro.spendless.data.mapper.toTransaction
import io.rafaelribeiro.spendless.data.mapper.toTransactionEntity
import io.rafaelribeiro.spendless.data.mapper.toTransactions
import io.rafaelribeiro.spendless.domain.transaction.Transaction
import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.domain.transaction.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.abs

class OfflineTransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getBalance(): Flow<Double?> {
        return getAllTransactions().map {
            it.sumOf { transaction -> transaction.amount }
        }
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map { it.toTransactions() }
    }

    override fun getLatestTransactions(): Flow<List<Transaction>> {
       return transactionDao.getLatestTransactions().map { it.toTransactions() }
    }

    override fun getTotalAmountLastWeek(): Flow<Double?> {
        return transactionDao.getTransactionsLastWeek().map {
            it.toTransactions().sumOf { transaction ->
                transaction.amount
            }
        }
    }

    override fun getBiggestTransaction(): Flow<Transaction?> {
        return transactionDao.getExpenses().map {
            it.toTransactions().maxByOrNull { transaction ->
                abs(transaction.amount)
            }
        }
    }

    override fun getMostPopularCategory(): Flow<TransactionCategory?> {
        return transactionDao.getMostPopularCategory()
    }

    override suspend fun getTransactionsFromLastThreeMonths(): List<Transaction> {
        return transactionDao.getTransactionsFromLastThreeMonths().map { it.toTransaction() }
    }

    override suspend fun getTransactionsFromCurrentMonth(): List<Transaction> {
        return transactionDao.getTransactionsFromCurrentMonth().map { it.toTransaction() }
    }

    override suspend fun getTransactionsFromLastMonth(): List<Transaction> {
        return transactionDao.getTransactionsFromLastMonth().map { it.toTransaction() }
    }

    override suspend fun saveTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toTransactionEntity())
    }

    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }
}
