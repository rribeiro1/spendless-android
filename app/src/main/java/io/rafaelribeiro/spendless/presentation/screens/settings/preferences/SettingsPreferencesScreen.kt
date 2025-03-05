package io.rafaelribeiro.spendless.presentation.screens.settings.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.PreferencesScreen
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.navigation.rememberNavigationState
import io.rafaelribeiro.spendless.presentation.screens.registration.PreferencesUiEvent
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPreferencesScreen(
    modifier: Modifier,
    navigationState: NavigationState,
    uiState: SettingsPreferencesUiState,
    onEvent: (PreferencesUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.preferences)) },
                navigationIcon = {
                    IconButton(onClick = { navigationState.popBackStack() }) {
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
        },
    ) { innerPadding ->
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            PreferencesScreen(
                onEvent = onEvent,
                buttonText = stringResource(R.string.save),
                exampleExpenseFormat = uiState.exampleExpenseFormat,
                expensesFormat = uiState.expensesFormat,
                decimalSeparator = uiState.decimalSeparator,
                thousandSeparator = uiState.thousandSeparator,
                currencySymbol = uiState.currencySymbol,
                buttonEnabled = uiState.buttonEnabled
            )
        }
    }

}


@Preview
@Composable
fun SettingsPreferencesScreenPreview() {
    SpendLessTheme {
        SettingsPreferencesScreen(
            modifier = Modifier,
            navigationState = rememberNavigationState(),
            uiState = SettingsPreferencesUiState(),
            onEvent = {},
        )
    }
}
