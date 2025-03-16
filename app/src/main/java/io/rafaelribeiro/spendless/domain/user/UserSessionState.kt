package io.rafaelribeiro.spendless.domain.user

/**
 * Represents the state of the user's session.
 * The state of the session will determine which screen should be shown to the user.
 * The state of the session can be one of the following:
 * - Idle: The user has not registered yet. Show the RegistrationScreen.
 * - Active: The user is logged in. Show the DashboardScreen.
 * - Inactive: The user is logged out. Show the LoginScreen.
 * - Expired: The user's session has expired. Show the PinPromptScreen.
 */
enum class UserSessionState {
    Idle,
    Active,
    Inactive,
    Expired,
}
