package io.rafaelribeiro.spendless.data.repository

import androidx.datastore.core.DataStore
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.error.RegistrationError
import io.rafaelribeiro.spendless.domain.Result
import io.rafaelribeiro.spendless.domain.user.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineAuthRepository @Inject constructor(
    private val dataStore: DataStore<User>,
) : AuthRepository {

    override val pin: Flow<String> = dataStore.data.map { it.pin }

    override val userName: Flow<String> = dataStore.data.map { it.username }

	override suspend fun checkUserName(username: String): Result<Unit, RegistrationError> =
		if (!username.all { it.isLetterOrDigit() }) {
			Result.Failure(RegistrationError.USERNAME_MUST_BE_ALPHANUMERIC)
		} else if (username == userName.first()) {
			Result.Failure(RegistrationError.USERNAME_ALREADY_EXISTS)
		} else {
			Result.Success(Unit)
		}

	override suspend fun register(
		username: String,
		pin: String,
	): Result<User, RegistrationError> {

        dataStore.updateData {
            User(username, pin)
        }
        return Result.Success(User(username, pin))
    }

    override suspend fun isPinCorrect(pin: String): Boolean {
        return this.pin.first() == pin
    }

    override suspend fun authenticateCredentials(pin: String, username: String): Boolean {
        return this.pin.first() == pin && this.userName.first() == username
    }
}
