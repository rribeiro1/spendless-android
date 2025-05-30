package io.rafaelribeiro.spendless.domain.user

import io.rafaelribeiro.spendless.data.repository.SecurityPreferences
import kotlinx.coroutines.flow.Flow
import io.rafaelribeiro.spendless.data.repository.UserPreferences

interface UserPreferencesRepository {
    val userPreferences: Flow<UserPreferences>
    val securityPreferences: Flow<SecurityPreferences>
    val pinLockStatePreferences: Flow<Int?>
    suspend fun clearAllPreferences()
    suspend fun saveUserPreferences(userPreferences: UserPreferences)
    suspend fun saveSecurityPreferences(securityPreferences: SecurityPreferences)
    suspend fun savePinLockState(remainingSeconds: Int)
    suspend fun clearPinLockState()
}
