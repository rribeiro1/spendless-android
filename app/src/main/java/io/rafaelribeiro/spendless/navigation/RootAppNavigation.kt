package io.rafaelribeiro.spendless.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import io.rafaelribeiro.spendless.core.presentation.ErrorDialog
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationActionEvent
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationPinConfirmationScreen
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationPinPromptScreen
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationPreferencesRootScreen
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationUsernameRootScreen
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun RootAppNavigation(
	navigationState: NavigationState,
	modifier: Modifier,
) {
	NavHost(
		navController = navigationState.navHostController,
		startDestination = Screen.RegistrationFlow.route,
	) {
		navigation(
			startDestination = Screen.RegistrationSetPreferences.route,
			route = Screen.RegistrationFlow.route,
		) {
			composable(route = Screen.RegistrationUsername.route) { entry ->
				val viewModel = entry.sharedViewModel<RegistrationViewModel>(navigationState.navHostController)
				val uiState by viewModel.uiState.collectAsStateWithLifecycle()
				ObserveAsEvents(flow = viewModel.actionEvents) { event ->
					if (event is RegistrationActionEvent.UsernameCheckSuccess) {
						navigationState.navigateTo(Screen.RegistrationPinCreation.route)
					}
				}
				RegistrationUsernameRootScreen(
					uiState = uiState,
					onEvent = viewModel::onEvent,
					modifier = modifier,
				)
				ErrorDialog(errorMessage = uiState.errorMessage)
			}
			composable(route = Screen.RegistrationPinCreation.route) { entry ->
				val viewModel = entry.sharedViewModel<RegistrationViewModel>(navigationState.navHostController)
				val uiState by viewModel.uiState.collectAsStateWithLifecycle()
				ObserveAsEvents(flow = viewModel.actionEvents) { event ->
					if (event is RegistrationActionEvent.PinCreated) {
						navigationState.navigateTo(Screen.RegistrationPinConfirmation.route)
					}
				}
				RegistrationPinPromptScreen(
					uiState = uiState,
					onEvent = viewModel::onEvent,
                    navigationState = navigationState,
					modifier = modifier,
				)
				ErrorDialog(errorMessage = uiState.errorMessage)
			}
			composable(route = Screen.RegistrationPinConfirmation.route) { entry ->
				val viewModel = entry.sharedViewModel<RegistrationViewModel>(navigationState.navHostController)
				val uiState by viewModel.uiState.collectAsStateWithLifecycle()
				ObserveAsEvents(flow = viewModel.actionEvents) { event ->
					when (event) {
						is RegistrationActionEvent.PinConfirmed -> {
							navigationState.navigateTo(Screen.RegistrationSetPreferences.route)
						}
						is RegistrationActionEvent.PinMismatch -> {
							navigationState.popBackStack()
						}
						else -> {}
					}
				}
				RegistrationPinConfirmationScreen(
					uiState = uiState,
					onEvent = viewModel::onEvent,
                    navigationState = navigationState,
					modifier = modifier,
				)
				ErrorDialog(errorMessage = uiState.errorMessage)
			}
			composable(route = Screen.RegistrationSetPreferences.route) { entry ->
				val viewModel = entry.sharedViewModel<RegistrationViewModel>(navigationState.navHostController)
				val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                RegistrationPreferencesRootScreen(
                    navigationState = navigationState,
                    modifier = modifier,
                )
			}
		}
	}
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
	navController: NavHostController,
): T {
	val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
	val parentEntry = remember(this) {
		navController.getBackStackEntry(navGraphRoute)
	}
	return hiltViewModel(parentEntry)
}

@Composable
private fun <T> ObserveAsEvents(flow: Flow<T>, onEvent: (T) -> Unit) {
	val lifecycleOwner = LocalLifecycleOwner.current
	LaunchedEffect(flow, lifecycleOwner.lifecycle) {
		lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
			flow.collect(onEvent)
		}
	}
}
