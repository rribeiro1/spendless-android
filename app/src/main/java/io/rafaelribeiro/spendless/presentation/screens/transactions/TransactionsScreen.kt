package io.rafaelribeiro.spendless.presentation.screens.transactions

import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.SpendLessGroupedTransactions
import io.rafaelribeiro.spendless.core.data.TransactionCreator
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.presentation.screens.dashboard.GroupedTransactions
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun TransactionsRootScreen(
    modifier: Modifier,
    uiState: TransactionsUiState,
    onEvent: (TransactionsUiEvent) -> Unit,
    navigationState: NavigationState
) {
    TransactionsScreen(
        uiState = uiState,
        onEvent = onEvent,
        modifier = modifier,
        onBackPress = { navigationState.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    uiState: TransactionsUiState,
    onEvent: (TransactionsUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    onBackPress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.all_transactions),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(TransactionsUiEvent.AddTransactionClicked) },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.offset(y = (-24).dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add a new expense")
            }
        },
    ) { innerPadding ->
        SpendLessGroupedTransactions(
            groupedTransactions = uiState.transactions,
            onShowTransactionNoteClicked = { onEvent(TransactionsUiEvent.TransactionNoteClicked(it)) },
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionsRootScreenPreview() {
    SpendLessTheme {
        TransactionsScreen(
            modifier = Modifier,
            uiState = TransactionsUiState(
                transactions = listOf(
                    GroupedTransactions(
                        dateHeader = "Today",
                        transactions = TransactionCreator.createTransactionUiModels(quantity = 5)
                    )
                )
            ),
            onEvent = {},
            onBackPress = {}
        )
    }
}
