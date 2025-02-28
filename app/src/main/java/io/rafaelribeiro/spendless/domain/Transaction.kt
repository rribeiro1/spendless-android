package io.rafaelribeiro.spendless.domain

data class Transaction(
    val id: Int,
    val amount: Double,
    val amountDisplay: String,
    val description: String,
    val note: String? = null,
    val category: TransactionCategory,
    val type: TransactionType,
    val date: String,
)
