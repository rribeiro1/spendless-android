package io.rafaelribeiro.spendless.presentation.screens.settings

sealed interface SettingsUiEvent {
    data object PreferencesClicked: SettingsUiEvent
    data object SecurityClicked: SettingsUiEvent
    data object LogoutClicked: SettingsUiEvent
    data object AccountClicked: SettingsUiEvent
    data object BackClicked: SettingsUiEvent
    data object DeleteAccountClicked: SettingsUiEvent
    data object DeleteTransactionsClicked: SettingsUiEvent
}
