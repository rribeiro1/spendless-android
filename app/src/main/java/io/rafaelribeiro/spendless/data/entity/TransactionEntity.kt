package io.rafaelribeiro.spendless.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.domain.TransactionType
import java.time.Instant

@Entity(tableName = "transactions")
data class TransactionEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val amount: Double,
    val description: String,
    val note: String? = null,
    val category: TransactionCategory,
    val type: TransactionType,
    val createdAt: Instant
)
