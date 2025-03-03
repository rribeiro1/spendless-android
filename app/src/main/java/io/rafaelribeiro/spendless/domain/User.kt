package io.rafaelribeiro.spendless.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(
	val username: String,
    val pin: String,
)
