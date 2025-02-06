package io.rafaelribeiro.spendless.domain

enum class RegistrationError : Error {
	USERNAME_ALREADY_EXISTS,
	USERNAME_MUST_BE_ALPHANUMERIC,
}
