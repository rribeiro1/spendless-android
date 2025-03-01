package io.rafaelribeiro.spendless.domain

import io.rafaelribeiro.spendless.core.presentation.formatDateTime
import io.rafaelribeiro.spendless.presentation.screens.dashboard.TransactionUiModel
import java.time.Instant

data class Transaction(
    val id: Long,
    val amount: Double,
    val description: String,
    val note: String? = null,
    val category: TransactionCategory,
    val type: TransactionType,
    val createdAt: Long,
)

fun Transaction.toUiModel(): TransactionUiModel {
    return TransactionUiModel(
        id = id,
        amount = amount,
        amountDisplay = amount.toString(),
        description = description,
        note = note,
        category = category,
        type = type,
        createdAt = formatDateTime(createdAt),
    )
}
