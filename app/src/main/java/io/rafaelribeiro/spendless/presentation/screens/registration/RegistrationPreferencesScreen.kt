package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.PreferencesScreen
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.PreferencesUiState
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPreferencesRootScreen(
    uiState: RegistrationUiState,
    navigationState: NavigationState,
    modifier: Modifier,
    onEvent: (RegistrationUiEvent) -> Unit,
) {
    fun onBackPress() {
        onEvent(RegistrationUiEvent.ResetPinValues)
        navigationState.popBackStack()
    }
    BackHandler(onBack = ::onBackPress)
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = ::onBackPress) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.background,
                )
            )
        }
    ) { innerPadding ->
        RegistrationPreferencesScreen(
            modifier = modifier.padding(innerPadding),
            uiState = uiState.preferences,
            onEvent = onEvent,
        )
    }
}

@Composable
fun RegistrationPreferencesScreen(
    modifier: Modifier,
    uiState: PreferencesUiState,
    onEvent: (RegistrationUiEvent) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
    ) {
        Text(
            text = stringResource(R.string.registration_preferences_title),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        )
        Text(
            text = stringResource(R.string.registration_preferences_description),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        )
        PreferencesScreen(uiState, onEvent)
    }
}

@Preview
@Composable
fun RegistrationPreferencesRootScreenPreview() {
    SpendLessTheme {
        RegistrationPreferencesScreen(
            modifier = Modifier,
            uiState = PreferencesUiState(
                exampleExpenseFormat = "-$1.234,50",
                expensesFormat = ExpenseFormat.PARENTHESES,
                decimalSeparator = DecimalSeparator.DOT,
                thousandSeparator = ThousandSeparator.DOT,
                currencySymbol = CurrencySymbol.DOLLAR,
            ),
            onEvent = {},
        )
    }
}
