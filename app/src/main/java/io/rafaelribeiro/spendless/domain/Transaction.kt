package io.rafaelribeiro.spendless.domain

import io.rafaelribeiro.spendless.core.presentation.timestampToLocalDate
import io.rafaelribeiro.spendless.data.repository.UserPreferences
import io.rafaelribeiro.spendless.presentation.screens.dashboard.GroupedTransactions
import io.rafaelribeiro.spendless.presentation.screens.dashboard.TransactionUiModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class Transaction(
    val id: Long,
    val amount: Double,
    val description: String,
    val note: String? = null,
    val category: TransactionCategory,
    val type: TransactionType,
    val createdAt: Long,
)

fun Transaction.toUIModel(
    formatter: TransactionFormatter,
    preferences: UserPreferences
): TransactionUiModel {
    return TransactionUiModel(
        id = id,
        amount = amount,
        amountDisplay = formatter.formatAmount(amount, preferences),
        description = description,
        note = note,
        category = category,
        type = type,
        createdAt = formatter.formatDateTime(createdAt),
    )
}

fun List<Transaction>.toGroupedTransactions(
    formatter: TransactionFormatter,
    preferences: UserPreferences
): List<GroupedTransactions> {
    return this
        .sortedByDescending { it.createdAt }
        .groupBy { it.createdAt.timestampToLocalDate() }
        .map { (date, transactions) ->
            val formattedDate = when (date) {
                LocalDate.now() -> "Today"
                LocalDate.now().minusDays(1) -> "Yesterday"
                else -> date.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
            }
            GroupedTransactions(
                dateHeader = formattedDate,
                transactions = transactions.map { it.toUIModel(formatter, preferences) }
            )
        }
}
