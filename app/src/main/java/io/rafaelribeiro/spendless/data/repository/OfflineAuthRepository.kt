package io.rafaelribeiro.spendless.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.RegistrationError
import io.rafaelribeiro.spendless.domain.Result
import io.rafaelribeiro.spendless.domain.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfflineAuthRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : AuthRepository {

    companion object {
        private val USER_NAME = stringPreferencesKey("user_name")
        private val PIN = stringPreferencesKey("pin")
    }

    override val pin: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[PIN] ?: ""
        }
    override val userName: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[USER_NAME] ?: ""
        }

	override suspend fun checkUserName(username: String): Result<Unit, RegistrationError> =
		if (!username.all { it.isLetterOrDigit() }) {
			Result.Failure(RegistrationError.USERNAME_MUST_BE_ALPHANUMERIC)
		} else if (username == userName.first()) {
			Result.Failure(RegistrationError.USERNAME_ALREADY_EXISTS)
		} else {
			Result.Success(Unit)
		}

	override suspend fun register(
		username: String,
		pin: String,
	): Result<User, RegistrationError> {

        dataStore.edit {
            it[USER_NAME] = username
            it[PIN] = pin // TODO: encrypt it
        }

        return Result.Success(User(username))
    }
}
