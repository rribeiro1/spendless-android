package io.rafaelribeiro.spendless.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.core.data.BiometricPromptManager


@Module
@InstallIn(SingletonComponent::class)
object BiometricModule {

    @Provides
    fun provideBiometricPromptManager(@ApplicationContext context: Context): BiometricPromptManager {
        return BiometricPromptManager(context)
    }
}
