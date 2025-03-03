package io.rafaelribeiro.spendless.presentation.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.data.repository.DefaultExpenseFormatterService
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val authRepository: AuthRepository,
    private val expenseFormatterService: DefaultExpenseFormatterService,
) : ViewModel() {

    private val _uiState: MutableStateFlow<SettingsUiEvent> = MutableStateFlow(SettingsUiEvent())
    val uiState: StateFlow<SettingsUiEvent> = _uiState
        .onStart { loadSettings() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = SettingsUiEvent()
        )

    private fun loadSettings() {

    }


}



data class SettingsUiEvent(
    val isLoading: Boolean = false,
)
