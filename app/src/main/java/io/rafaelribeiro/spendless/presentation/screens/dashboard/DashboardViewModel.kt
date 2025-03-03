package io.rafaelribeiro.spendless.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.core.presentation.combine
import io.rafaelribeiro.spendless.data.repository.DefaultTransactionFormatter
import io.rafaelribeiro.spendless.data.repository.TransactionCreator
import io.rafaelribeiro.spendless.data.repository.UserPreferences
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.TransactionRepository
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import io.rafaelribeiro.spendless.domain.toGroupedTransactions
import io.rafaelribeiro.spendless.domain.toUIModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val transactionRepository: TransactionRepository,
    private val transactionFormatter: DefaultTransactionFormatter,
    userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    companion object {
        const val WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS = 5000L
    }

    private val _uiState: MutableStateFlow<DashboardUiState> = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState
        .onStart { subscribeToDashboardData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = DashboardUiState()
        )

    private val userPreferences = userPreferencesRepository
        .userPreferences
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = UserPreferences()
        )

    private val _actionEvents = Channel<DashboardActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.AddTransactionClicked -> sendActionEvent(DashboardActionEvent.AddTransaction)
            is DashboardUiEvent.DownloadTransactionsClicked -> addTransaction() // TODO: Replace with download screen.
            is DashboardUiEvent.SettingsClicked -> deleteAllTransactions() // TODO: Replace with settings screen.
            is DashboardUiEvent.TransactionNoteClicked -> showTransactionNote(event.transactionId)
            is DashboardUiEvent.ShowAllTransactionsClicked -> sendActionEvent(DashboardActionEvent.ShowAllTransactions)
        }
    }

    private fun subscribeToDashboardData() {
        getDashboardData()
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    private fun getDashboardData(): Flow<DashboardUiState> {
        return combine(
            userPreferences,
            authRepository.userName,
            transactionRepository.getBalance(),
            transactionRepository.getBiggestTransaction(),
            transactionRepository.getLatestTransactions(),
            transactionRepository.getMostPopularCategory(),
            transactionRepository.getTotalAmountLastWeek()
        ) { preferences, username, balance, largestTransaction, latestTransactions, mostPopularCategory, totalAmountLastWeek ->
            DashboardUiState(
                username = username,
                accountBalance = transactionFormatter.formatAmount(balance ?: 0.0, preferences),
                previousWeekAmount = transactionFormatter.formatAmount(totalAmountLastWeek ?: 0.0, preferences),
                latestTransactions = latestTransactions.toGroupedTransactions(transactionFormatter, preferences),
                largestTransaction = largestTransaction?.toUIModel(transactionFormatter, preferences),
                mostPopularCategory = mostPopularCategory
            )
        }
    }

    // TODO: Temporary method to add a transaction. This will be replaced with add transaction screen on RIB-29.
    private fun addTransaction() {
        viewModelScope.launch {
            transactionRepository.saveTransaction(
                TransactionCreator.createTransaction()
            )
        }
    }

    // TODO: Temporary method to delete all transactions.
    private fun deleteAllTransactions() {
        viewModelScope.launch {
            transactionRepository.deleteAllTransactions()
        }
    }

    private fun showTransactionNote(transactionId: Long) {
        _uiState.value = _uiState.value.copy(
            latestTransactions = _uiState.value.latestTransactions.map { group ->
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

    private fun sendActionEvent(actionEvent: DashboardActionEvent) {
        viewModelScope.launch { _actionEvents.send(actionEvent) }
    }
}

sealed interface DashboardActionEvent {
    data object ShowAllTransactions : DashboardActionEvent
    data object AddTransaction : DashboardActionEvent
}

sealed interface DashboardUiEvent {
    data object SettingsClicked : DashboardUiEvent
    data object DownloadTransactionsClicked : DashboardUiEvent
    data object AddTransactionClicked : DashboardUiEvent
    data object ShowAllTransactionsClicked : DashboardUiEvent
    data class TransactionNoteClicked(val transactionId: Long) : DashboardUiEvent
}
