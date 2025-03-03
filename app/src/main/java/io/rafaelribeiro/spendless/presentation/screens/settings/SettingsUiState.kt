package io.rafaelribeiro.spendless.presentation.screens.settings

data class SettingsUiState(
    val isLoading: Boolean = false,
    val sessionExpiryDuration: SessionExpiryDuration = SessionExpiryDuration.MINUTES_15,
    val lockoutDuration: LockoutDuration = LockoutDuration.SECONDS_15,
)
