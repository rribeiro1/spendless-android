package io.rafaelribeiro.spendless.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import io.rafaelribeiro.spendless.data.converter.CryptoTypeConverter
import io.rafaelribeiro.spendless.data.converter.TransactionCategoryConverter
import io.rafaelribeiro.spendless.data.converter.TransactionRecurrenceConverter
import io.rafaelribeiro.spendless.data.converter.TransactionTypeConverter
import io.rafaelribeiro.spendless.data.entity.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 4, exportSchema = false)
@TypeConverters(
    TransactionCategoryConverter::class,
    TransactionTypeConverter::class,
    TransactionRecurrenceConverter::class,
    CryptoTypeConverter::class,
)
abstract class SpendLessDatabase : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "spendless"
    }
    abstract fun getTransactionDao(): TransactionDao
}
