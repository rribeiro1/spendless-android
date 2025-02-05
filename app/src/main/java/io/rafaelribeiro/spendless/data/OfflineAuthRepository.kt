package io.rafaelribeiro.spendless.data

import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.RegistrationError
import io.rafaelribeiro.spendless.domain.Result
import io.rafaelribeiro.spendless.domain.User
import javax.inject.Inject

class OfflineAuthRepository
	@Inject
	constructor() : AuthRepository {
		override fun exists(username: String): Result<Unit, RegistrationError> =
			if (username == "rafael") {
				Result.Failure(RegistrationError.USERNAME_ALREADY_EXISTS)
			} else {
				Result.Success(Unit)
			}

		override fun register(
			username: String,
			pin: String,
		): Result<User, RegistrationError> = Result.Success(User(username))
	}
