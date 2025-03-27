package io.rafaelribeiro.spendless.data.repository

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.rafaelribeiro.spendless.domain.PinLockTickerRepository
import io.rafaelribeiro.spendless.workers.PinLockWorker
import io.rafaelribeiro.spendless.workers.PinLockWorker.Companion.WORKER_NAME
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OfflinePinLockTickerRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : PinLockTickerRepository {

    companion object{
        const val REPEAT_INTERVAL = 10L
    }
    private val workManager = WorkManager.getInstance(context)

    override fun startSession(remainingSeconds: Int) {
        Log.i(WORKER_NAME, "Enqueueing PinLockWorker")
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(false)
            .build()
        val workRequest = PeriodicWorkRequestBuilder<PinLockWorker>(
            repeatInterval = REPEAT_INTERVAL,
            repeatIntervalTimeUnit = TimeUnit.SECONDS
        )
            .setConstraints(constraints)
            .build()
        workManager.enqueueUniquePeriodicWork(
            WORKER_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            workRequest,
        )
    }

    override fun cancelWorker() {
        workManager.cancelUniqueWork(WORKER_NAME)
    }
}