package io.rafaelribeiro.spendless.presentation.screens.registration

import io.rafaelribeiro.spendless.data.UserPreferences
import io.rafaelribeiro.spendless.domain.UserPreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class UserPreferencesRepositoryFake : UserPreferencesRepository {

    private val _userPreferencesFlow = MutableStateFlow(UserPreferences("", ""))
    override val userPreferencesFlow: Flow<UserPreferences> = _userPreferencesFlow.asStateFlow()

    private var userName: String = ""
    private var pin: String = ""

    override suspend fun saveUserName(userName: String) {
        this.userName = userName
        _userPreferencesFlow.update {
            it.copy(userName = userName)
        }
    }

    override suspend fun savePin(pin: String) {
        this.pin = pin
        _userPreferencesFlow.update {
            it.copy(pin = pin)
        }
    }

    override suspend fun clearAllPreferences() {
        userName = ""
        pin = ""
        _userPreferencesFlow.update {
            it.copy(userName = "", pin = "")
        }
    }
}
