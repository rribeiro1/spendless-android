package io.rafaelribeiro.spendless.presentation.screens.transactions.export

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.SpendLessButton
import io.rafaelribeiro.spendless.core.presentation.SpendLessDropDown
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportFormat
import io.rafaelribeiro.spendless.domain.transaction.TransactionExportRange
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportTransactionRootScreen(
    onEvent: (ExportTransactionUiEvent) -> Unit = {},
    uiState: ExportTransactionUiState,
    modifier: Modifier,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        onDismissRequest = { onEvent(ExportTransactionUiEvent.OnCancelClicked) },
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = null
    ) {
        ExportTransactionScreen(
            onEvent = onEvent,
            uiState = uiState,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.75f)
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
        )
    }
}

@Composable
fun ExportTransactionScreen(
    onEvent: (ExportTransactionUiEvent) -> Unit = {},
    uiState: ExportTransactionUiState,
    modifier: Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = stringResource(R.string.export),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .clickable { onEvent(ExportTransactionUiEvent.OnCancelClicked) }
            )
        }
        SpendLessDropDown(
            title = stringResource(R.string.export_range),
            itemBackgroundColor = Color.Transparent,
            values = TransactionExportRange.entries,
            selectedValue = uiState.exportRange,
            getLeadingIcon = { "" },
            getText = { it.displayName },
            onItemSelected = { onEvent(ExportTransactionUiEvent.OnRangeSelected(it)) },
        )
        SpendLessDropDown(
            title = stringResource(R.string.export_format),
            itemBackgroundColor = Color.Transparent,
            values = TransactionExportFormat.entries,
            selectedValue = uiState.exportFormat,
            getLeadingIcon = { "" },
            getText = { it.name },
            onItemSelected = { onEvent(ExportTransactionUiEvent.OnFormatSelected(it)) },
        )
        SpendLessButton(
            text = stringResource(R.string.export),
            onClick = { onEvent(ExportTransactionUiEvent.OnExportClicked) },
            modifier = Modifier.padding(top = 24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ExportTransactionRootScreenPreview() {
    SpendLessTheme {
        ExportTransactionScreen(
            uiState = ExportTransactionUiState(),
            modifier = Modifier.padding(16.dp)
        )
    }
}