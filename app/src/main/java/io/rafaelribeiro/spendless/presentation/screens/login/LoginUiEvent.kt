package io.rafaelribeiro.spendless.presentation.screens.login

sealed interface LoginUiEvent {
    data class UsernameChanged(val username: String) : LoginUiEvent
    data class PinChanged(val pin: String) : LoginUiEvent
    data object ActionButtonLoginClicked : LoginUiEvent
    data class UsernameFocusChanged(val isFocused: Boolean) : LoginUiEvent
    data class PinFocusChanged(val isFocused: Boolean) : LoginUiEvent
}