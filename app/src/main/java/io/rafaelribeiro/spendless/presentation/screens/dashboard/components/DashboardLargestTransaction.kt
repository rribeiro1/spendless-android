package io.rafaelribeiro.spendless.presentation.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.data.TransactionCreator
import io.rafaelribeiro.spendless.core.presentation.AutoResizingText
import io.rafaelribeiro.spendless.presentation.screens.dashboard.TransactionUiModel
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun DashboardLargestTransaction(
    modifier: Modifier = Modifier,
    transaction: TransactionUiModel? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(72.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        if (transaction != null) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                AutoResizingText(
                    text = transaction.description,
                    maxFontSize = 18.sp,
                    minFontSize = 12.sp,
                    textOverflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                )
                AutoResizingText(
                    text = stringResource(R.string.largest_transaction),
                    maxFontSize = 11.sp,
                    minFontSize = 8.sp,
                    textOverflow = TextOverflow.Visible,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                AutoResizingText(
                    text = transaction.amountDisplay,
                    textOverflow = TextOverflow.Visible,
                    maxFontSize = 18.sp,
                    minFontSize = 12.sp,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                )
                AutoResizingText(
                    text = transaction.createdAt,
                    maxFontSize = 11.sp,
                    minFontSize = 8.sp,
                    textOverflow = TextOverflow.Visible,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.largest_transaction_description),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 200)
@Composable
fun DashboardLargestTransactionPreview() {
    SpendLessTheme {
        DashboardLargestTransaction(
            transaction = TransactionCreator.createTransactionUiModel()
        )
    }
}

@Preview
@Composable
fun DashboardLargestTransactionEmptyPreview() {
    SpendLessTheme {
        DashboardLargestTransaction()
    }
}
