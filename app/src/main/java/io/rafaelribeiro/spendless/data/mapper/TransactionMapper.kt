package io.rafaelribeiro.spendless.data.mapper

import io.rafaelribeiro.spendless.data.entity.TransactionEntity
import io.rafaelribeiro.spendless.domain.transaction.Transaction

fun Transaction.toTransactionEntity(): TransactionEntity = TransactionEntity(
    id = id,
    amount = amount,
    description = description,
    note = note,
    category = category,
    type = type,
    createdAt = createdAt
)

fun TransactionEntity.toTransaction(): Transaction = Transaction(
    id = id,
    amount = amount,
    description = description,
    note = note,
    category = category,
    type = type,
    createdAt = createdAt
)

fun List<TransactionEntity>.toTransactions(): List<Transaction> = map { it.toTransaction() }
