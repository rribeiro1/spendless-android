package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.data.UserPreferences
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserPreferencesRepositoryFake : UserPreferencesRepository {

    private val initialUserPreferences = UserPreferences(0,0,0,0)
    private val _userPreferencesFlow = MutableStateFlow(initialUserPreferences)
    override val userPreferences: Flow<UserPreferences> = _userPreferencesFlow.asStateFlow()


    override suspend fun clearAllPreferences() {
        _userPreferencesFlow.update {
            initialUserPreferences
        }
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        _userPreferencesFlow.update {
            it.copy(
                expensesFormatOrdinal = userPreferences.expensesFormatOrdinal,
                decimalSeparatorOrdinal = userPreferences.decimalSeparatorOrdinal,
                thousandsSeparatorOrdinal = userPreferences.thousandsSeparatorOrdinal,
                currencyOrdinal = userPreferences.currencyOrdinal,
            )
        }
    }
}
