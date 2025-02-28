package io.rafaelribeiro.spendless.presentation.screens.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.formatDateTime
import io.rafaelribeiro.spendless.data.TransactionCreator
import io.rafaelribeiro.spendless.domain.Transaction
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.domain.TransactionType
import io.rafaelribeiro.spendless.domain.toUiModel
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme
import io.rafaelribeiro.spendless.presentation.theme.Success

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
                mostPopularCategory = uiState.mostPopularCategory,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )
            DashboardLatestTransactions(
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
        if (mostPopularCategory != null) {
            MostPopularCategory(
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
fun MostPopularCategory(
    modifier: Modifier,
    category: TransactionCategory,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 16.dp)
            .height(72.dp)
            .background(
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f),
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                Text(
                    text = category.emoji,
                    fontSize = 24.sp,
                )
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
                Text(
                    text = stringResource(R.string.most_popular_category),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
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
                        .padding(end = 12.dp)
                ) {
                    Text(
                        text = transaction.amountDisplay,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                    )
                    Text(
                        text = formatDateTime(transaction.date),
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
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 18.sp
            ),
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
    groupedTransactions: List<GroupedTransactions> = emptyList(),
    onEvent: (DashboardUiEvent) -> Unit = {}
) {
    if (groupedTransactions.isNotEmpty()) {
        LatestTransactions(
            groupedTransactions = groupedTransactions,
            onEvent = onEvent,
            modifier = modifier
        )
    } else {
        EmptyTransactionsScreen(modifier = modifier)
    }
}

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
                    TransactionItem(
                        transaction = transaction,
                        modifier = Modifier,
                        onEvent = onEvent
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: TransactionUiModel,
    modifier: Modifier,
    onEvent: (DashboardUiEvent) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .padding(
                start = if (transaction.extended) 8.dp else 0.dp,
                end = if (transaction.extended) 8.dp else 0.dp,
                bottom = if (transaction.extended) 4.dp else 0.dp,
                top = 0.dp,
            )
            .fillMaxWidth()
            .shadow(
                elevation = if (transaction.extended) 4.dp else 0.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = DefaultShadowColor.copy(
                    red = 24 / 255f,
                    green = 0 / 255f,
                    blue = 64 / 255f,
                    alpha = if (transaction.extended) 0.4f else 0f
                ),
            )
            .background(if (transaction.extended) MaterialTheme.colorScheme.onPrimary else Color.Transparent)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = if (transaction.extended) 4.dp else 12.dp,
                        vertical = if (transaction.extended) 4.dp else 6.dp
                    )
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
                                .background(MaterialTheme.colorScheme.onPrimary)
                                .clickable { onEvent(DashboardUiEvent.TransactionNoteClicked(transaction.id)) },
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
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
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
                    color = if (transaction.type == TransactionType.INCOME) Success else MaterialTheme.colorScheme.onSurface,
                    modifier = modifier.padding(end = 4.dp)
                )
            }
            if (transaction.extended && transaction.note != null) {
                Text(
                    text = transaction.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 60.dp, end = 12.dp, bottom = 12.dp)
                )
            }
        }

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
                previousWeekAmount = "-$12,450.00",
                latestTransactions = listOf(
                    GroupedTransactions(
                        dateHeader = "Today",
                        transactions = TransactionCreator.createTransactions(3).map { it.toUiModel() }
                    ),
                    GroupedTransactions(
                        dateHeader = "Yesterday",
                        transactions = TransactionCreator.createTransactions(2).map { it.toUiModel() }
                    ),
                    GroupedTransactions(
                        dateHeader = "01 Jan 2022",
                        transactions = TransactionCreator.createTransactions(1).map { it.toUiModel() }
                    ),
                ),
                largestTransaction = TransactionCreator.createTransaction(),
                mostPopularCategory = TransactionCategory.FOOD
            ),
            onEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    SpendLessTheme {
        TransactionItem(
            transaction = TransactionCreator.createTransaction().toUiModel(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionSelectedItemPreview() {
    SpendLessTheme {
        TransactionItem(
            transaction = TransactionCreator.createTransaction().toUiModel(),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
