package io.rafaelribeiro.spendless.presentation.screens.registration

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import io.rafaelribeiro.spendless.domain.AuthRepository
import io.rafaelribeiro.spendless.domain.RegistrationError
import io.rafaelribeiro.spendless.domain.Result
import io.rafaelribeiro.spendless.domain.User
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class RegistrationViewModelTest {
    private lateinit var registrationViewModel: RegistrationViewModel
    private val state get() = registrationViewModel.uiState.value

    @BeforeEach
    fun setup() {
        registrationViewModel = RegistrationViewModel(
            authRepository = object : AuthRepository {
                override fun checkUserName(username: String): Result<Unit, RegistrationError> = Result.Success(Unit)
                override fun register(username: String, pin: String): Result<User, RegistrationError> = Result.Success(data = User(username = "rafael"))
            }
        )
    }

    @Nested
    inner class UsernameValidation {
        @Test
        fun `next button is enabled once the username contains at least one character`() {
            assertThat(state.nextButtonEnabled).isFalse()
            registrationViewModel.usernameChanged("r")
            assertThat(state.nextButtonEnabled).isTrue()
        }

        @ParameterizedTest
        @CsvSource(
            "short,short",
            "fits.perfectly,fits.perfectly",
            "this.is.a.huge.username,this.is.a.huge",
        )
        fun `username must not exceed 14 characters`(input: String, result: String) {
            registrationViewModel.usernameChanged(input)
            assertThat(state.username).isEqualTo(result)
        }

        @Test
        fun `username must be at least 3 characters to enable next button`() {
            registrationViewModel.usernameChanged("raf")
            assertThat(state.nextButtonEnabled).isTrue()
            registrationViewModel.usernameChanged("ra")
            assertThat(state.nextButtonEnabled).isFalse()
        }
    }
}