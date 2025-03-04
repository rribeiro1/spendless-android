package io.rafaelribeiro.spendless.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.PreferencesUiState
import io.rafaelribeiro.spendless.domain.LockoutDuration
import io.rafaelribeiro.spendless.domain.SessionExpiryDuration
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DataStoreUserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : UserPreferencesRepository {

    companion object {
        private val EXPENSES_FORMAT = stringPreferencesKey("expenses_format")
        private val DECIMAL_SEPARATOR = stringPreferencesKey("decimal_separator")
        private val THOUSANDS_SEPARATOR = stringPreferencesKey("thousands_separator")
        private val CURRENCY = stringPreferencesKey("currency_symbol")
        private val SESSION_EXPIRY_DURATION = intPreferencesKey("session_expiry_duration")
        private val LOCKED_OUT_DURATION = longPreferencesKey("locked_out_duration")
    }

    override val userPreferences: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val expensesFormat = preferences[EXPENSES_FORMAT] ?: ""
            val decimalSeparator = preferences[DECIMAL_SEPARATOR] ?: ""
            val thousandsSeparator = preferences[THOUSANDS_SEPARATOR] ?: ""
            val currency = preferences[CURRENCY] ?: ""
            UserPreferences(
                expensesFormatName = expensesFormat,
                decimalSeparatorName = decimalSeparator,
                thousandsSeparatorName = thousandsSeparator,
                currencyName = currency
            )
        }

    override val securityPreferences: Flow<SecurityPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val sessionExpiryDuration = preferences[SESSION_EXPIRY_DURATION]?.toInt() ?: SessionExpiryDuration.MINUTES_5.value
            val lockedOutDuration = preferences[LOCKED_OUT_DURATION]?.toLong() ?: LockoutDuration.SECONDS_30.value
            SecurityPreferences(
                sessionExpiryDuration = sessionExpiryDuration,
                lockedOutDuration = lockedOutDuration
            )
        }

    override suspend fun clearAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        dataStore.edit { preferences ->
            preferences[EXPENSES_FORMAT] = userPreferences.expensesFormatName
            preferences[DECIMAL_SEPARATOR] = userPreferences.decimalSeparatorName
            preferences[THOUSANDS_SEPARATOR] = userPreferences.thousandsSeparatorName
            preferences[CURRENCY] = userPreferences.currencyName
        }
    }

    override suspend fun saveSecurityPreferences(securityPreferences: SecurityPreferences) {
        dataStore.edit { preferences ->
            preferences[SESSION_EXPIRY_DURATION] = securityPreferences.sessionExpiryDuration
            preferences[LOCKED_OUT_DURATION] = securityPreferences.lockedOutDuration
        }
    }

}

open class UserPreferences(
    val expensesFormatName: String = "",
    val decimalSeparatorName: String = "",
    val thousandsSeparatorName: String = "",
    val currencyName: String = "",
)

data class SecurityPreferences(
    val sessionExpiryDuration: Int = SessionExpiryDuration.MINUTES_5.value,
    val lockedOutDuration: Long = LockoutDuration.SECONDS_30.value,
): UserPreferences()


fun PreferencesUiState.toUserPreferences(): UserPreferences {
    return UserPreferences(
        expensesFormatName = expensesFormat.name,
        decimalSeparatorName = decimalSeparator.name,
        thousandsSeparatorName = thousandSeparator.name,
        currencyName = currencySymbol.name,
    )
}
