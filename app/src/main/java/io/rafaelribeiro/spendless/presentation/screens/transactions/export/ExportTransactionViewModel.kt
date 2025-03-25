package io.rafaelribeiro.spendless.presentation.screens.transactions.export

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportFormat
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportFormat.CSV
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportFormat.PDF
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportRange
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportRange.LAST_THREE_MONTHS
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportRange.CURRENT_MONTH
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportRange.LAST_MONTH
import io.rafaelribeiro.spendless.domain.transaction.TransactionRepository
import io.rafaelribeiro.spendless.service.TransactionExporter
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Named

@HiltViewModel
class ExportTransactionViewModel @Inject constructor(
    @Named("csv") private val csvTransactionExporter: TransactionExporter,
    @Named("pdf") private val pdfTransactionExporter: TransactionExporter,
    @ApplicationContext private val context: Context,
    private val transactionRepository: TransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExportTransactionUiState())
    val uiState: StateFlow<ExportTransactionUiState> = _uiState
        .stateIn(
            scope = viewModelScope,
            initialValue = ExportTransactionUiState(),
            started = SharingStarted.WhileSubscribed(5000L),
        )

    private val _actionEvents = Channel<ExportTransactionActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    fun onEvent(event: ExportTransactionUiEvent) {
        when (event) {
            is ExportTransactionUiEvent.OnRangeSelected -> updateRange(event.range)
            is ExportTransactionUiEvent.OnFormatSelected -> updateFormat(event.format)
            is ExportTransactionUiEvent.OnCancelClicked -> sendActionEvent(ExportTransactionActionEvent.CancelExportCreation)
            is ExportTransactionUiEvent.OnExportClicked -> exportTransactions()
        }
    }

    private fun exportTransactions() {
        viewModelScope.launch {
            try {
                val transactions = when (uiState.value.exportRange) {
                    LAST_THREE_MONTHS -> transactionRepository.getTransactionsFromLastThreeMonths()
                    LAST_MONTH -> transactionRepository.getTransactionsFromLastMonth()
                    CURRENT_MONTH -> transactionRepository.getTransactionsFromCurrentMonth()
                }
                when (uiState.value.exportFormat) {
                    CSV -> csvTransactionExporter.export(transactions, context)
                    PDF -> pdfTransactionExporter.export(transactions, context)
                }
                sendActionEvent(ExportTransactionActionEvent.TransactionExportSuccess)
            } catch (e: Exception) {
                sendActionEvent(ExportTransactionActionEvent.TransactionExportFailed)
            }
        }
    }

    private fun updateState(state: (ExportTransactionUiState) -> ExportTransactionUiState) {
        _uiState.update { state(it) }
    }

    private fun sendActionEvent(actionEvent: ExportTransactionActionEvent) {
        viewModelScope.launch { _actionEvents.send(actionEvent) }
    }

    private fun updateRange(range: TransactionExportRange) {
        updateState { it.copy(exportRange = range) }
    }

    private fun updateFormat(format: TransactionExportFormat) {
        updateState { it.copy(exportFormat = format) }
    }
}