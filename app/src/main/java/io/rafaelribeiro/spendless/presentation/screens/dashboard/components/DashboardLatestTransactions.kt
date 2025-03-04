package io.rafaelribeiro.spendless.presentation.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.SpendLessGroupedTransactions
import io.rafaelribeiro.spendless.core.data.TransactionCreator
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardUiEvent
import io.rafaelribeiro.spendless.presentation.screens.dashboard.GroupedTransactions
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun DashboardLatestTransactions(
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
        SpendLessGroupedTransactions(
            groupedTransactions = groupedTransactions,
            onShowTransactionNoteClicked = { onEvent(DashboardUiEvent.TransactionNoteClicked(it)) }
        )
    }
}

@Preview
@Composable
fun LatestTransactionsPreview() {
    SpendLessTheme {
        DashboardLatestTransactions(
            groupedTransactions = listOf(
                GroupedTransactions(
                    dateHeader = "Today",
                    transactions = TransactionCreator.createTransactionUiModels(quantity = 3)
                ),
                GroupedTransactions(
                    dateHeader = "Yesterday",
                    transactions = TransactionCreator.createTransactionUiModels(quantity = 5)
                ),
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
    }
}
