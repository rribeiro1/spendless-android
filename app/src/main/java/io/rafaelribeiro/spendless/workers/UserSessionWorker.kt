package io.rafaelribeiro.spendless.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.rafaelribeiro.spendless.domain.UserSessionRepository
import io.rafaelribeiro.spendless.domain.user.UserSessionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

@HiltWorker
class UserSessionWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userSessionRepository: UserSessionRepository,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORKER_TAG = "UserSessionWorker"
        private const val WORK_NAME = "user_session_worker"

        fun enqueue(context: Context, sessionExpiryDuration: Int) {
            val workManager = WorkManager.getInstance(context)
            val workInfo = workManager.getWorkInfosForUniqueWork(WORK_NAME).get()

            if (workInfo.isNullOrEmpty() || workInfo.any { it.state.isFinished }) {
                Log.i(WORKER_TAG, "Enqueueing Worker for $sessionExpiryDuration minutes...")
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
                Log.i(WORKER_TAG, "Worker is already running or enqueued...")
            }
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }

    override suspend fun doWork(): Result {
        Log.i(WORKER_TAG, "Do Work Started...")
        withContext(Dispatchers.IO) {
            userSessionRepository.updateSessionState(UserSessionState.Expired)
        }
        Log.i(WORKER_TAG, "Do Work Finished...")
        return Result.success()
    }
}
