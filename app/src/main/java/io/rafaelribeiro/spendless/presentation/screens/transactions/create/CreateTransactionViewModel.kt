package io.rafaelribeiro.spendless.presentation.screens.transactions.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.data.repository.OfflineTransactionRepository
import io.rafaelribeiro.spendless.domain.Transaction
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.domain.TransactionType
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
class CreateTransactionViewModel @Inject constructor(
    private val transactionRepository: OfflineTransactionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateTransactionUiState())
    val uiState: StateFlow<CreateTransactionUiState> = _uiState.asStateFlow()

    private val _actionEvents = Channel<CreateTransactionActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    fun onEvent(event: CreateTransactionUiEvent) {
        when (event) {
            is CreateTransactionUiEvent.OnCreatedClicked -> saveTransaction()
            is CreateTransactionUiEvent.OnCancelClicked -> sendActionEvent(CreateTransactionActionEvent.CancelTransactionCreation)
            is CreateTransactionUiEvent.OnTransactionTypeSelected -> updateTransactionType(event.transactionType)
            is CreateTransactionUiEvent.OnCategorySelected -> updateTransactionCategory(event.transactionCategory)
            is CreateTransactionUiEvent.OnNoteChanged -> updateTransactionNote(event.transactionNote)
            is CreateTransactionUiEvent.OnDescriptionChanged -> updateTransactionDescription(event.transactionDescription)
            is CreateTransactionUiEvent.OnAmountChanged -> updateAmount(event.amount)
        }
    }

    private fun saveTransaction() {
        val transaction = Transaction(
            type = uiState.value.transactionType,
            amount = convertAmount(uiState.value.amountDisplay),
            description = uiState.value.description,
            note = uiState.value.note,
            category = uiState.value.category,
            createdAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            transactionRepository.saveTransaction(transaction)
        }
    }

    private fun convertAmount(amountDisplay: String): Double {
        val amount = amountDisplay.replace(Regex("[^\\d]"), "")
        return amount.toDouble() / 100
    }

    private fun updateTransactionType(transactionType: TransactionType) {
        updateState { it.copy(transactionType = transactionType) }
    }

    private fun updateTransactionCategory(transactionCategory: TransactionCategory) {
        updateState { it.copy(category = transactionCategory) }
    }

    private fun updateTransactionNote(transactionNote: String) {
        val note = transactionNote.take(NOTE_MAX_SIZE)
        updateState { it.copy(note = note) }
    }

    private fun updateTransactionDescription(transactionDescription: String) {
        val newDescription = transactionDescription.take(DESCRIPTION_MAX_SIZE)
        updateState { it.copy(description = newDescription) }
        enableCreateButton()
    }

    private fun updateAmount(amount: String) {
        updateState { it.copy(amountDisplay = amount) }
        enableCreateButton()
    }

    private fun enableCreateButton() {
        if (uiState.value.amountDisplay.isNotEmpty() &&
            uiState.value.description.length >= 3 &&
            uiState.value.description.length <= 14) {
            updateState { it.copy(createdButtonEnabled = true) }
        } else {
            updateState { it.copy(createdButtonEnabled = false) }
        }
    }

    private fun sendActionEvent(actionEvent: CreateTransactionActionEvent) {
        viewModelScope.launch { _actionEvents.send(actionEvent) }
    }

    private fun updateState(state: (CreateTransactionUiState) -> CreateTransactionUiState) {
        _uiState.update { state(it) }
    }

    private fun showErrorMessage(text: UiText) {
        updateState { it.copy(errorMessage = text) }
        viewModelScope.launch {
            delay(ERROR_MESSAGE_DURATION)
            updateState { it.copy(errorMessage = UiText.Empty) }
        }
    }

    companion object {
        const val ERROR_MESSAGE_DURATION = 2000L
        const val DESCRIPTION_MAX_SIZE = 14
        const val NOTE_MAX_SIZE = 100
    }
}

sealed interface CreateTransactionActionEvent {
    data object CancelTransactionCreation : CreateTransactionActionEvent
}

sealed interface CreateTransactionUiEvent {
    data object OnCreatedClicked : CreateTransactionUiEvent
    data object OnCancelClicked : CreateTransactionUiEvent
    data class OnTransactionTypeSelected(val transactionType: TransactionType) : CreateTransactionUiEvent
    data class OnCategorySelected(val transactionCategory: TransactionCategory) : CreateTransactionUiEvent
    data class OnNoteChanged(val transactionNote: String) : CreateTransactionUiEvent
    data class OnDescriptionChanged(val transactionDescription: String) : CreateTransactionUiEvent
    data class OnAmountChanged(val amount: String) : CreateTransactionUiEvent
}
