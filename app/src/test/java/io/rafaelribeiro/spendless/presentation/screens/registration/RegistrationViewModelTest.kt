package io.rafaelribeiro.spendless.presentation.screens.registration

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isInstanceOf
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import io.rafaelribeiro.spendless.core.presentation.UiText
import io.rafaelribeiro.spendless.core.presentation.asUiText
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.RegistrationError
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class RegistrationViewModelTest {
	private lateinit var registrationViewModel: RegistrationViewModel
	private val state get() = registrationViewModel.uiState.value

    @ExperimentalCoroutinesApi
    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()

	@OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
	fun setup() {
        Dispatchers.setMain(testDispatcher)
		registrationViewModel = RegistrationViewModel(
            authRepository = AuthRepositoryFake(),
            userPreferencesRepository = UserPreferencesRepositoryFake(),
        )
	}

    @ExperimentalCoroutinesApi
    @AfterEach
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

	@Nested
	inner class OnUsernameChangedEvent {
		@Test
		fun `next button is enabled once the username contains at least three characters`() {
			assertThat(state.nextButtonEnabled).isFalse()
			registrationViewModel.onEvent(RegistrationUiEvent.UsernameChanged("raf"))
			assertThat(state.nextButtonEnabled).isTrue()
		}

		@ParameterizedTest
		@CsvSource(
			"short,short",
			"fits.perfectly,fits.perfectly",
			"this.is.a.huge.username,this.is.a.huge",
		)
		fun `username must not exceed 14 characters`(input: String, result: String) {
			registrationViewModel.onEvent(RegistrationUiEvent.UsernameChanged(input))
			assertThat(state.username).isEqualTo(result)
		}
	}

    @Nested
    inner class OnActionButtonNextClickedEvent {
        @Test
        fun `returns error when username already exists`() = runTest {
            registrationViewModel.onEvent(RegistrationUiEvent.UsernameChanged("rafael"))
            registrationViewModel.onEvent(RegistrationUiEvent.ActionButtonNextClicked)
            assertThat(state.errorMessage).isNotNull()
            assertThat(state.errorMessage).isInstanceOf(UiText.StringResource::class.java)
            assertThat((state.errorMessage as UiText.StringResource).id)
                .isEqualTo((RegistrationError.USERNAME_ALREADY_EXISTS.asUiText() as UiText.StringResource).id)
        }

        @Test
        fun `disables next button`() = runTest {
            registrationViewModel.onEvent(RegistrationUiEvent.UsernameChanged("rafael"))
            registrationViewModel.onEvent(RegistrationUiEvent.ActionButtonNextClicked)
            assertThat(state.nextButtonEnabled).isFalse()
        }

        @Test
        @Disabled("Figure out how to assert collected action events")
        fun `sends action event when username is available`() = runTest {
            registrationViewModel.onEvent(RegistrationUiEvent.UsernameChanged("rafa"))
            registrationViewModel.onEvent(RegistrationUiEvent.ActionButtonNextClicked)
            // Collect the action events to check if the correct event was sent
            registrationViewModel.actionEvents.collect {
                assertThat(it).isInstanceOf(RegistrationActionEvent.UsernameCheckSuccess::class.java)
            }
        }
    }

    @Nested
    inner class OnPreferenceSettingsChangedEvent {
        @Test
        fun `updates the example currency text to match the current preferences selected`() {
            assertThat(state.preferences.exampleExpenseFormat).isEqualTo("-$10,382.45")

            registrationViewModel.onEvent(RegistrationUiEvent.ExpensesFormatSelected(ExpenseFormat.PARENTHESES))
            assertThat(state.preferences.exampleExpenseFormat).isEqualTo("($10,382.45)")

            registrationViewModel.onEvent(RegistrationUiEvent.DecimalSeparatorSelected(DecimalSeparator.COMMA))
            assertThat(state.preferences.exampleExpenseFormat).isEqualTo("($10,382,45)")

            registrationViewModel.onEvent(RegistrationUiEvent.ThousandSeparatorSelected(ThousandSeparator.SPACE))
            assertThat(state.preferences.exampleExpenseFormat).isEqualTo("($10 382,45)")

            registrationViewModel.onEvent(RegistrationUiEvent.CurrencySelected(CurrencySymbol.EURO))
            assertThat(state.preferences.exampleExpenseFormat).isEqualTo("(€10 382,45)")
        }

        @Test
        fun `disables button when user has the same separator selected for both the thousands and decimal separator`() {
            assertThat(state.preferences.startTrackingButtonEnabled).isTrue()

            registrationViewModel.onEvent(RegistrationUiEvent.DecimalSeparatorSelected(DecimalSeparator.COMMA))
            registrationViewModel.onEvent(RegistrationUiEvent.ThousandSeparatorSelected(ThousandSeparator.COMMA))
            assertThat(state.preferences.startTrackingButtonEnabled).isFalse()

            registrationViewModel.onEvent(RegistrationUiEvent.ThousandSeparatorSelected(ThousandSeparator.DOT))
            assertThat(state.preferences.startTrackingButtonEnabled).isTrue()

            registrationViewModel.onEvent(RegistrationUiEvent.DecimalSeparatorSelected(DecimalSeparator.DOT))
            assertThat(state.preferences.startTrackingButtonEnabled).isFalse()
        }
    }
}
