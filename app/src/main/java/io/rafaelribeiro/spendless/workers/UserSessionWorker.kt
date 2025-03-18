package io.rafaelribeiro.spendless.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.rafaelribeiro.spendless.domain.UserSessionRepository
import io.rafaelribeiro.spendless.domain.user.UserSessionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class UserSessionWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val userSessionRepository: UserSessionRepository,
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        const val WORKER_TAG = "UserSessionWorker"
        const val WORK_NAME = "user_session_worker"
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
