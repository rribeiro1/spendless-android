package io.rafaelribeiro.spendless.presentation.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun DashboardEmptyTransactions(modifier: Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
    ) {
        Text(
            text = "\uD83D\uDCB8",
            fontSize = 96.sp,
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "No transactions to show",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 360)
@Composable
fun EmptyTransactionsScreenPreview() {
    SpendLessTheme {
        DashboardEmptyTransactions(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 8.dp, end = 8.dp, top = 8.dp)
        )
    }
}
