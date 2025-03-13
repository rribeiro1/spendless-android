package io.rafaelribeiro.spendless.data.converter

import androidx.room.TypeConverter
import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory

class TransactionCategoryConverter {
    @TypeConverter
    fun fromCategory(category: String): TransactionCategory = TransactionCategory.valueOf(category)

    @TypeConverter
    fun toCategory(category: TransactionCategory): String = category.name
}
