package io.rafaelribeiro.spendless.presentation.screens.dashboard.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.data.repository.TransactionCreator
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardUiEvent
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardViewModel.Companion.toUiModel
import io.rafaelribeiro.spendless.presentation.screens.dashboard.GroupedTransactions
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LatestTransactions(
    groupedTransactions: List<GroupedTransactions>,
    onEvent: (DashboardUiEvent) -> Unit = {},
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.latest_transactions),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(R.string.show_all),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { onEvent(DashboardUiEvent.ShowAllTransactionsClicked) }
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            groupedTransactions.forEach { group ->
                stickyHeader {
                    Text(
                        text = group.dateHeader.uppercase(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 16.sp,
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                items(group.transactions) { transaction ->
                    DashboardTransactionItem(
                        transaction = transaction,
                        modifier = Modifier,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LatestTransactionsPreview() {
    SpendLessTheme {
        LatestTransactions(
            groupedTransactions = listOf(
                GroupedTransactions(
                    dateHeader = "Today",
                    transactions = TransactionCreator.createTransactions(3).map { it.toUiModel() }
                ),
                GroupedTransactions(
                    dateHeader = "Yesterday",
                    transactions = TransactionCreator.createTransactions(2).map { it.toUiModel() }
                ),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}
