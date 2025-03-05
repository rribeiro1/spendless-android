package io.rafaelribeiro.spendless.presentation.screens.settings.security

import io.rafaelribeiro.spendless.domain.LockoutDuration
import io.rafaelribeiro.spendless.domain.SessionExpiryDuration

data class SecurityUiState(
    val sessionExpiryDuration: SessionExpiryDuration = SessionExpiryDuration.MINUTES_5,
    val lockoutDuration: LockoutDuration = LockoutDuration.SECONDS_30,
)
