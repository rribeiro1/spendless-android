package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.presentation.core.UiState
import io.rafaelribeiro.spendless.utils.UiText
import io.rafaelribeiro.spendless.utils.UiText.Companion.Empty

data class RegistrationUiState(
	val isLoading: Boolean = false,
	val errorMessage: UiText = Empty,
) : UiState
