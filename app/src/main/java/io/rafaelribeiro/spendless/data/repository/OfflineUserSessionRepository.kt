package io.rafaelribeiro.spendless.data.repository

import androidx.datastore.core.DataStore
import io.rafaelribeiro.spendless.domain.UserSessionRepository
import io.rafaelribeiro.spendless.domain.user.User
import io.rafaelribeiro.spendless.domain.user.UserSessionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineUserSessionRepository @Inject constructor(
    private val dataStore: DataStore<User>,
) : UserSessionRepository {

    override val sessionState: Flow<UserSessionState>
        get() = dataStore.data.map { it.sessionState }

    override suspend fun updateSessionState(state: UserSessionState) {
        dataStore.updateData {
            it.copy(sessionState = state)
        }
    }
}