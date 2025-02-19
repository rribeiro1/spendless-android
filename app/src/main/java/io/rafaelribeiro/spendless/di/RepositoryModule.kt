package io.rafaelribeiro.spendless.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.data.OfflineAuthRepository
import io.rafaelribeiro.spendless.domain.AuthRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
	@Binds
	abstract fun bindAuthRepository(impl: OfflineAuthRepository): AuthRepository
}
