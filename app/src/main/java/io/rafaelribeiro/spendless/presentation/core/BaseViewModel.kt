package io.rafaelribeiro.spendless.presentation.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<S : UiState> : ViewModel() {
	protected abstract val initialState: S

	private val _uiState: MutableStateFlow<S> by lazy { MutableStateFlow(initialState) }
	val uiState: StateFlow<S> = _uiState.asStateFlow()

	protected val currentState: S
		get() = uiState.value

	protected fun updateState(block: (currentState: S) -> S) {
		_uiState.update(block)
	}
}
