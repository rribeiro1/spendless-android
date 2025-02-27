package io.rafaelribeiro.spendless.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.data.OfflineAuthRepository
import io.rafaelribeiro.spendless.data.UserPreferencesRepository
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.UserPreferencesRepositoryInterface

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
	@Binds
	abstract fun bindAuthRepository(impl: OfflineAuthRepository): AuthRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class UserPreferencesRepositoryModule {
	@Binds
	abstract fun bindUserPreferencesRepository(impl: UserPreferencesRepository): UserPreferencesRepositoryInterface
}
