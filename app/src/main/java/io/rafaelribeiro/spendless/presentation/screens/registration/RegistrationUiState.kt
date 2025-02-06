package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.UiText.Companion.Empty

data class RegistrationUiState(
	val username: String = "",
	val errorMessage: UiText = Empty,
	val nextButtonEnabled: Boolean = false,
	val registrationStage: RegistrationStage = RegistrationStage.INITIAL,
)

enum class RegistrationStage {
	INITIAL,
	PIN_CREATION,
	PIN_CONFIRMATION,
	PREFERENCES,
}
