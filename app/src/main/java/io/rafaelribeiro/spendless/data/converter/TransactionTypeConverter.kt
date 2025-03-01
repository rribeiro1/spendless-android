package io.rafaelribeiro.spendless.data.converter

import androidx.room.TypeConverter
import io.rafaelribeiro.spendless.domain.TransactionType

class TransactionTypeConverter {
    @TypeConverter
    fun fromType(type: String): TransactionType = TransactionType.valueOf(type)

    @TypeConverter
    fun toType(type: TransactionType): String = type.name
}
