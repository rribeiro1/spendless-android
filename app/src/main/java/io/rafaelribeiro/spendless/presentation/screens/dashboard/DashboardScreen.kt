package io.rafaelribeiro.spendless.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.core.data.TransactionCreator
import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.presentation.screens.dashboard.components.DashboardBalance
import io.rafaelribeiro.spendless.presentation.screens.dashboard.components.DashboardEmptyTransactions
import io.rafaelribeiro.spendless.presentation.screens.dashboard.components.DashboardLargestTransaction
import io.rafaelribeiro.spendless.presentation.screens.dashboard.components.DashboardLatestTransactions
import io.rafaelribeiro.spendless.presentation.screens.dashboard.components.DashboardMostPopularCategory
import io.rafaelribeiro.spendless.presentation.screens.dashboard.components.DashboardPreviousWeek
import io.rafaelribeiro.spendless.presentation.screens.dashboard.components.DashboardTopBar
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun DashboardScreen(
    uiState: DashboardUiState,
    modifier: Modifier,
    onEvent: (DashboardUiEvent) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        modifier = modifier
            .background(
                brush = Brush.radialGradient(
                    center = Offset(300f, 0f),
                    radius = 1000f,
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.tertiary,
                    ),
                )
            ),
        topBar = {
            DashboardTopBar(
                username = uiState.username,
                onSettingsClick = { onEvent(DashboardUiEvent.SettingsClicked) },
                onDownloadClick = { onEvent(DashboardUiEvent.DownloadTransactionsClicked) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(DashboardUiEvent.AddTransactionClicked) },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.offset(y = (-24).dp)
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add a new expense")
            }
        },
        contentWindowInsets = WindowInsets.captionBar
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            DashboardSummaryScreen(
                accountBalance = uiState.accountBalance,
                previousWeekAmount = uiState.previousWeekAmount,
                largestTransaction = uiState.largestTransaction,
                mostPopularCategory = uiState.mostPopularCategory,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            DashboardLatestTransactionsScreen(
                groupedTransactions = uiState.latestTransactions,
                onEvent = onEvent,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
fun DashboardSummaryScreen(
    accountBalance: String,
    previousWeekAmount: String,
    mostPopularCategory: TransactionCategory? = null,
    largestTransaction: TransactionUiModel? = null,
    modifier: Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DashboardBalance(
            accountBalance = accountBalance,
            modifier = modifier
        )
        if (mostPopularCategory != null) {
            DashboardMostPopularCategory(
                category = mostPopularCategory,
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxWidth()
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DashboardLargestTransaction(
                transaction = largestTransaction,
                modifier = Modifier.weight(1f)
            )
            DashboardPreviousWeek(
                amountPreviousWeekDisplay = previousWeekAmount,
                modifier = Modifier.weight(0.6f)
            )
        }
    }
}

@Composable
fun DashboardLatestTransactionsScreen(
    modifier: Modifier,
    groupedTransactions: List<GroupedTransactions> = emptyList(),
    onEvent: (DashboardUiEvent) -> Unit = {}
) {
    if (groupedTransactions.isNotEmpty()) {
        DashboardLatestTransactions(
            groupedTransactions = groupedTransactions,
            onEvent = onEvent,
            modifier = modifier
        )
    } else {
        DashboardEmptyTransactions(modifier = modifier)
    }
}

@Preview
@Composable
fun DashboardEmptyScreenPreview() {
    SpendLessTheme {
        DashboardScreen(
            modifier = Modifier,
            uiState = DashboardUiState(
                username = "rockefeller74",
                accountBalance = "$0.00",
                previousWeekAmount = "$0",
                largestTransaction = null
            ),
            onEvent = {}
        )
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    SpendLessTheme {
        DashboardScreen(
            modifier = Modifier,
            uiState = DashboardUiState(
                username = "rockefeller74",
                accountBalance = "$1,000.00",
                previousWeekAmount = "-$12,450.00",
                latestTransactions = listOf(
                    GroupedTransactions(
                        dateHeader = "Today",
                        transactions = TransactionCreator.createTransactionUiModels(quantity = 5)
                    ),
                    GroupedTransactions(
                        dateHeader = "Yesterday",
                        transactions = TransactionCreator.createTransactionUiModels(quantity = 2)
                    ),
                    GroupedTransactions(
                        dateHeader = "01 Jan 2022",
                        transactions = TransactionCreator.createTransactionUiModels(quantity = 1)
                    ),
                ),
                largestTransaction = TransactionCreator.createTransactionUiModel(),
                mostPopularCategory = TransactionCategory.FOOD
            ),
            onEvent = {}
        )
    }
}
