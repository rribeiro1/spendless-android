package io.rafaelribeiro.spendless.domain

import io.rafaelribeiro.spendless.domain.error.Error

sealed interface Result<out D, out E : Error> {
	data class Success<out D, out E : Error>(val data: D) : Result<D, E>
	data class Failure<out D, out E : Error>(val error: E) : Result<D, E>
}
