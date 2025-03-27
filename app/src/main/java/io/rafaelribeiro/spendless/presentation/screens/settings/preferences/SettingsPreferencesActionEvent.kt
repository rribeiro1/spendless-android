package io.rafaelribeiro.spendless.presentation.screens.settings.preferences

sealed interface SettingsPreferencesActionEvent {
    data object OnBackClicked : SettingsPreferencesActionEvent
}