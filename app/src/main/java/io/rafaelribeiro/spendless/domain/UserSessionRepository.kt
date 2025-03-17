package io.rafaelribeiro.spendless.domain

import io.rafaelribeiro.spendless.domain.user.UserSessionState
import kotlinx.coroutines.flow.Flow

interface UserSessionRepository {
    val sessionState: Flow<UserSessionState>
    suspend fun updateSessionState(state: UserSessionState)
}
