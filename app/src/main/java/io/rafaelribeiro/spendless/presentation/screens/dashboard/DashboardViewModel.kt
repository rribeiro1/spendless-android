package io.rafaelribeiro.spendless.presentation.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.core.presentation.formatDateTime
import io.rafaelribeiro.spendless.core.presentation.timestampToLocalDate
import io.rafaelribeiro.spendless.data.repository.TransactionCreator
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ExpenseFormatter
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import io.rafaelribeiro.spendless.domain.Transaction
import io.rafaelribeiro.spendless.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<DashboardUiState> = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState
        .onStart { subscribeToDashboardData() }
        .stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = DashboardUiState()
        )

    fun onEvent(event: DashboardUiEvent) {
        when (event) {
            is DashboardUiEvent.AddTransactionClicked -> addTransaction() // TODO: Replace with screen to add transaction.
            is DashboardUiEvent.DownloadTransactionsClicked -> TODO()
            is DashboardUiEvent.SettingsClicked -> deleteAllTransactions() // TODO: Replace with settings screen.
            is DashboardUiEvent.TransactionNoteClicked -> showTransactionNote(event.transactionId)
            is DashboardUiEvent.ShowAllTransactionsClicked -> TODO()
        }
    }

    private fun subscribeToDashboardData() {
        getDashboardData()
            .onEach { _uiState.value = it }
            .launchIn(viewModelScope)
    }

    private fun getDashboardData(): Flow<DashboardUiState> {
        return combine(
            transactionRepository.getBalance(),
            transactionRepository.getBiggestTransaction(),
            transactionRepository.getLatestTransactions(),
            transactionRepository.getMostPopularCategory(),
            transactionRepository.getTotalAmountLastWeek()
        ) { balance, largestTransaction, latestTransactions, mostPopularCategory, totalAmountLastWeek ->
            DashboardUiState(
                username = "rafael87", // TODO: Replace with user's name from auth repository.
                accountBalance = balance?.formatExpense() ?: 0.toDouble().formatExpense(),
                previousWeekAmount = totalAmountLastWeek?.formatExpense() ?: 0.toDouble().formatExpense(),
                latestTransactions = groupedTransactions(latestTransactions),
                largestTransaction = largestTransaction?.toUiModel(),
                mostPopularCategory = mostPopularCategory
            )
        }
    }

    private fun groupedTransactions(latestTransactions: List<Transaction>): List<GroupedTransactions> {
        val groupedTransactions = latestTransactions
            .sortedByDescending { it.createdAt }
            .groupBy { it.createdAt.timestampToLocalDate() }
            .map { (date, transactions) ->
                val formattedDate = when (date) {
                    LocalDate.now() -> "Today"
                    LocalDate.now().minusDays(1) -> "Yesterday"
                    else -> date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
                }
                GroupedTransactions(
                    dateHeader = formattedDate,
                    transactions = transactions.map { it.toUiModel() }
                )
            }
        return groupedTransactions
    }

    // TODO: Move to screen to add transaction.
    private fun addTransaction() {
        viewModelScope.launch {
            transactionRepository.saveTransaction(
                TransactionCreator.createTransaction()
            )
        }
    }

    // TODO: We should move this to settings page or debug screen to be able to delete all transactions.
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

    companion object {
        const val WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS = 5000L

        fun Transaction.toUiModel(): TransactionUiModel {
            return TransactionUiModel(
                id = id,
                amount = amount,
                amountDisplay = amount.formatExpense(),
                description = description,
                note = note,
                category = category,
                type = type,
                createdAt = formatDateTime(createdAt),
            )
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
}

sealed interface DashboardUiEvent {
    data object SettingsClicked : DashboardUiEvent
    data object DownloadTransactionsClicked : DashboardUiEvent
    data object AddTransactionClicked : DashboardUiEvent
    data class TransactionNoteClicked(val transactionId: Long) : DashboardUiEvent
    data object ShowAllTransactionsClicked : DashboardUiEvent
}
