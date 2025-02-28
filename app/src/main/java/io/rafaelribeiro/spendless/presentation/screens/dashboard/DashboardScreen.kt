package io.rafaelribeiro.spendless.presentation.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.domain.Transaction
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.domain.TransactionType
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme
import kotlin.random.Random

@Composable
fun DashboardRootScreen(
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
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            DashboardLatestTransactions(
                transactions = uiState.latestTransactions,
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
    largestTransaction: Transaction? = null,
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
fun DashboardBalance(
    modifier: Modifier,
    accountBalance: String,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxHeight()
    ) {
        Text(
            text = accountBalance,
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            text = stringResource(R.string.account_balance),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun DashboardLargestTransaction(
    modifier: Modifier = Modifier,
    transaction: Transaction? = null
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .height(72.dp)
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        if (transaction != null) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = transaction.description,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Clip
                    )
                    Text(
                        text = stringResource(R.string.largest_transaction),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(end = 12.dp)
                ) {
                    Text(
                        text = transaction.amountDisplay,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = transaction.date,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
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

@Composable
fun DashboardPreviousWeek(
    modifier: Modifier,
    amountPreviousWeekDisplay: String,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = amountPreviousWeekDisplay,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = stringResource(R.string.previous_week),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun DashboardLatestTransactions(
    modifier: Modifier,
    transactions: List<Transaction> = emptyList(),
) {
    if (transactions.isNotEmpty()) {
        LatestTransactions(transactions = transactions, modifier = modifier)
    } else {
        EmptyTransactionsScreen(modifier = modifier)
    }
}

@Composable
fun LatestTransactions(
    transactions: List<Transaction>,
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
                color = MaterialTheme.colorScheme.primary
            )
        }
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            items(transactions) { transaction ->
                TransactionItem(transaction = transaction, modifier = modifier)
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(44.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (transaction.type == TransactionType.EXPENSE) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            ) {
                Text(
                    text = transaction.category.emoji,
                    fontSize = 18.sp,
                )
            }

            if (transaction.note != null) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(MaterialTheme.colorScheme.onPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Segment,
                        contentDescription = "Note attached",
                        tint = if (transaction.type == TransactionType.EXPENSE) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.surfaceDim,
                        modifier = Modifier
                            .size(14.dp)
                            .graphicsLayer(scaleX = -1f)
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 12.dp).weight(1f)
        ) {
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = transaction.category.displayName,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                ),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        Spacer(modifier = Modifier.weight(0.1f))
        Text(
            text = transaction.amountDisplay,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun EmptyTransactionsScreen(modifier: Modifier) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    username: String,
    onSettingsClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Transparent,
        ),
        actions = {
            IconButton(
                onClick = { onDownloadClick() },
                modifier = Modifier
                    .padding(end = 14.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Download transactions"
                )
            }
            IconButton(
                onClick = { onSettingsClick() },
                modifier = Modifier
                    .padding(end = 14.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Go to settings"
                )
            }
        }
    )
}

@Preview
@Composable
fun DashboardEmptyScreenPreview() {
    SpendLessTheme {
        DashboardRootScreen(
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
        DashboardRootScreen(
            modifier = Modifier,
            uiState = DashboardUiState(
                username = "rockefeller74",
                accountBalance = "$1,000.00",
                previousWeekAmount = "$500",
                latestTransactions = TransactionCreator.createTransactions(10),
                largestTransaction = TransactionCreator.createTransaction()
            ),
            onEvent = {}
        )
    }
}

class TransactionCreator {
    companion object {
        fun createTransactions(quantity: Int): List<Transaction> {
            return List(quantity) {
                createTransaction()
            }
        }

        fun createTransaction(): Transaction {
            val (description, category, type) = randomTransaction()
            return Transaction(
                id = Random.nextInt(),
                amount = 100.00,
                amountDisplay = listOf("$100.00", "-$50.00", "-$10.00", "-$12,492.50").random(),
                description = description,
                note = listOf("This is a note for $description", null).random(),
                category = category,
                type = type,
                date = listOf("2025-01-01", "2025-02-27", "2025-02-28").random()
            )
        }

        private fun randomTransaction(): Triple<String, TransactionCategory, TransactionType> {
            val descriptions = listOf(
                Triple("Amazon", TransactionCategory.HOME, TransactionType.EXPENSE),
                Triple("McDonald's", TransactionCategory.FOOD, TransactionType.EXPENSE),
                Triple("Netflix Monthly Subscription from Brazil", TransactionCategory.ENTERTAINMENT, TransactionType.EXPENSE),
                Triple("Zara", TransactionCategory.CLOTHING, TransactionType.EXPENSE),
                Triple("Gym - Monthly Membership John Reed", TransactionCategory.HEALTH, TransactionType.EXPENSE),
                Triple("Haircut", TransactionCategory.PERSONAL_CARE, TransactionType.EXPENSE),
                Triple("Uber", TransactionCategory.TRANSPORTATION, TransactionType.EXPENSE),
                Triple("Udemy", TransactionCategory.EDUCATION, TransactionType.EXPENSE),
                Triple("Rick's share - Birthday Present from Rafael", TransactionCategory.SAVINGS, TransactionType.INCOME),
            )
            return descriptions.random()
        }
    }
}
