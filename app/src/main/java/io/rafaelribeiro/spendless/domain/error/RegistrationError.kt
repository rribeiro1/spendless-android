package io.rafaelribeiro.spendless.domain.error

enum class RegistrationError : Error {
	USERNAME_ALREADY_EXISTS,
	USERNAME_MUST_BE_ALPHANUMERIC,
}
