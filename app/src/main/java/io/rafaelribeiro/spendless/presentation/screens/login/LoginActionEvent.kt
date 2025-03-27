package io.rafaelribeiro.spendless.presentation.screens.login

sealed interface LoginActionEvent {
    data class LoginSucceed(val username: String) : LoginActionEvent
}