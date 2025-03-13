package io.rafaelribeiro.spendless.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.data.crypto.UserSerializer
import io.rafaelribeiro.spendless.domain.user.User
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    private const val USER_PREFERENCES_NAME = "user_preferences"
    private const val USER_ENCRYPTED_DATASTORE_NAME = "user_accounts"

    private val Context.dataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)
    private val Context.dataStoreEncryptedUser by dataStore(fileName = USER_ENCRYPTED_DATASTORE_NAME, serializer = UserSerializer)

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideDataStoreUser(@ApplicationContext context: Context): DataStore<User> {
        return context.dataStoreEncryptedUser
    }
}
