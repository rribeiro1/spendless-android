package io.rafaelribeiro.spendless.domain.transaction

import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getBalance(): Flow<Double?>
    fun getAllTransactions(): Flow<List<Transaction>>
    fun getLatestTransactions(): Flow<List<Transaction>>
    fun getTotalAmountLastWeek(): Flow<Double?>
    fun getBiggestTransaction(): Flow<Transaction?>
    fun getMostPopularCategory(): Flow<TransactionCategory?>
    suspend fun getTransactionsFromLastThreeMonths(): List<Transaction>
    suspend fun getTransactionsFromCurrentMonth(): List<Transaction>
    suspend fun getTransactionsFromLastMonth(): List<Transaction>
    suspend fun saveTransaction(transaction: Transaction)
    suspend fun deleteAllTransactions()
}
