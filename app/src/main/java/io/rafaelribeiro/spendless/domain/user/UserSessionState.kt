package io.rafaelribeiro.spendless.domain.user

/**
 * Represents the state of the user's session.
 * The state of the session will determine which screen should be shown to the user.
 * The state of the session can be one of the following:
 * @property Idle: The user has not interacted with the app yet. There's no need to show any screen or show SplashScreen.
 * @property NotRegistered: The user has not registered yet. Show the RegistrationScreen.
 * @property Active: The user is logged in. Show the DashboardScreen.
 * @property Inactive: The user is logged out or has not logged in yet. Show the LoginScreen.
 * @property Expired: The user's session has expired. Show the PinPromptScreen.
 */
enum class UserSessionState {
    Idle,
    NotRegistered,
    Active,
    Inactive,
    Expired,
}
