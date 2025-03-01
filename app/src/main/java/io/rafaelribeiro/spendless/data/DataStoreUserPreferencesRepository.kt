package io.rafaelribeiro.spendless.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationPreferencesUiState
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
        private val EXPENSES_FORMAT = intPreferencesKey("expenses_format")
        private val DECIMAL_SEPARATOR = intPreferencesKey("decimal_separator")
        private val THOUSANDS_SEPARATOR = intPreferencesKey("thousands_separator")
        private val CURRENCY = intPreferencesKey("currency_symbol")
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
            val expensesFormat = preferences[EXPENSES_FORMAT] ?: 0
            val decimalSeparator = preferences[DECIMAL_SEPARATOR] ?: 0
            val thousandsSeparator = preferences[THOUSANDS_SEPARATOR] ?: 0
            val currency = preferences[CURRENCY] ?: 0
            UserPreferences(
                expensesFormatOrdinal = expensesFormat,
                decimalSeparatorOrdinal = decimalSeparator,
                thousandsSeparatorOrdinal = thousandsSeparator,
                currencyOrdinal = currency
            )
        }

    override suspend fun clearAllPreferences() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        dataStore.edit { preferences ->
            preferences[EXPENSES_FORMAT] = userPreferences.expensesFormatOrdinal
            preferences[DECIMAL_SEPARATOR] = userPreferences.decimalSeparatorOrdinal
            preferences[THOUSANDS_SEPARATOR] = userPreferences.thousandsSeparatorOrdinal
            preferences[CURRENCY] = userPreferences.currencyOrdinal
        }
    }
}

data class UserPreferences(
    val expensesFormatOrdinal: Int,
    val decimalSeparatorOrdinal: Int,
    val thousandsSeparatorOrdinal: Int,
    val currencyOrdinal: Int,
)

fun RegistrationPreferencesUiState.toUserPreferences(): UserPreferences {
    return UserPreferences(
        expensesFormatOrdinal = expensesFormat.ordinal,
        decimalSeparatorOrdinal = decimalSeparator.ordinal,
        thousandsSeparatorOrdinal = thousandSeparator.ordinal,
        currencyOrdinal = currencySymbol.ordinal,
    )
}
