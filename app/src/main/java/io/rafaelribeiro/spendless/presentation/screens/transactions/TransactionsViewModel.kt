package io.rafaelribeiro.spendless.presentation.screens.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.domain.transaction.DefaultTransactionFormatter
import io.rafaelribeiro.spendless.data.repository.OfflineTransactionRepository
import io.rafaelribeiro.spendless.data.repository.UserPreferences
import io.rafaelribeiro.spendless.domain.user.UserPreferencesRepository
import io.rafaelribeiro.spendless.domain.transaction.toGroupedTransactions
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val transactionRepository: OfflineTransactionRepository,
    private val transactionFormatter: DefaultTransactionFormatter,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    companion object {
        const val WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS = 5000L
    }

    private val _uiState: MutableStateFlow<TransactionsUiState> = MutableStateFlow(TransactionsUiState())
    val uiState: StateFlow<TransactionsUiState> = _uiState
        .onStart { subscribeToTransactionsData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = TransactionsUiState()
        )

    private val userPreferences = userPreferencesRepository
        .userPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = UserPreferences()
        )

    private val _actionEvents = Channel<TransactionsActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    fun onEvent(event: TransactionsUiEvent) {
        when (event) {
            is TransactionsUiEvent.TransactionNoteClicked -> showTransactionNote(event.transactionId)
            is TransactionsUiEvent.AddTransactionClicked -> sendActionEvent(TransactionsActionEvent.NavigateToAddTransaction)
            is TransactionsUiEvent.DownloadTransactionsClicked -> sendActionEvent(TransactionsActionEvent.NavigateToDownloadTransaction)
        }
    }

    private fun getTransactionsData(): Flow<TransactionsUiState> {
        return combine(
            userPreferences,
            transactionRepository.getAllTransactions()
        ) { userPreferences, transactions ->
            TransactionsUiState(transactions.toGroupedTransactions(transactionFormatter, userPreferences))
        }
    }

    private fun subscribeToTransactionsData() {
        getTransactionsData()
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    private fun showTransactionNote(transactionId: Long) {
        _uiState.value = _uiState.value.copy(
            transactions = _uiState.value.transactions.map { group ->
                group.copy(transactions = group.transactions.map { transaction ->
                    if (transaction.id == transactionId) {
                        transaction.copy(extended = !transaction.extended)
                    } else {
                        transaction
                    }
                })
            }
        )
    }

    private fun sendActionEvent(actionEvent: TransactionsActionEvent) {
        viewModelScope.launch { _actionEvents.send(actionEvent) }
    }
}
