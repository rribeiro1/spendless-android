package io.rafaelribeiro.spendless.domain

interface AuthRepository {
	fun exists(username: String): Result<Unit, RegistrationError>

	fun register(
		username: String,
		pin: String,
	): Result<User, RegistrationError>
}
