package io.rafaelribeiro.spendless.presentation.screens.transactions.export

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportFormat
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportRange
import javax.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ExportTransactionViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ExportTransactionUiState())
    val uiState = _uiState.asStateFlow()

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
        // TODO: Implement transaction export
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