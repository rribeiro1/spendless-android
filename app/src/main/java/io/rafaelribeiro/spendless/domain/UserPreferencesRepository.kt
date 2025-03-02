package io.rafaelribeiro.spendless.domain

import kotlinx.coroutines.flow.Flow
import io.rafaelribeiro.spendless.data.repository.UserPreferences

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun clearAllPreferences()
    suspend fun saveUserPreferences(userPreferences: UserPreferences)
}
