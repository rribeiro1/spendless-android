package io.rafaelribeiro.spendless.domain

import kotlinx.coroutines.flow.Flow
import io.rafaelribeiro.spendless.data.UserPreferences

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    suspend fun clearAllPreferences()
}
