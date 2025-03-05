package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.data.repository.SecurityPreferences
import io.rafaelribeiro.spendless.data.repository.UserPreferences
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserPreferencesRepositoryFake : UserPreferencesRepository {

    private val initialUserPreferences = UserPreferences()
    private val _userPreferencesFlow = MutableStateFlow(initialUserPreferences)
    override val userPreferences: Flow<UserPreferences> = _userPreferencesFlow.asStateFlow()
    override val securityPreferences: Flow<SecurityPreferences>
        get() = TODO("Not yet implemented")


    override suspend fun clearAllPreferences() {
        _userPreferencesFlow.update {
            initialUserPreferences
        }
    }

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
//        _userPreferencesFlow.update {
//            it.copy(
//                expensesFormatName = userPreferences.expensesFormatName,
//                decimalSeparatorName = userPreferences.decimalSeparatorName,
//                thousandsSeparatorName = userPreferences.thousandsSeparatorName,
//                currencyName = userPreferences.currencyName,
//            )
//        }
    }

    override suspend fun saveSecurityPreferences(securityPreferences: SecurityPreferences) {
        TODO("Not yet implemented")
    }
}
