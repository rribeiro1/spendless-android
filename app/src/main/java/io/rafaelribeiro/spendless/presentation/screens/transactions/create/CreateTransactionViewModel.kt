package io.rafaelribeiro.spendless.presentation.screens.transactions.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.data.repository.DefaultTransactionFormatter
import io.rafaelribeiro.spendless.data.repository.OfflineTransactionRepository
import io.rafaelribeiro.spendless.data.repository.UserPreferences
import io.rafaelribeiro.spendless.domain.transaction.Transaction
import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.domain.transaction.TransactionType
import io.rafaelribeiro.spendless.domain.user.UserPreferencesRepository
import io.rafaelribeiro.spendless.presentation.screens.transactions.TransactionsViewModel.Companion.WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTransactionViewModel @Inject constructor(
    private val transactionRepository: OfflineTransactionRepository,
    private val transactionFormatter: DefaultTransactionFormatter,
    val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(CreateTransactionUiState())
    val uiState: StateFlow<CreateTransactionUiState> = _uiState
        .onStart { subscribeToUserPreferences() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(WAIT_UNTIL_NO_CONSUMERS_IN_MILLIS),
            initialValue = CreateTransactionUiState()
        )

    private val _actionEvents = Channel<CreateTransactionActionEvent>()
    val actionEvents = _actionEvents.receiveAsFlow()

    private fun subscribeToUserPreferences() {
        viewModelScope.launch {
            userPreferencesRepository
                .userPreferences
                .onEach { preferences -> updatePreferencesState { it.fromUserPreferences(preferences) } }
                .launchIn(viewModelScope)
        }
    }

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
        viewModelScope.launch {
            try {
                val transaction = createTransaction()
                transactionRepository.saveTransaction(transaction)
                sendActionEvent(CreateTransactionActionEvent.TransactionCreated)
            } catch (e: Exception) {
                Log.e(LOG_TAG, "Error saving transaction", e)
                showErrorMessage(UiText.StringResource(R.string.created_transaction_failed))
            }
        }
    }

    private fun createTransaction(): Transaction {
        val transactionState = uiState.value.transaction
        val amount = transactionState.amountDisplay.toSignedAmount(uiState.value.transaction.transactionType)
        return Transaction(
            type = transactionState.transactionType,
            amount = amount,
            description = transactionState.description,
            note = transactionState.note,
            category = transactionState.category,
            createdAt = System.currentTimeMillis()
        )
    }

    private fun String.toAmount(): Double {
        return this
            .replace(Regex("\\D"), "")
            .ifEmpty { "0" }
            .toDouble() / 100
    }

    private fun String.toSignedAmount(transactionType: TransactionType): Double {
        val amount = this.toAmount()
        return if (transactionType == TransactionType.EXPENSE) -amount else amount
    }

    private fun updateTransactionType(transactionType: TransactionType) {
        updateState { it.copy(transaction = it.transaction.copy(transactionType = transactionType)) }
    }

    private fun updateTransactionCategory(transactionCategory: TransactionCategory) {
        updateState { it.copy(transaction = it.transaction.copy(category = transactionCategory)) }
    }

    private fun updateTransactionNote(transactionNote: String) {
        val note = transactionNote.take(NOTE_MAX_SIZE)
        updateState { it.copy(transaction = it.transaction.copy(note = note)) }
    }

    private fun updateTransactionDescription(transactionDescription: String) {
        val newDescription = transactionDescription.take(DESCRIPTION_MAX_SIZE)
        updateState { it.copy(transaction = it.transaction.copy(description = newDescription)) }
        enableCreateButton()
    }

    private fun updateAmount(amount: String) {
        val convertedAmount = amount
            .take(AMOUNT_DIGITS_MAX_SIZE)
            .toAmount()

        if (convertedAmount <= 0.0) {
            updateState { it.copy(transaction = it.transaction.copy(amountDisplay = "")) }
            return
        }
        val formattedAmount = transactionFormatter.formatAmount(
            convertedAmount,
            UserPreferences(
                currencyName = uiState.value.preferences.currencySymbol.name,
                expensesFormatName = uiState.value.preferences.expensesFormat.name,
                decimalSeparatorName = uiState.value.preferences.decimalSeparator.name,
                thousandsSeparatorName = uiState.value.preferences.thousandSeparator.name
            ),
            excludeExpenseFormat = true
        )
        updateState { it.copy(transaction = it.transaction.copy(amountDisplay = formattedAmount)) }
        enableCreateButton()
    }

    private fun updatePreferencesState(state: (TransactionPreferencesUiState) -> TransactionPreferencesUiState) {
        updateState { it.copy(preferences = state(it.preferences)) }
    }

    private fun enableCreateButton() {
        if (uiState.value.transaction.amountDisplay.isNotEmpty() &&
            uiState.value.transaction.description.length >= 3 &&
            uiState.value.transaction.description.length <= 14) {
            updateState { it.copy(transaction = it.transaction.copy(createdButtonEnabled = true)) }
        } else {
            updateState { it.copy(transaction = it.transaction.copy(createdButtonEnabled = false)) }
        }
    }

    private fun sendActionEvent(actionEvent: CreateTransactionActionEvent) {
        viewModelScope.launch { _actionEvents.send(actionEvent) }
    }

    private fun updateState(state: (CreateTransactionUiState) -> CreateTransactionUiState) {
        _uiState.update { state(it) }
    }

    private fun showErrorMessage(text: UiText) {
        updateState { it.copy(transaction = it.transaction.copy(errorMessage = text)) }
        viewModelScope.launch {
            delay(ERROR_MESSAGE_DURATION)
            updateState { it.copy(transaction = it.transaction.copy(errorMessage = UiText.Empty)) }
        }
    }

    companion object {
        const val LOG_TAG = "CreateTransactionViewModel"
        const val ERROR_MESSAGE_DURATION = 2000L
        const val DESCRIPTION_MAX_SIZE = 14
        const val NOTE_MAX_SIZE = 100
        const val AMOUNT_DIGITS_MAX_SIZE = 10
    }
}

sealed interface CreateTransactionActionEvent {
    data object CancelTransactionCreation : CreateTransactionActionEvent
    data object TransactionCreated : CreateTransactionActionEvent
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
