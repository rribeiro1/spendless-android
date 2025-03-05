package io.rafaelribeiro.spendless.presentation.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.spendlessShadow
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsRootScreen(
    modifier: Modifier,
    onEvent: (SettingsUiEvent) -> Unit,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(SettingsUiEvent.BackClicked) }) {
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
        SettingsMainScreen(
            modifier = Modifier.padding(innerPadding),
            onEvent = onEvent,
        )
    }

}

@Composable
fun SettingsMainScreen(
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
            SettingItemList(stringResource(R.string.preferences), Icons.Filled.Settings) {
                onEvent(SettingsUiEvent.PreferencesClicked)
            }
            SettingItemList(stringResource(R.string.security), Icons.Default.Lock) {
                onEvent(SettingsUiEvent.SecurityClicked)
            }
        }

        Column(
            modifier = Modifier
                .padding(top = 8.dp)
                .spendlessShadow()
                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(16.dp))
        ) {
            SettingItemList(
                title = stringResource(R.string.log_out),
                icon = Icons.AutoMirrored.Filled.Logout,
                iconContainerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
                iconTint = MaterialTheme.colorScheme.error,
                textColor = MaterialTheme.colorScheme.error,
                onClick = {
                    onEvent(SettingsUiEvent.LogoutClicked)
                }
            )
        }
    }
}

@Composable
private fun SettingItemList(
    title: String,
    icon: ImageVector,
    iconContainerColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit = {}
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 4.dp, vertical = 4.dp)
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(iconContainerColor)

        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
            )
        }
        Text(
            modifier = Modifier.padding(start = 8.dp),
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = textColor,
        )
    }
}


@Preview
@Composable
fun SettingsScreenPreview() {
    SpendLessTheme {
        SettingsRootScreen(
            modifier = Modifier,
            onEvent = {},
        )
    }
}
