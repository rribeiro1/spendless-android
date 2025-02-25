package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import io.rafaelribeiro.spendless.core.presentation.SpendLessButton
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.presentation.screens.registration.components.SpendLessDropDown
import io.rafaelribeiro.spendless.presentation.screens.registration.components.SpendLessSegmentedButton
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
    uiState: RegistrationPreferencesUiState,
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 32.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = DefaultShadowColor.copy(
                        red = 24 / 255f,
                        green = 0 / 255f,
                        blue = 64 / 255f,
                        alpha = 0.4f
                    ),
                )
                .background(MaterialTheme.colorScheme.onPrimary)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = uiState.exampleExpenseFormat,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp),
                )
                Text(
                    text = stringResource(R.string.spend_this_month),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }

        SpendLessSegmentedButton(
            title = stringResource(R.string.expenses_format),
            options = ExpenseFormat.entries.map { it.value },
            selectedIndex = ExpenseFormat.entries.indexOf(uiState.expensesFormat),
            onOptionSelected = {
                onEvent(RegistrationUiEvent.ExpensesFormatSelected(ExpenseFormat.entries[it]))
            }
        )
        SpendLessDropDown(
            title = stringResource(R.string.currency),
            values = CurrencySymbol.entries,
            itemBackgroundColor = MaterialTheme.colorScheme.onPrimary,
            getText = { it.title },
            getLeadingIcon = { it.symbol },
            onItemSelected = { onEvent(RegistrationUiEvent.CurrencySelected(it)) },
        )
        SpendLessSegmentedButton(
            title = stringResource(R.string.decimal_separator),
            options = DecimalSeparator.entries.map { it.value },
            selectedIndex = DecimalSeparator.entries.indexOf(uiState.decimalSeparator),
            onOptionSelected = {
                onEvent(RegistrationUiEvent.DecimalSeparatorSelected(DecimalSeparator.entries[it]))
            }
        )
        SpendLessSegmentedButton(
            title = stringResource(R.string.thousands_separator),
            options = ThousandSeparator.entries.map { it.value },
            selectedIndex = ThousandSeparator.entries.indexOf(uiState.thousandSeparator),
            onOptionSelected = {
                onEvent(RegistrationUiEvent.ThousandSeparatorSelected(ThousandSeparator.entries[it]))
            }
        )
        SpendLessButton(
            text = stringResource(R.string.start_tracking),
            modifier = Modifier.padding(top = 34.dp),
            enabled = uiState.startTrackingButtonEnabled,
            onClick = {
                onEvent(RegistrationUiEvent.StartTrackingButtonTapped)
            },
        )
    }
}

@Preview
@Composable
fun RegistrationPreferencesRootScreenPreview() {
    SpendLessTheme {
        RegistrationPreferencesScreen(
            modifier = Modifier,
            uiState = RegistrationPreferencesUiState(
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
