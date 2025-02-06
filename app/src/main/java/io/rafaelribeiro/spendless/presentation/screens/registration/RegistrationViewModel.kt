package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.asUiText
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.Result
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel
	@Inject
	constructor(
		private val authRepository: AuthRepository,
	) : ViewModel() {
		private val _uiState: MutableStateFlow<RegistrationUiState> = MutableStateFlow(RegistrationUiState())
		val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

		private val _actionEvent = Channel<RegistrationActionEvent>()
		val actionEvent = _actionEvent.receiveAsFlow()

		companion object {
			const val ERROR_MESSAGE_DURATION = 2000L
		}

		fun checkUserName(username: String) {
			viewModelScope.launch {
				disableNextButton()
				when (val result = authRepository.checkUserName(username)) {
					is Result.Success -> {
						updateStage(RegistrationStage.PIN_CREATION)
					}
					is Result.Failure -> {
						showErrorMessage(result.error.asUiText())
					}
				}
			}
		}

		fun usernameChanged(username: String) {
			if (username.length <= 14) {
				updateUsername(username)
				if (username.length >= 3) {
					enableNextButton()
				} else {
					disableNextButton()
				}
			}
		}

		private fun showErrorMessage(text: UiText) {
			_uiState.update { it.copy(errorMessage = text) }
			viewModelScope.launch {
				delay(ERROR_MESSAGE_DURATION)
				_uiState.update { it.copy(errorMessage = UiText.Empty) }
			}
		}

		private fun enableNextButton() {
			_uiState.update { it.copy(nextButtonEnabled = true) }
		}

		private fun disableNextButton() {
			_uiState.update { it.copy(nextButtonEnabled = false) }
		}

		private fun updateUsername(username: String) {
			_uiState.update { it.copy(username = username) }
		}

		private fun updateStage(stage: RegistrationStage) {
			_uiState.update { it.copy(registrationStage = stage) }
		}

		private fun sendActionEvent(actionEvent: RegistrationActionEvent) {
			viewModelScope.launch { _actionEvent.send(actionEvent) }
		}
	}

sealed interface RegistrationActionEvent {
	data object RegistrationComplete : RegistrationActionEvent
}
