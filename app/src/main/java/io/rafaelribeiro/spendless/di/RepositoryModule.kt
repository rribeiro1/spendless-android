package io.rafaelribeiro.spendless.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.data.repository.OfflineAuthRepository
import io.rafaelribeiro.spendless.data.repository.OfflineTransactionRepository
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.transaction.TransactionRepository
import io.rafaelribeiro.spendless.data.repository.DataStoreUserPreferencesRepository
import io.rafaelribeiro.spendless.data.repository.OfflinePinLockTickerRepository
import io.rafaelribeiro.spendless.data.repository.OfflineUserSessionRepository
import io.rafaelribeiro.spendless.domain.PinLockTickerRepository
import io.rafaelribeiro.spendless.domain.UserSessionRepository
import io.rafaelribeiro.spendless.domain.user.UserPreferencesRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
	@Binds
	abstract fun bindAuthRepository(impl: OfflineAuthRepository): AuthRepository

    @Binds
    abstract fun bindTransactionRepository(impl: OfflineTransactionRepository): TransactionRepository

    @Binds
    abstract fun bindUserPreferencesRepository(impl: DataStoreUserPreferencesRepository): UserPreferencesRepository

    @Binds
    abstract fun bindUserSessionRepository(impl: OfflineUserSessionRepository): UserSessionRepository

    @Binds
    abstract fun bindPinLockTickerRepository(impl: OfflinePinLockTickerRepository): PinLockTickerRepository
}
