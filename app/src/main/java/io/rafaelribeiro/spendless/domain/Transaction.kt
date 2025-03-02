package io.rafaelribeiro.spendless.domain

data class Transaction(
    val id: Long,
    val amount: Double,
    val description: String,
    val note: String? = null,
    val category: TransactionCategory,
    val type: TransactionType,
    val createdAt: Long,
)
