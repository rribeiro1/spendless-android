package io.rafaelribeiro.spendless.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.data.PinVerifierImpl
import io.rafaelribeiro.spendless.domain.PinVerifier

@Module
@InstallIn(SingletonComponent::class)
abstract class PinVerifierModule {
    @Binds
    abstract fun bindPinVerifier(impl: PinVerifierImpl): PinVerifier
}
