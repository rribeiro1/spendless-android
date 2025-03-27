package io.rafaelribeiro.spendless.data.mapper

import io.rafaelribeiro.spendless.data.crypto.Crypto
import io.rafaelribeiro.spendless.data.crypto.EncryptedDouble
import io.rafaelribeiro.spendless.data.crypto.EncryptedString
import io.rafaelribeiro.spendless.data.entity.TransactionEntity
import io.rafaelribeiro.spendless.domain.transaction.Transaction

fun Transaction.toTransactionEntity(): TransactionEntity = TransactionEntity(
    id = id,
    amount = EncryptedDouble(Crypto.encrypt(amount.toString().toByteArray())),
    description = EncryptedString(Crypto.encrypt(description.toByteArray())),
    note = note?.let { EncryptedString(Crypto.encrypt(it.toByteArray())) },
    category = category,
    type = type,
    recurrence = recurrence,
    createdAt = createdAt
)

fun TransactionEntity.toTransaction(): Transaction = Transaction(
    id = id,
    amount = String(Crypto.decrypt(amount.value)).toDouble(),
    description = String(Crypto.decrypt(description.value)),
    note = note?.let { String(Crypto.decrypt(it.value)) },
    category = category,
    type = type,
    recurrence = recurrence,
    createdAt = createdAt
)

fun List<TransactionEntity>.toTransactions(): List<Transaction> = map { it.toTransaction() }
