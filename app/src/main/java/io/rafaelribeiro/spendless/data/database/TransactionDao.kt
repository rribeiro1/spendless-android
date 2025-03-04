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
    @Query("SELECT * FROM transactions ORDER BY createdAt DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

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

    @Query("""
        SELECT 
            (SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'INCOME') -
            (SELECT COALESCE(SUM(amount), 0) FROM transactions WHERE type = 'EXPENSE')
        AS balance
    """)
    fun getBalance(): Flow<Double?>

    /**
     * Retrieves the total sum of expenses for the **previous full week** (Monday to Sunday).
     *
     * ## Query Logic:
     * - This method calculates the total amount spent on **EXPENSE** transactions in the **last full week**.
     * - The last full week is determined dynamically based on the **current date**.
     * - The query **automatically shifts week by week**, ensuring accuracy regardless of when it is called.
     *
     * ## How It Works:
     * - `strftime('%s', 'now', 'start of day', 'weekday 0', '-13 days') * 1000`
     *   - Moves to **Monday (00:00:00) of the previous full week**.
     * - `strftime('%s', 'now', 'start of day', 'weekday 0', '-6 days') * 1000`
     *   - Moves to **Monday (00:00:00) of the current week**, acting as an upper boundary.
     * - **Only transactions within this range are summed.**
     *
     * ## Example Cases:
     * | Run Date        | Start Date (Monday 00:00:00) | End Date (Sunday 23:59:59) | Filtered Transactions |
     * |---------------|----------------------|----------------------|----------------------|
     * | **March 1, 2025 (Saturday)** | Feb 17, 2025 | Feb 23, 2025 | ✅ Feb 17 → Feb 23 |
     * | **March 3, 2025 (Monday)**   | Feb 24, 2025 | March 2, 2025 | ✅ Feb 24 → March 2 |
     * | **March 5, 2025 (Wednesday)** | Feb 24, 2025 | March 2, 2025 | ✅ Feb 24 → March 2 |
     *
     * @return A `Flow<Double?>` representing the total sum of **EXPENSE** transactions from the last full week.
     */
    @Query("""
        SELECT COALESCE(SUM(amount), 0)
        FROM transactions
        WHERE type = 'EXPENSE'
        AND createdAt >= (strftime('%s', 'now', 'start of day', 'weekday 0', '-13 days') * 1000)
        AND createdAt < (strftime('%s', 'now', 'start of day', 'weekday 0', '-6 days') * 1000)
    """)
    fun getTotalAmountLastWeek(): Flow<Double?>
}
