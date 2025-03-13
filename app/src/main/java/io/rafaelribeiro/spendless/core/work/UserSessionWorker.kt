package io.rafaelribeiro.spendless.core.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import io.rafaelribeiro.spendless.SpendLessApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class UserSessionWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val WORK_NAME = "user_session_worker"

        fun enqueue(context: Context, sessionExpiryDuration: Int) {
            val workManager = WorkManager.getInstance(context)
            val workInfo = workManager.getWorkInfosForUniqueWork(WORK_NAME).get() // Check existing work

            if (workInfo.isNullOrEmpty() ||
                workInfo.any {
                    it.state == WorkInfo.State.SUCCEEDED || it.state == WorkInfo.State.FAILED || it.state == WorkInfo.State.CANCELLED
                }
            ) {
                // Only enqueue if no work exists or existing work is finished
                println("enqueueing worker STARTED")
                val constraints = Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build()

                val workRequest = OneTimeWorkRequestBuilder<UserSessionWorker>()
                    .setInitialDelay(sessionExpiryDuration.toLong(), TimeUnit.MINUTES)
                    .setConstraints(constraints)
                    .build()

                workManager.enqueueUniqueWork(
                    WORK_NAME,
                    ExistingWorkPolicy.REPLACE,
                    workRequest
                )
            } else {
                println("asd Worker is already running or enqueued")
            }
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }

    override suspend fun doWork(): Result {
        println("asd enqueueing worker doWork** STARTED")
        withContext(Dispatchers.Main) {
            // Trigger ViewModel's sessionTimeout function
            (applicationContext as SpendLessApplication).sessionTimeout()
        }
        println("asd enqueueing worker FINISHED")
        return Result.success()
    }

}
