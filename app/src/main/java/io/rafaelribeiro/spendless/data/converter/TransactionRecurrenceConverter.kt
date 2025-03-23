package io.rafaelribeiro.spendless.data.converter

import androidx.room.TypeConverter
import io.rafaelribeiro.spendless.domain.transaction.TransactionRecurrenceType

class TransactionRecurrenceConverter {
    @TypeConverter
    fun fromRecurrenceType(recurrence: String): TransactionRecurrenceType {
        return TransactionRecurrenceType.valueOf(recurrence)
    }

    @TypeConverter
    fun toRecurrenceType(recurrence: TransactionRecurrenceType): String {
        return recurrence.name
    }
}