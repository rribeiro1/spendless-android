package io.rafaelribeiro.spendless.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
	val username: String,
    val pin: String,
    val sessionState: UserSessionState = UserSessionState.Idle,
)

enum class UserSessionState {
    Idle,           // show RegistrationScreen
    Active,         // show DashboardScreen
    Inactive,       // show LoginScreen
    Expired,        // show pinPromptScreen
}
