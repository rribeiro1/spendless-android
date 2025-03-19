package io.rafaelribeiro.spendless.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import io.rafaelribeiro.spendless.domain.UserSessionRepository
import io.rafaelribeiro.spendless.domain.user.User
import io.rafaelribeiro.spendless.domain.user.UserSessionState
import io.rafaelribeiro.spendless.workers.UserSessionWorker
import io.rafaelribeiro.spendless.workers.UserSessionWorker.Companion.WORKER_TAG
import io.rafaelribeiro.spendless.workers.UserSessionWorker.Companion.WORK_NAME
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class OfflineUserSessionRepository @Inject constructor(
    private val dataStore: DataStore<User>,
    @ApplicationContext private val context: Context,
) : UserSessionRepository {

    private val workManager = WorkManager.getInstance(context)

    override val sessionState: Flow<UserSessionState>
        get() = dataStore.data.map { it.sessionState }

    override suspend fun updateSessionState(state: UserSessionState) {
        dataStore.updateData {
            it.copy(sessionState = state)
        }
    }

    override fun startSession(sessionDurationInMinutes: Long) {
        val workInfo = workManager.getWorkInfosForUniqueWork(WORK_NAME).get()

        if (workInfo.isNullOrEmpty() || workInfo.any { it.state.isFinished }) {
            Log.i(WORKER_TAG, "Enqueueing Worker for $sessionDurationInMinutes minutes...")
            val constraints = Constraints.Builder()
                .setRequiresBatteryNotLow(false)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<UserSessionWorker>()
                .setInitialDelay(sessionDurationInMinutes, TimeUnit.MINUTES)
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

    override fun cancelWorker() {
        workManager.cancelUniqueWork(WORK_NAME)
    }
}