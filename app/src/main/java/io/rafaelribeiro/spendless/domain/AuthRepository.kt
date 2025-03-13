package io.rafaelribeiro.spendless.domain

import io.rafaelribeiro.spendless.domain.error.RegistrationError
import io.rafaelribeiro.spendless.domain.user.User
import io.rafaelribeiro.spendless.domain.user.UserSessionState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val userName: Flow<String>
    val pin: Flow<String>
    val sessionState: Flow<UserSessionState>
	suspend fun checkUserName(username: String): Result<Unit, RegistrationError>
	suspend fun register(username: String, pin: String): Result<User, RegistrationError>
    suspend fun isPinCorrect(pin: String): Boolean
    suspend fun authenticateCredentials(pin: String, username: String): Boolean
    suspend fun updateSessionState(state: UserSessionState)
}
