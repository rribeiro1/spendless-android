package io.rafaelribeiro.spendless.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.data.converter.CryptoTypeConverter
import io.rafaelribeiro.spendless.data.database.SpendLessDatabase
import io.rafaelribeiro.spendless.data.database.TransactionDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideSpendLessDatabase(
        @ApplicationContext context: Context,
        cryptoTypeConverter: CryptoTypeConverter,
    ): SpendLessDatabase {
        return Room.databaseBuilder(context, SpendLessDatabase::class.java, SpendLessDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .addTypeConverter(cryptoTypeConverter)
            .build()
    }

    @Provides
    fun provideTransactionDao(database: SpendLessDatabase): TransactionDao = database.getTransactionDao()

    @Provides
    fun provideCryptoTypeConverter(): CryptoTypeConverter {
        return CryptoTypeConverter()
    }
}
