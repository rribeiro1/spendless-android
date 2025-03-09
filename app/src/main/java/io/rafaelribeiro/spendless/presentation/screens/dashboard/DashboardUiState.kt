package io.rafaelribeiro.spendless.presentation.screens.dashboard

import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.domain.transaction.TransactionType

data class DashboardUiState(
    val username: String = "",
    val accountBalance: String = "",
    val previousWeekAmount: String = "",
    val latestTransactions: List<GroupedTransactions> = emptyList(),
    val largestTransaction: TransactionUiModel? = null,
    val mostPopularCategory: TransactionCategory? = null,
)

data class GroupedTransactions(
    val dateHeader: String,
    val transactions: List<TransactionUiModel>,
)

data class TransactionUiModel(
    val id: Long,
    val amount: Double,
    val amountDisplay: String,
    val description: String,
    val note: String? = null,
    val category: TransactionCategory,
    val type: TransactionType,
    val createdAt: String,
    val extended: Boolean = false,
)
