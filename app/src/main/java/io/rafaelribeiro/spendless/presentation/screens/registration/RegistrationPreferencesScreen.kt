package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.presentation.screens.registration.components.ExpenseCategories
import io.rafaelribeiro.spendless.presentation.screens.registration.components.SpendLessDropDown
import io.rafaelribeiro.spendless.presentation.screens.registration.components.SpendLessSegmentedButton
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPreferencesRootScreen(
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
            expenseFormat = "-$10,382.45"
        )
    }
}

@Composable
fun RegistrationPreferencesScreen(
    modifier: Modifier,
    expenseFormat: String,
) {
    var selectedPreferenceFormat by remember { mutableIntStateOf(0) }
    var selectedCurrency by remember { mutableIntStateOf(0) }
    var selectedDecimalSeparator by remember { mutableIntStateOf(0) }
    var selectedThousandSeparator by remember { mutableIntStateOf(0) }

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
                    text = expenseFormat,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp),
                )
                Text(
                    text = "spend this month",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
        }

        SpendLessSegmentedButton(
            title = "Expenses format",
            options = listOf("-$10", "($10)"),
            selectedIndex = selectedPreferenceFormat,
            onOptionSelected = { selectedPreferenceFormat = it }
        )
        SpendLessDropDown(
            title = "Currency",
            values = ExpenseCategories.categories,
            itemBackgroundColor = MaterialTheme.colorScheme.secondary,
        )
        SpendLessSegmentedButton(
            title = "Decimal separator",
            options = listOf("1.00", "1,00"),
            selectedIndex = selectedDecimalSeparator,
            onOptionSelected = { selectedDecimalSeparator = it }
        )
        SpendLessSegmentedButton(
            title = "Thousands separator",
            options = listOf("1.000", "1,000", "1 000"),
            selectedIndex = selectedThousandSeparator,
            onOptionSelected = { selectedThousandSeparator = it }
        )
        Button(
            onClick = {},
            modifier = Modifier
                .padding(top = 34.dp)
                .height(48.dp)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            ),
            shape = RoundedCornerShape(16.dp),
            enabled = true
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Start Tracking!",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Preview
@Composable
fun RegistrationPreferencesRootScreenPreview() {
    SpendLessTheme {
        RegistrationPreferencesScreen(
            modifier = Modifier,
            expenseFormat = "-$10,382.45"
        )
    }
}

