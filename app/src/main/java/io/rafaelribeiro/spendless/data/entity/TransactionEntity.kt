package io.rafaelribeiro.spendless.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.domain.transaction.TransactionRecurrenceType
import io.rafaelribeiro.spendless.domain.transaction.TransactionType

@Entity(tableName = "transactions")
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val amount: Double,
    val description: String,
    val note: String? = null,
    val category: TransactionCategory,
    val type: TransactionType,
    val recurrence: TransactionRecurrenceType,
    val createdAt: Long
)
