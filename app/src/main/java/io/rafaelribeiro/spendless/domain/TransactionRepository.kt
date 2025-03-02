package io.rafaelribeiro.spendless.domain

import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getBalance(): Flow<Double?>
    fun getLatestTransactions(): Flow<List<Transaction>>
    fun getTotalAmountLastWeek(): Flow<Double?>
    fun getBiggestTransaction(): Flow<Transaction?>
    fun getMostPopularCategory(): Flow<TransactionCategory?>
    suspend fun saveTransaction(transaction: Transaction)
    suspend fun deleteAllTransactions()
}
