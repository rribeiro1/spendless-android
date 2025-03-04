package io.rafaelribeiro.spendless

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.rafaelribeiro.spendless.data.repository.DataStoreUserPreferencesRepository
import io.rafaelribeiro.spendless.domain.AuthRepository
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dataStoreUserPreferencesRepository: DataStoreUserPreferencesRepository,
) : ViewModel() {

    val isUserLoggedIn: SharedFlow<Boolean> = authRepository.userName
        .map {
            it.isNotEmpty()
        }
        .shareIn(
            replay = 0,
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
        )

}
