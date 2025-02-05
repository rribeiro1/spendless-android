package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.Result
import io.rafaelribeiro.spendless.presentation.core.BaseViewModel
import io.rafaelribeiro.spendless.utils.UiText
import io.rafaelribeiro.spendless.utils.asUiText
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegistrationViewModel
	@Inject
	constructor(
		private val authRepository: AuthRepository,
	) : BaseViewModel<RegistrationUiState>() {
		override val initialState: RegistrationUiState
			get() = RegistrationUiState()

		fun exists(username: String) {
			viewModelScope.launch {
				when (val result = authRepository.exists(username)) {
					is Result.Success -> {}
					is Result.Failure -> {
						val errorMessage = result.error.asUiText()
						updateState { it.copy(errorMessage = errorMessage) }
					}
				}
			}
		}

		fun userMessageShown() {
			updateState { it.copy(errorMessage = UiText.Empty) }
		}
	}
