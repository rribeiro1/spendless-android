package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty

data class RegistrationUiState(
	val username: String = "",
	val errorMessage: UiText = Empty,
	val pin: String = "",
	val pinConfirmation: String = "",
	val nextButtonEnabled: Boolean = false,
)
