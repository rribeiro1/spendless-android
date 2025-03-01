package io.rafaelribeiro.spendless.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.data.repository.OfflineAuthRepository
import io.rafaelribeiro.spendless.data.repository.OfflineTransactionRepository
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.TransactionRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
	@Binds
	abstract fun bindAuthRepository(impl: OfflineAuthRepository): AuthRepository

    @Binds
    abstract fun bindTransactionRepository(impl: OfflineTransactionRepository): TransactionRepository
}
