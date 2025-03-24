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

    /**
     * Starts a new session by enqueuing a [UserSessionWorker] to run after [sessionDurationInMinutes] minutes.
     * We don't need to check whether worker is already enqueued because we are using [ExistingWorkPolicy.REPLACE]
     * in case the user changes the session duration in the settings.
     * @param sessionDurationInMinutes The duration of the session in minutes.
     * @see UserSessionWorker
     */
    override fun startSession(sessionDurationInMinutes: Long) {
        Log.i(WORKER_TAG, "Enqueueing Session Worker for $sessionDurationInMinutes minutes...")
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
    }

    override fun cancelWorker() {
        workManager.cancelUniqueWork(WORK_NAME)
    }
}