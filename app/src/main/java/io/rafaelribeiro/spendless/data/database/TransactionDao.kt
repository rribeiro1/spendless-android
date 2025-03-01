package io.rafaelribeiro.spendless.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.rafaelribeiro.spendless.data.entity.TransactionEntity
import io.rafaelribeiro.spendless.domain.TransactionCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("""
        SELECT 
            (SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME') -
            (SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE')
        AS balance
    """)
    fun getBalance(): Flow<Double?>

    @Query("""
        SELECT SUM(amount) 
        FROM transactions 
        WHERE createdAt >= (strftime('%s', 'now', 'weekday 0', '-7 days') * 1000)
        AND createdAt < (strftime('%s', 'now', 'weekday 0') * 1000)
    """)
    fun getTotalAmountLastWeek(): Flow<Double?>

    @Query("SELECT * FROM transactions ORDER BY createdAt DESC LIMIT 20")
    fun getLatestTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions ORDER BY amount DESC LIMIT 1")
    fun getBiggestTransaction(): Flow<TransactionEntity?>

    @Query("SELECT category FROM transactions GROUP BY category ORDER BY COUNT(*) DESC LIMIT 1")
    fun getMostPopularCategory(): Flow<TransactionCategory?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}
