package io.rafaelribeiro.spendless.presentation.screens.registration

sealed interface RegistrationActionEvent {
    data object UsernameCheckSuccess : RegistrationActionEvent
    data object PinCreated : RegistrationActionEvent
    data object PinConfirmed : RegistrationActionEvent
    data object PinMismatch : RegistrationActionEvent
    data object AlreadyHaveAccount : RegistrationActionEvent
    data object UserPreferencesSaved : RegistrationActionEvent
}