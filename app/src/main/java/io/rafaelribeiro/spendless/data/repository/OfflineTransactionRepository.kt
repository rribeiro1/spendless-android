package io.rafaelribeiro.spendless.data.repository

import io.rafaelribeiro.spendless.data.database.TransactionDao
import io.rafaelribeiro.spendless.data.mapper.toTransaction
import io.rafaelribeiro.spendless.data.mapper.toTransactionEntity
import io.rafaelribeiro.spendless.data.mapper.toTransactions
import io.rafaelribeiro.spendless.domain.Transaction
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineTransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) : TransactionRepository {
    override fun getBalance(): Flow<Double?> {
        return transactionDao.getBalance()
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getLatestTransactions().map { it.toTransactions() }
    }

    override fun getLatestTransactions(): Flow<List<Transaction>> {
       return transactionDao.getLatestTransactions().map { it.toTransactions() }
    }

    override fun getTotalAmountLastWeek(): Flow<Double?> {
        return transactionDao.getTotalAmountLastWeek()
    }

    override fun getBiggestTransaction(): Flow<Transaction?> {
        return transactionDao.getBiggestTransaction().map { it?.toTransaction() }
    }

    override fun getMostPopularCategory(): Flow<TransactionCategory?> {
        return transactionDao.getMostPopularCategory()
    }

    override suspend fun saveTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toTransactionEntity())
    }

    override suspend fun deleteAllTransactions() {
        transactionDao.deleteAllTransactions()
    }
}
