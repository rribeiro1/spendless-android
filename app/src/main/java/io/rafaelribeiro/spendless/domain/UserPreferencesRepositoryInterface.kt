package io.rafaelribeiro.spendless.domain

import kotlinx.coroutines.flow.Flow
import io.rafaelribeiro.spendless.data.UserPreferences

interface UserPreferencesRepositoryInterface {
    val userPreferencesFlow: Flow<UserPreferences>
    suspend fun saveUserName(userName: String)
    suspend fun savePin(pin: String)
    suspend fun clearAllPreferences()
}
