package io.rafaelribeiro.spendless.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import androidx.navigation.navOptions
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
		enterTransition = {
			enterTransition(AnimatedContentTransitionScope.SlideDirection.Start)
		},
		exitTransition = {
			exitTransition(AnimatedContentTransitionScope.SlideDirection.Start)
		},
		popEnterTransition = {
			enterTransition(AnimatedContentTransitionScope.SlideDirection.End)
		},
		popExitTransition = {
			exitTransition(AnimatedContentTransitionScope.SlideDirection.End)
		}
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
							navigationState.navigateTo(
								route = Screen.RegistrationSetPreferences.route,
								navOptions = navOptions {
									popUpTo(Screen.RegistrationPinCreation.route) { inclusive = false }
									restoreState = true
								}
							)
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
					uiState = uiState,
                    navigationState = navigationState,
                    modifier = modifier,
					onEvent = viewModel::onEvent
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

fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(
	slideDirection: AnimatedContentTransitionScope.SlideDirection
) = slideOutOfContainer(
	slideDirection,
	animationSpec = tween(500)
)

fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(
	slideDirection: AnimatedContentTransitionScope.SlideDirection
) = slideIntoContainer(
	slideDirection,
	animationSpec = tween(500)
)
