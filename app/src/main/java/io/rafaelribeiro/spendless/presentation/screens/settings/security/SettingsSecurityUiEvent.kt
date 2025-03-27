package io.rafaelribeiro.spendless.presentation.screens.settings.security

import io.rafaelribeiro.spendless.domain.preferences.Biometrics
import io.rafaelribeiro.spendless.domain.preferences.LockoutDuration
import io.rafaelribeiro.spendless.domain.preferences.SessionExpiryDuration

sealed interface SettingsSecurityUiEvent {
    data object SaveClicked : SettingsSecurityUiEvent
    data class SessionExpiryDurationSelected(val duration: SessionExpiryDuration) : SettingsSecurityUiEvent
    data class LockedOutDurationSelected(val duration: LockoutDuration) : SettingsSecurityUiEvent
    data class BiometricsSelected(val biometrics: Biometrics) : SettingsSecurityUiEvent
}