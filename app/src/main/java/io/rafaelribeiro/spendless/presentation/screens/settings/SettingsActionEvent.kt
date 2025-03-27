package io.rafaelribeiro.spendless.presentation.screens.settings

sealed interface SettingsActionEvent {
    data object OnPreferencesClicked: SettingsActionEvent
    data object OnSecurityClicked: SettingsActionEvent
    data object OnLogoutClicked: SettingsActionEvent
    data object OnAccountClicked: SettingsActionEvent
    data object OnBackClicked: SettingsActionEvent
}