package io.rafaelribeiro.spendless.presentation.screens.settings.security

sealed interface SettingsSecurityActionEvent {
    data object OnBackClicked : SettingsSecurityActionEvent
    data object RestartSession : SettingsSecurityActionEvent
}