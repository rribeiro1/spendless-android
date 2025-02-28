package io.rafaelribeiro.spendless.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ExpenseFormatter
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import io.rafaelribeiro.spendless.domain.Transaction
import io.rafaelribeiro.spendless.domain.TransactionRepository
import io.rafaelribeiro.spendless.domain.toUiModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {

    companion object {
        private const val WAIT_BEFORE_CONSUMERS_UNSUBSCRIBED = 5000L
    }

    private val _uiState: MutableStateFlow<DashboardUiState> = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState
        .onStart { loadData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(WAIT_BEFORE_CONSUMERS_UNSUBSCRIBED),
            DashboardUiState()
        )

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.AddTransactionClicked -> TODO()
            is DashboardUiEvent.DownloadTransactionsClicked -> TODO()
            is DashboardUiEvent.SettingsClicked -> TODO()
            is DashboardUiEvent.TransactionNoteClicked -> extendTransaction(event.transactionId)
            is DashboardUiEvent.ShowAllTransactionsClicked -> TODO()
        }
    }

    fun loadData() {
        viewModelScope.launch {
            val transactions = transactionRepository.getTransactions().toList()
            val latestTransactions = groupTransactionsByDate(transactions)
            val largestTransaction = transactions.maxByOrNull { it.amount }
            val mostPopularCategory = transactions
                .groupBy { it.category }
                .maxByOrNull { it.value.size }
                ?.key
            updateState { state ->
                state.copy(
                    accountBalance = transactions.sumOf { it.amount }.formatExpense(),
                    previousWeekAmount = transactions.filter { it.date.isAfter(LocalDateTime.now().minusWeeks(1)) }.sumOf { it.amount }.formatExpense(),
                    latestTransactions = latestTransactions.map { group ->
                        group.copy(transactions = group.transactions.map { transaction ->
                            transaction.copy(amountDisplay = transaction.amount.formatExpense())
                        })
                    },
                    largestTransaction = largestTransaction?.copy(amountDisplay = largestTransaction.amount.formatExpense()),
                    mostPopularCategory = mostPopularCategory,
                )
            }
        }
    }

    private fun extendTransaction(transactionId: Int) {
        val currentTransaction = _uiState.value.latestTransactions
            .flatMap { it.transactions }
            .find { it.id == transactionId }
        if (currentTransaction != null) {
            updateState {
                it.copy(latestTransactions = it.latestTransactions.map { group ->
                    group.copy(transactions = group.transactions.map { transaction ->
                        if (transaction.id == transactionId) {
                            transaction.copy(extended = !transaction.extended)
                        } else {
                            transaction
                        }
                    })
                })
            }
        }
    }

    private fun groupTransactionsByDate(transactions: List<Transaction>): List<GroupedTransactions> = transactions
        .sortedByDescending { it.date }
        .groupBy { it.date.toLocalDate() }
        .map { (date, transactions) ->
            val formattedDate = when (date) {
                LocalDate.now() -> "Today"
                LocalDate.now().minusDays(1) -> "Yesterday"
                else -> date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            }
            GroupedTransactions(formattedDate, transactions.map { it.toUiModel() })
        }

    private fun updateState(state: (DashboardUiState) -> DashboardUiState) {
        _uiState.update { state(it) }
    }

    /**
     * Formats the expense amount according to the user's preferences.
     * TODO: The preferences should come from the user's preferences repository.
     */
    private fun Double.formatExpense(): String {
        val formatter = ExpenseFormatter(
            decimalSeparator = DecimalSeparator.DOT,
            thousandSeparator = ThousandSeparator.COMMA,
            currencySymbol = CurrencySymbol.DOLLAR,
            expensesFormat = ExpenseFormat.NEGATIVE
        )
        return formatter.format(this)
    }
}

sealed interface DashboardUiEvent {
    data object SettingsClicked : DashboardUiEvent
    data object DownloadTransactionsClicked : DashboardUiEvent
    data object AddTransactionClicked : DashboardUiEvent
    data class TransactionNoteClicked(val transactionId: Int) : DashboardUiEvent
    data object ShowAllTransactionsClicked : DashboardUiEvent
}
