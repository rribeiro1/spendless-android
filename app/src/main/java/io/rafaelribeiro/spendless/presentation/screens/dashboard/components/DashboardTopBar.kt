package io.rafaelribeiro.spendless.presentation.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardTopBar(
    username: String,
    onSettingsClick: () -> Unit = {},
    onDownloadClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = username,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = Color.Transparent,
        ),
        actions = {
            IconButton(
                onClick = { onDownloadClick() },
                modifier = Modifier
                    .padding(end = 14.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Download,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Download transactions"
                )
            }
            IconButton(
                onClick = { onSettingsClick() },
                modifier = Modifier
                    .padding(end = 14.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Go to settings"
                )
            }
        }
    )
}

@Preview
@Composable
fun DashboardTopBarPreview() {
    SpendLessTheme {
        Box(
            modifier = Modifier.background(MaterialTheme.colorScheme.primary)
        ) {
            DashboardTopBar(username = "rafael89")
        }
    }
}
