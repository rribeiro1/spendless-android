package io.rafaelribeiro.spendless.navigation

sealed class Screen(val route: String) {
	data object RegistrationFlow : Screen(REGISTRATION_FLOW)
	data object RegistrationUsername : Screen(REGISTRATION_USERNAME)
	data object RegistrationPinCreation : Screen(REGISTRATION_PIN_CREATION)
	data object RegistrationPinConfirmation : Screen(REGISTRATION_PIN_CONFIRMATION)
	data object RegistrationSetPreferences : Screen(REGISTRATION_SET_PREFERENCES)

	companion object {
		private const val REGISTRATION_FLOW = "registration_flow"
		private const val REGISTRATION_USERNAME = "registration_username_screen"
		private const val REGISTRATION_PIN_CREATION = "registration_pin_creation_screen"
		private const val REGISTRATION_PIN_CONFIRMATION = "registration_pin_confirmation_screen"
		private const val REGISTRATION_SET_PREFERENCES = "registration_set_preferences_screen"
	}
}
