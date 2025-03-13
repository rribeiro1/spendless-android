package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.error.RegistrationError
import io.rafaelribeiro.spendless.domain.Result
import io.rafaelribeiro.spendless.domain.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AuthRepositoryFake : AuthRepository {
    private val testUsername = "rafael"
    private val testPin = "12345"

    override suspend fun checkUserName(username: String): Result<Unit, RegistrationError> {
        return if (username == testUsername) {
            Result.Failure(RegistrationError.USERNAME_ALREADY_EXISTS)
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun register(username: String, pin: String): Result<User, RegistrationError> {
        return Result.Success(User(username = username, pin = pin))
    }

    override suspend fun isPinCorrect(pin: String): Boolean {
        return pin == testPin
    }

    override suspend fun authenticateCredentials(pin: String, username: String): Boolean {
        return pin == testPin && username == testUsername
    }

    override val userName: Flow<String> = flow { emit(testUsername) }

    override val pin: Flow<String> = flow { emit(testPin) }
}
