package io.rafaelribeiro.spendless.presentation.screens.registration

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.SpendLessMessageDialog
import io.rafaelribeiro.spendless.core.presentation.PinPromptScreen
import io.rafaelribeiro.spendless.navigation.NavigationState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPinConfirmationScreen(
    navigationState: NavigationState,
    uiState: RegistrationUiState,
    onEvent: (RegistrationUiEvent) -> Unit,
    modifier: Modifier,
) {
    fun onBackPress() {
        onEvent(RegistrationUiEvent.ResetPinValues)
        navigationState.popBackStack()
    }
    BackHandler(onBack = ::onBackPress)

    Box {
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
            PinPromptScreen(
                modifier = modifier.padding(innerPadding),
                title = stringResource(id = R.string.registration_pin_confirmation_title),
                description = stringResource(id = R.string.registration_pin_confirmation_description),
                currentPinSize = uiState.pinConfirmation.length,
                onNumberClick = { onEvent(RegistrationUiEvent.PinConfirmationDigitTapped(it)) },
                onBackspaceClick = { onEvent(RegistrationUiEvent.PinConfirmationBackspaceTapped) },
            )
        }
        SpendLessMessageDialog(
            modifier = modifier.align(Alignment.BottomCenter),
            message = uiState.errorMessage,
        )
    }
}

