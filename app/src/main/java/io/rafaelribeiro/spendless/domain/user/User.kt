package io.rafaelribeiro.spendless.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
	val username: String,
    val pin: String,
)
