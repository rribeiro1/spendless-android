package io.rafaelribeiro.spendless.domain

import io.rafaelribeiro.spendless.core.presentation.formatDateTime
import io.rafaelribeiro.spendless.presentation.screens.dashboard.TransactionUiModel
import java.time.LocalDateTime

data class Transaction(
    val id: Int,
    val amount: Double,
    val amountDisplay: String,
    val description: String,
    val note: String? = null,
    val category: TransactionCategory,
    val type: TransactionType,
    val date: LocalDateTime,
)

fun Transaction.toUiModel(): TransactionUiModel {
    return TransactionUiModel(
        id = id,
        amount = amount,
        amountDisplay = amountDisplay,
        description = description,
        note = note,
        category = category,
        type = type,
        date = formatDateTime(date),
    )
}
