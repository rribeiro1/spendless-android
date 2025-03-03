package io.rafaelribeiro.spendless.presentation.screens.settings

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
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.presentation.screens.registration.components.SpendLessSegmentedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSecurityScreen(
    modifier: Modifier,
    navigationState: NavigationState,
    onEvent: (SettingsSecurityUiEvent) -> Unit,
    uiState: SettingsUiState,
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

        Box(modifier = Modifier.padding(innerPadding)){
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                SpendLessSegmentedButton(
                    title = stringResource(R.string.session_expiry_duration),
                    options = SessionExpiryDuration.entries.map { it.value },
                    selectedIndex = SessionExpiryDuration.entries.indexOf(uiState.sessionExpiryDuration),
                    onOptionSelected = {
                        onEvent(SettingsSecurityUiEvent.SessionExpiryDurationSelected(SessionExpiryDuration.entries[it]))
                    }
                )
                SpendLessSegmentedButton(
                    title = stringResource(R.string.lockout_duration),
                    options = LockoutDuration.entries.map { it.value },
                    selectedIndex = LockoutDuration.entries.indexOf(uiState.lockoutDuration),
                    onOptionSelected = {
                        onEvent(SettingsSecurityUiEvent.LockedOutDurationSelected(LockoutDuration.entries[it]))
                    }
                )
            }
        }
    }

}

enum class SessionExpiryDuration(val value: String) {
    MINUTES_5("5 min"),
    MINUTES_15("15 min"),
    MINUTES_30("30 min"),
    MINUTES_60("1 hour"),
}


enum class LockoutDuration(val value: String) {
    SECONDS_15("15s"),
    SECONDS_30("30s"),
    SECONDS_60("1 min"),
    SECONDS_300("5 min"),
}
