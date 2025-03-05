package io.rafaelribeiro.spendless.presentation.screens.login

import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty

data class LoginUiState(
    val isLoading: Boolean = false,
    val errorMessage: UiText = Empty,
    val username: String = "",
    val pin: String = "",
    val loginButtonEnabled: Boolean = false,
    val isUsernameFocused: Boolean = false,
    val isPinFocused: Boolean = false,
)
