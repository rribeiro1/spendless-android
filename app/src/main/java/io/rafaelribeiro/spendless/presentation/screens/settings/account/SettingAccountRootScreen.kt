package io.rafaelribeiro.spendless.presentation.screens.settings.account

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DeleteForever
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
import io.rafaelribeiro.spendless.core.presentation.SpendLessMessageDialog
import io.rafaelribeiro.spendless.core.presentation.spendlessShadow
import io.rafaelribeiro.spendless.navigation.NavigationState
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingItemList
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsUiEvent
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsUiState
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingAccountRootScreen(
    modifier: Modifier,
    uiState: SettingsUiState,
    navigationState: NavigationState,
    onEvent: (SettingsUiEvent) -> Unit,
) {
    Box {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.account)) },
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
            SettingsAccountScreen(
                modifier = Modifier.padding(innerPadding),
                onEvent = onEvent,
            )
        }
        SpendLessMessageDialog(
            message = uiState.message,
            isError = uiState.isError,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun SettingsAccountScreen(
    modifier: Modifier,
    onEvent: (SettingsUiEvent) -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),

        ) {
        Column(
            modifier = Modifier
                .spendlessShadow()
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(16.dp))

        ) {
            SettingItemList(stringResource(R.string.add_fake_transactions), Icons.Filled.AttachMoney) {
                onEvent(SettingsUiEvent.AddFakeTransactionsClicked)
            }
            SettingItemList(stringResource(R.string.delete_transactions), Icons.Filled.Delete) {
                onEvent(SettingsUiEvent.DeleteFakeTransactionsClicked)
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .spendlessShadow()
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(16.dp))
        ) {
            SettingItemList(
                title = stringResource(R.string.delete_account),
                icon = Icons.Default.DeleteForever,
                iconContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                iconTint = MaterialTheme.colorScheme.error,
                textColor = MaterialTheme.colorScheme.error,
                onClick = {
                    onEvent(SettingsUiEvent.DeleteAccountClicked)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsAccountScreenPreview() {
    SpendLessTheme {
        SettingsAccountScreen(
            modifier = Modifier,
            onEvent = {},
        )
    }
}
