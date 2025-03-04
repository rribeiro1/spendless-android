package io.rafaelribeiro.spendless.presentation.screens.settings

import io.rafaelribeiro.spendless.domain.LockoutDuration
import io.rafaelribeiro.spendless.domain.SessionExpiryDuration

data class SettingsUiState(
    val sessionExpiryDuration: SessionExpiryDuration = SessionExpiryDuration.MINUTES_5,
    val lockoutDuration: LockoutDuration = LockoutDuration.SECONDS_30,
)
