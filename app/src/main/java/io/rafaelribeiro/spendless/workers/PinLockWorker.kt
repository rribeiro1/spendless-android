package io.rafaelribeiro.spendless.workers

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.rafaelribeiro.spendless.data.repository.DataStoreUserPreferencesRepository
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class PinLockWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val pinLockDataStore: DataStoreUserPreferencesRepository,
) : CoroutineWorker(appContext, workerParams) {
    companion object {
        const val WORKER_NAME = "PinLockWorker"
    }

    override suspend fun doWork(): Result {
        val initialRemainingSeconds = pinLockDataStore.pinLockStatePreferences.firstOrNull() ?: 0
        Log.i(WORKER_NAME, "Enqueueing PinLockWorker from $initialRemainingSeconds seconds...")
        return try {
            for (seconds in initialRemainingSeconds downTo 0) {
                pinLockDataStore.savePinLockState(seconds)
                if (seconds == 0) {
                    pinLockDataStore.clearPinLockState()
                    break
                }
                kotlinx.coroutines.delay(1000L)
            }
            Result.success()
        } catch (exception: Exception) {
            Log.e(WORKER_NAME, exception.message?:"")
            Result.failure()
        }
    }
}