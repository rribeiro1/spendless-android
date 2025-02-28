package io.rafaelribeiro.spendless.domain

import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun getTransactions(): Flow<Transaction>
}
