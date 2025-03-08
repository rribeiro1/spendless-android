package io.rafaelribeiro.spendless.presentation.screens.transactions.create

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.SpendLessButton
import io.rafaelribeiro.spendless.core.presentation.SpendLessDropDown
import io.rafaelribeiro.spendless.core.presentation.SpendLessSegmentedButton
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.domain.TransactionType
import io.rafaelribeiro.spendless.presentation.screens.login.LoginUiEvent
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme
import io.rafaelribeiro.spendless.presentation.theme.Success


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTransactionRootScreen(
    onEvent: (CreateTransactionUiEvent) -> Unit = {},
    uiState: CreateTransactionUiState,
    modifier: Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = { onEvent(CreateTransactionUiEvent.OnCancelClicked) },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = null
    ) {
        CreateTransactionScreen(
            onEvent = onEvent,
            uiState = uiState,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(16.dp)
        )
    }
}

@Composable
fun CreateTransactionScreen(
    onEvent: (CreateTransactionUiEvent) -> Unit = {},
    uiState: CreateTransactionUiState,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.create_transaction),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clickable { onEvent(CreateTransactionUiEvent.OnCancelClicked) }
            )
        }
        SpendLessSegmentedButton(
            options = TransactionType.entries,
            getText = { it.display },
            getLeadingIcon = { it.icon },
            selectedIndex = TransactionType.entries.indexOf(uiState.transaction.transactionType),
            selectedItemColor = MaterialTheme.colorScheme.primary,
            onOptionSelected = {
                onEvent(CreateTransactionUiEvent.OnTransactionTypeSelected(TransactionType.entries[it]))
            },
            modifier = Modifier.padding(top = 24.dp)
        )
        Column(
            modifier = Modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CreateTransactionReceiver(
                description = uiState.transaction.description,
                onReceiverChanged = { onEvent(CreateTransactionUiEvent.OnDescriptionChanged(it)) },
                focusRequester = focusRequester,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
                modifier = Modifier.padding(top = 60.dp)
            )
            CreateTransactionAmount(
                amountDisplay = uiState.transaction.amountDisplay,
                onAmountChanged = { onEvent(CreateTransactionUiEvent.OnAmountChanged(it)) },
                transactionType = uiState.transaction.transactionType,
                currencySymbol = uiState.preferences.transactionCurrency,
                expenseFormat = uiState.preferences.transactionFormat,
                decimalSeparator = uiState.preferences.transactionDecimalSeparator,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) },
                ),
                modifier = Modifier.padding(top = 24.dp)
            )
            CreateTransactionNote(
                note = uiState.transaction.note,
                onNoteChanged = { onEvent(CreateTransactionUiEvent.OnNoteChanged(it)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    },
                ),
                modifier = Modifier.padding(top = 24.dp, start = 56.dp, end = 56.dp)
            )
        }
        SpendLessDropDown(
            itemBackgroundColor = MaterialTheme.colorScheme.secondary,
            values = TransactionCategory.categories,
            getText = { it.displayName },
            getLeadingIcon = { it.emoji },
            onItemSelected = { onEvent(CreateTransactionUiEvent.OnCategorySelected(it)) },
            modifier = Modifier.padding(top = 62.dp)
        )
        SpendLessButton(
            text = stringResource(R.string.create),
            onClick = { onEvent(CreateTransactionUiEvent.OnCreatedClicked) },
            enabled = uiState.transaction.createdButtonEnabled,
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}

@Composable
fun CreateTransactionAmount(
    amountDisplay: String,
    onAmountChanged: (String) -> Unit,
    expenseFormat: ExpenseFormat = ExpenseFormat.PARENTHESES,
    currencySymbol: CurrencySymbol = CurrencySymbol.EURO,
    transactionType: TransactionType = TransactionType.EXPENSE,
    decimalSeparator: DecimalSeparator = DecimalSeparator.DOT,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier
) {
    val amountFieldValue = remember(amountDisplay) {
        TextFieldValue(amountDisplay, selection = TextRange(amountDisplay.length))
    }
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (transactionType == TransactionType.EXPENSE) {
            Text(
                text = if (expenseFormat == ExpenseFormat.NEGATIVE) "-${currencySymbol.symbol}" else "(${currencySymbol.symbol}",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        } else {
            Text(
                text = currencySymbol.symbol,
                style = MaterialTheme.typography.displayMedium,
                color = Success,
                textAlign = TextAlign.Center,
            )
        }
        Box(
            modifier = Modifier
                .width(IntrinsicSize.Min) // Adjust width based on text content.
                .padding(horizontal = 8.dp)
        ) {
            BasicTextField(
                value = amountFieldValue,
                onValueChange = { onAmountChanged(it.text) },
                textStyle = MaterialTheme.typography.displayMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.End
                ),
                decorationBox = { innerTextField ->
                    Box(contentAlignment = Alignment.Center) {
                        if (amountFieldValue.text.isEmpty() || amountFieldValue.text == "0,00" || amountFieldValue.text == "0.00") {
                            Text(
                                text = if (decimalSeparator == DecimalSeparator.COMMA) "00,00" else "00.00",
                                style = MaterialTheme.typography.displayMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                                textAlign = TextAlign.Center,
                            )
                        }
                        innerTextField()
                    }
                },
                keyboardOptions = keyboardOptions,
                keyboardActions = keyboardActions,
            )
        }
        if (transactionType == TransactionType.EXPENSE && expenseFormat == ExpenseFormat.PARENTHESES) {
            Text(
                text = ")",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun CreateTransactionReceiver(
    description: String,
    onReceiverChanged: (String) -> Unit,
    focusRequester: FocusRequester,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = description,
            onValueChange = { onReceiverChanged(it) },
            textStyle = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            ),
            decorationBox = { innerTextField ->
                if (description.isEmpty()) {
                    Text(
                        text = stringResource(R.string.receiver),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center
                    )
                }
                innerTextField()
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )
    }
}

@Composable
fun CreateTransactionNote(
    note: String,
    onNoteChanged: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicTextField(
            value = note,
            onValueChange = { onNoteChanged(it) },
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            ),
            decorationBox = { innerTextField ->
                if (note.isEmpty()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add icon",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(end = 4.dp)
                        )
                        Text(
                            text = "Add Note",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                innerTextField()
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTransactionScreenPreview() {
    SpendLessTheme {
        CreateTransactionScreen(
            onEvent = {},
            uiState = CreateTransactionUiState(
                transaction = TransactionState(
                    amountDisplay = ""
                ),
                preferences = TransactionPreferences(
                    transactionFormat = ExpenseFormat.PARENTHESES,
                )
            ),
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.92f)
                .padding(16.dp)
        )
    }
}
