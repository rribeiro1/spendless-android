package io.rafaelribeiro.spendless.data.repository

import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.RegistrationError
import io.rafaelribeiro.spendless.domain.Result
import io.rafaelribeiro.spendless.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OfflineAuthRepository @Inject constructor() : AuthRepository {
	override fun checkUserName(username: String): Result<Unit, RegistrationError> =
		if (!username.all { it.isLetterOrDigit() }) {
			Result.Failure(RegistrationError.USERNAME_MUST_BE_ALPHANUMERIC)
		} else if (username == "rafael") {
			Result.Failure(RegistrationError.USERNAME_ALREADY_EXISTS)
		} else {
			Result.Success(Unit)
		}

	override fun register(
		username: String,
		pin: String,
	): Result<User, RegistrationError> = Result.Success(User(username))
}
