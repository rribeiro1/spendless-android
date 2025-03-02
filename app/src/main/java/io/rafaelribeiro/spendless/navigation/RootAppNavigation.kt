package io.rafaelribeiro.spendless.navigation

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
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardRootScreen
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardViewModel
import io.rafaelribeiro.spendless.presentation.screens.login.LoginActionEvent
import io.rafaelribeiro.spendless.presentation.screens.login.LoginRootScreen
import io.rafaelribeiro.spendless.presentation.screens.login.LoginViewModel
import io.rafaelribeiro.spendless.presentation.screens.login.LoginPinPromptRootScreen
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
	modifier: Modifier = Modifier,
) {
	NavHost(
		navController = navigationState.navHostController,
		startDestination = Screen.RegistrationFlow.route,
		enterTransition = enterTransition(),
		exitTransition = exitTransition(),
		popEnterTransition = popEnterTransition(),
		popExitTransition = popExitTransition(),
	) {
		navigation(
			startDestination = Screen.RegistrationUsername.route,
			route = Screen.RegistrationFlow.route,
		) {
			composable(route = Screen.RegistrationUsername.route) { entry ->
				val viewModel = entry.sharedViewModel<RegistrationViewModel>(navigationState.navHostController)
				val uiState by viewModel.uiState.collectAsStateWithLifecycle()
				ObserveAsEvents(flow = viewModel.actionEvents) { event ->
					when {
						event is RegistrationActionEvent.AlreadyHaveAccount -> {
							navigationState.navigateTo(
								route = Screen.LoginScreen.route,
								navOptions = navOptions {
									popUpTo(Screen.RegistrationUsername.route) { inclusive = true }
								}
							)
						}
						event is RegistrationActionEvent.UsernameCheckSuccess -> {
							navigationState.navigateTo(Screen.RegistrationPinCreation.route)
						}
					}
				}
				RegistrationUsernameRootScreen(
					uiState = uiState,
					onEvent = viewModel::onEvent,
					modifier = modifier,
				)
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
			}
			composable(route = Screen.RegistrationSetPreferences.route) { entry ->
                val viewModel = entry.sharedViewModel<RegistrationViewModel>(navigationState.navHostController)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ObserveAsEvents(flow = viewModel.actionEvents) { event ->
                    if (event is RegistrationActionEvent.UserPreferencesSaved) {
                        navigationState.navigateTo(
                            route = Screen.DashboardScreen.route,
                            navOptions = navOptions {
                                popUpTo(Screen.RegistrationFlow.route) { inclusive = true }
                            }
                        )
                    }
                }
                RegistrationPreferencesRootScreen(
                    uiState = uiState,
                    navigationState = navigationState,
                    modifier = modifier,
                    onEvent = viewModel::onEvent
                )
			}
		}
		composable(route = Screen.LoginScreen.route) {
			val viewModel = hiltViewModel<LoginViewModel>()
			val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ObserveAsEvents(flow = viewModel.actionEvents) { event ->
                when (event) {
                    is LoginActionEvent.LoginSucceed -> {
                        navigationState.navigateTo(
                            route = Screen.DashboardScreen.route,
                            navOptions = navOptions {
                                popUpTo(Screen.LoginScreen.route) { inclusive = true }
                            }
                        )
                    }
                }
            }
			LoginRootScreen(
				modifier = modifier,
				uiState = uiState,
				navigationState = navigationState,
				onEvent = viewModel::onEvent,
			)
		}
        composable(route = Screen.PinPromptScreen.route) { entry ->
            val viewModel = entry.sharedViewModel<LoginViewModel>(navigationState.navHostController)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            LoginPinPromptRootScreen(
                navigationState = navigationState,
                uiState = uiState,
                onEvent = viewModel::onEvent,
                modifier = modifier,
            )
        }
        composable(route = Screen.DashboardScreen.route) {
            val viewModel = hiltViewModel<DashboardViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            DashboardRootScreen(
                modifier = modifier,
                uiState = uiState,
                onEvent = viewModel::onEvent,
            )
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

