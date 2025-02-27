package io.rafaelribeiro.spendless.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        private val USER_NAME = stringPreferencesKey("user_name")
        private val PIN = stringPreferencesKey("pin")
        // Todo: Expenses Format can be store here..
    }

    val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .map { preferences ->
            val userName = preferences[USER_NAME] ?: ""
            val pin = preferences[PIN] ?: ""
            UserPreferences(userName, pin)
        }

    suspend fun saveUserName(userName: String) {
        dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

    suspend fun savePin(pin: String) {
        dataStore.edit { preferences ->
            preferences[PIN] = pin
        }
    }

    // ❌ Clear all stored preferences
    suspend fun clearAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear() // Removes all stored keys and values
        }
    }
}

data class UserPreferences(
    val userName: String,
    val pin: String,
)

