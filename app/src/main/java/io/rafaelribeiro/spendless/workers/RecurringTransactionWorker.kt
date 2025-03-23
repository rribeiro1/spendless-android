package io.rafaelribeiro.spendless.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.rafaelribeiro.spendless.domain.transaction.TransactionRecurrence.Companion.getNextOccurrence
import io.rafaelribeiro.spendless.domain.transaction.TransactionRecurrenceType
import io.rafaelribeiro.spendless.domain.transaction.TransactionRepository
import kotlinx.coroutines.flow.first
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@HiltWorker
class RecurringTransactionWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val transactionRepository: TransactionRepository
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORK_NAME = "recurring_transaction_worker"
    }

    override suspend fun doWork(): Result {
        val transactions = transactionRepository.getAllTransactions().first()
        val recurringTransactions = transactions.filter { it.recurrence != TransactionRecurrenceType.NONE }
        val today = LocalDate.now()

        for (transaction in recurringTransactions) {
            Log.i(WORK_NAME, "Processing transaction with description ${transaction.description}")
            var currentDate = Instant
                .ofEpochMilli(transaction.createdAt)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()

            var nextDate = getNextOccurrence(currentDate, transaction.recurrence)
            Log.i(WORK_NAME, "Next occurrence calculated for $nextDate, today is $today")

            while (nextDate != null && !nextDate.isAfter(LocalDate.now())) {
                Log.i(WORK_NAME, "Creating transaction with description ${transaction.description} for $nextDate")
                val newTransaction = transaction.copy(
                    id = 0,
                    recurrence = TransactionRecurrenceType.NONE,
                    createdAt = nextDate
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                )
                transactionRepository.saveTransaction(newTransaction)
                Log.i(WORK_NAME, "Transaction created")
                currentDate = nextDate
                nextDate = getNextOccurrence(currentDate, transaction.recurrence)
                Log.i(WORK_NAME, "Next occurrence calculated for $nextDate")
            }
        }
        return Result.success()
    }
}