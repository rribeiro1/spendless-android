package io.rafaelribeiro.spendless.data.repository

import io.rafaelribeiro.spendless.core.data.TransactionCreator
import io.rafaelribeiro.spendless.domain.transaction.Transaction
import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.domain.transaction.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Fake implementation of [TransactionRepository] to be used in tests.
 * Change DI's [TransactionRepository] implementation to this one to use it in tests for convenience.
 */
class FakeTransactionRepository @Inject constructor() : TransactionRepository {
    override fun getBalance(): Flow<Double?> {
        return flow {
            emit(1000.toDouble())
        }
    }

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return flow {
            emit(TransactionCreator.createTransactions(40))
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

    override suspend fun getTransactionsFromLastThreeMonths(): List<Transaction> {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionsFromCurrentMonth(): List<Transaction> {
        TODO("Not yet implemented")
    }

    override suspend fun getTransactionsFromLastMonth(): List<Transaction> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTransaction(transaction: Transaction) {}

    override suspend fun deleteAllTransactions() {}
}
