package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.RegistrationError
import io.rafaelribeiro.spendless.domain.Result
import io.rafaelribeiro.spendless.domain.User
import kotlinx.coroutines.flow.Flow

class AuthRepositoryFake : AuthRepository {
    override suspend fun checkUserName(username: String): Result<Unit, RegistrationError> {
        if (username == "rafael") {
            return Result.Failure(RegistrationError.USERNAME_ALREADY_EXISTS)
        } else {
            return Result.Success(Unit)
        }
    }

    override suspend fun register(username: String, pin: String): Result<User, RegistrationError> {
        return Result.Success(User(username = username))
    }

    override val userName: Flow<String>
        get() = TODO("Not yet implemented")
    override val pin: Flow<String>
        get() = TODO("Not yet implemented")
}
