package io.rafaelribeiro.spendless.presentation.screens.settings.security

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.SpendLessButton
import io.rafaelribeiro.spendless.domain.LockoutDuration
import io.rafaelribeiro.spendless.domain.SessionExpiryDuration
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.core.presentation.SpendLessSegmentedButton
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSecurityScreen(
    modifier: Modifier,
    navigationState: NavigationState,
    onEvent: (SettingsSecurityUiEvent) -> Unit,
    uiState: SecurityUiState,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.security)) },
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
        SecurityScreen(
            modifier = Modifier.padding(innerPadding),
            onEvent = onEvent,
            uiState = uiState
        )
    }
}

@Composable
fun SecurityScreen(
    modifier: Modifier,
    onEvent: (SettingsSecurityUiEvent) -> Unit,
    uiState: SecurityUiState,
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            SpendLessSegmentedButton(
                title = stringResource(R.string.session_expiry_duration),
                options = SessionExpiryDuration.entries.map { it.title },
                selectedIndex = SessionExpiryDuration.entries.indexOf(uiState.sessionExpiryDuration),
                onOptionSelected = {
                    onEvent(
                        SettingsSecurityUiEvent.SessionExpiryDurationSelected(
                            SessionExpiryDuration.entries[it]
                        )
                    )
                }
            )
            SpendLessSegmentedButton(
                title = stringResource(R.string.lockout_duration),
                options = LockoutDuration.entries.map { it.title },
                selectedIndex = LockoutDuration.entries.indexOf(uiState.lockoutDuration),
                onOptionSelected = {
                    onEvent(SettingsSecurityUiEvent.LockedOutDurationSelected(LockoutDuration.entries[it]))
                }
            )
            SpendLessButton(
                modifier = Modifier.padding(vertical = 24.dp),
                text = stringResource(R.string.save),
                onClick = {
                    onEvent(SettingsSecurityUiEvent.SaveClicked)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsSecurityScreenPreview() {
    SpendLessTheme {
        SecurityScreen(
            modifier = Modifier,
            onEvent = {},
            uiState = SecurityUiState()
        )
    }
}
