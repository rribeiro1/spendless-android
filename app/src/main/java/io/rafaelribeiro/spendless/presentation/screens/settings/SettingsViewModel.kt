package io.rafaelribeiro.spendless.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.domain.LockoutDuration
import io.rafaelribeiro.spendless.domain.SessionExpiryDuration
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnBackClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnLogoutClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnPreferencesClicked
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent.OnSecurityClicked
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {

    private val _actionEvents = Channel<SettingsActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()


    fun onEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.PreferencesClicked -> sendActionEvent(OnPreferencesClicked)
            is SettingsUiEvent.SecurityClicked -> sendActionEvent(OnSecurityClicked)
            is SettingsUiEvent.LogoutClicked -> sendActionEvent(OnLogoutClicked) // TODO: should we clear UserPreferences?
            is SettingsUiEvent.BackClicked -> sendActionEvent(OnBackClicked)
        }

    }

    private fun sendActionEvent(event: SettingsActionEvent) {
        viewModelScope.launch {
            _actionEvents.send(event)
        }
    }

}

sealed interface SettingsActionEvent {
    data object OnPreferencesClicked: SettingsActionEvent
    data object OnSecurityClicked: SettingsActionEvent
    data object OnLogoutClicked: SettingsActionEvent
    data object OnBackClicked: SettingsActionEvent
}

sealed interface SettingsUiEvent {
    data object PreferencesClicked: SettingsUiEvent
    data object SecurityClicked: SettingsUiEvent
    data object LogoutClicked: SettingsUiEvent
    data object BackClicked: SettingsUiEvent
}
