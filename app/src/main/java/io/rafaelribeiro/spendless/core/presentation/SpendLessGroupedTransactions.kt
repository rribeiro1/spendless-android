package io.rafaelribeiro.spendless.core.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rafaelribeiro.spendless.core.data.TransactionCreator
import io.rafaelribeiro.spendless.presentation.screens.dashboard.GroupedTransactions
import io.rafaelribeiro.spendless.presentation.screens.dashboard.components.DashboardTransactionItem
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SpendLessGroupedTransactions(
    groupedTransactions: List<GroupedTransactions>,
    onShowTransactionNoteClicked: (Long) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(bottom = 80.dp),
        modifier = modifier
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
                    onShowTransactionNoteClicked = onShowTransactionNoteClicked
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SpendLessGroupedTransactionsPreview() {
    SpendLessTheme {
        SpendLessGroupedTransactions(
            groupedTransactions = listOf(
                GroupedTransactions(
                    dateHeader = "Today",
                    transactions = TransactionCreator.createTransactionUiModels(quantity = 5)
                )
            )
        )
    }
}
