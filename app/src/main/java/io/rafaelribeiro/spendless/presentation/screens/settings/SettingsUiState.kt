package io.rafaelribeiro.spendless.presentation.screens.settings

import io.rafaelribeiro.spendless.core.presentation.UiText

data class SettingsUiState(
    val message: UiText = UiText.Empty,
    val isError: Boolean = false
)