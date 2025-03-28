package io.rafaelribeiro.spendless.navigation

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity.RESULT_CANCELED
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
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
import io.rafaelribeiro.spendless.MainActionEvent
import io.rafaelribeiro.spendless.MainActivity
import io.rafaelribeiro.spendless.MainViewModel
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.data.BiometricPromptManager
import io.rafaelribeiro.spendless.presentation.screens.authentication.AuthPinActionEvent
import io.rafaelribeiro.spendless.presentation.screens.authentication.AuthPinPromptScreen
import io.rafaelribeiro.spendless.presentation.screens.authentication.AuthPinPromptViewModel
import io.rafaelribeiro.spendless.presentation.screens.authentication.AuthPinUiEvent
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardActionEvent
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardScreen
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardViewModel
import io.rafaelribeiro.spendless.presentation.screens.login.LoginActionEvent
import io.rafaelribeiro.spendless.presentation.screens.login.LoginRootScreen
import io.rafaelribeiro.spendless.presentation.screens.login.LoginViewModel
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationActionEvent
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationPinConfirmationScreen
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationPinPromptScreen
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationPreferencesRootScreen
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationUsernameRootScreen
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsActionEvent
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsRootScreen
import io.rafaelribeiro.spendless.presentation.screens.settings.SettingsViewModel
import io.rafaelribeiro.spendless.presentation.screens.settings.account.SettingAccountRootScreen
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.SettingsPreferencesActionEvent
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.SettingsPreferencesScreen
import io.rafaelribeiro.spendless.presentation.screens.settings.preferences.SettingsPreferencesViewModel
import io.rafaelribeiro.spendless.presentation.screens.settings.security.SettingsSecurityActionEvent
import io.rafaelribeiro.spendless.presentation.screens.settings.security.SettingsSecurityScreen
import io.rafaelribeiro.spendless.presentation.screens.settings.security.SettingsSecurityViewModel
import io.rafaelribeiro.spendless.presentation.screens.transactions.TransactionsActionEvent
import io.rafaelribeiro.spendless.presentation.screens.transactions.TransactionsRootScreen
import io.rafaelribeiro.spendless.presentation.screens.transactions.TransactionsViewModel
import io.rafaelribeiro.spendless.presentation.screens.transactions.create.CreateTransactionActionEvent.CancelTransactionCreation
import io.rafaelribeiro.spendless.presentation.screens.transactions.create.CreateTransactionActionEvent.TransactionCreated
import io.rafaelribeiro.spendless.presentation.screens.transactions.create.CreateTransactionRootScreen
import io.rafaelribeiro.spendless.presentation.screens.transactions.create.CreateTransactionViewModel
import io.rafaelribeiro.spendless.presentation.screens.transactions.export.ExportTransactionActionEvent
import io.rafaelribeiro.spendless.presentation.screens.transactions.export.ExportTransactionRootScreen
import io.rafaelribeiro.spendless.presentation.screens.transactions.export.ExportTransactionViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun RootAppNavigation(
    modifier: Modifier = Modifier,
    navigationState: NavigationState,
    launchedFromWidget: Boolean = false,
) {
    val context = LocalContext.current
    val activity = LocalActivity.current as MainActivity
    val mainViewModel = hiltViewModel<MainViewModel>()
    val sessionState by mainViewModel.sessionState.collectAsState()
    val startScreen = sessionState.getStartScreen(launchedFromWidget) ?: return
    ObserveAsEvents(flow = mainViewModel.actionEvents) { event ->
        when (event) {
            MainActionEvent.SessionExpired -> navigationState.triggerPinPromptScreen()
        }
    }
    NavHost(
		navController = navigationState.navHostController,
		startDestination = startScreen,
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
                        mainViewModel.startSession()
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
                    is LoginActionEvent.LoginSucceed -> mainViewModel.startSession()
                }
            }
			LoginRootScreen(
				modifier = modifier,
				uiState = uiState,
				navigationState = navigationState,
				onEvent = viewModel::onEvent,
			)
		}
        composable(route = Screen.PinPromptScreen.route) {
            val viewModel = hiltViewModel<AuthPinPromptViewModel>()
            val uiState by viewModel.authPinUiState.collectAsStateWithLifecycle()
            val enrollLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult(),
                onResult = {
                    Log.d(Screen.PinPromptScreen.route, "Activity result: $it")
                    when (it.resultCode) {
                        RESULT_CANCELED -> {}
                        else -> showBiometricPrompt(viewModel, activity, context)
                    }
                }
            )
            ObserveAsEvents(flow = viewModel.actionEvents) { event ->
                when (event) {
                    AuthPinActionEvent.CorrectPinEntered -> mainViewModel.startSession()
                    AuthPinActionEvent.BiometricsTriggered -> showBiometricPrompt(viewModel, activity, context)
                    AuthPinActionEvent.LogoutClicked -> {
                        mainViewModel.terminateSession()
                        navigationState.navigateAndClearBackStack(Screen.LoginScreen.route)
                    }
                }
            }
            ObserveAsEvents(flow = viewModel.biometricEvents) { result ->
                when (result) {
                    BiometricPromptManager.BiometricResult.AuthenticationSuccess -> {
                        mainViewModel.startSession()
                        viewModel.onEvent(AuthPinUiEvent.CorrectBiometricsEntered)
                    }
                    BiometricPromptManager.BiometricResult.AuthenticationNotSet -> {
                        if (Build.VERSION.SDK_INT >= 30)
                            launchIntent(enrollLauncher)
                    }
                    else -> {}
                }
            }
            AuthPinPromptScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                modifier = modifier,
            )
        }
        composable(route = Screen.DashboardScreen.route) {
            val viewModel = hiltViewModel<DashboardViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ObserveAsEvents(flow = viewModel.actionEvents) { event ->
                when (event) {
                    is DashboardActionEvent.ShowAllTransactions -> {
                        navigationState.navigateTo(Screen.TransactionsScreen.route)
                    }
                    is DashboardActionEvent.AddTransaction -> {
                        navigationState.navigateTo(Screen.CreateTransactionScreen.route)
                    }
                    is DashboardActionEvent.OnSettingsClicked -> {
                        navigationState.navigateTo(Screen.SettingsFlow.route)
                    }
                    is DashboardActionEvent.ExportTransactions -> {
                        navigationState.navigateTo(Screen.ExportTransactionScreen.route)
                    }
                }
            }
            DashboardScreen(
                modifier = modifier,
                uiState = uiState,
                onEvent = viewModel::onEvent,
                launchedFromWidget = launchedFromWidget
            )
        }
        composable(route = Screen.TransactionsScreen.route) {
            val viewModel = hiltViewModel<TransactionsViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ObserveAsEvents(flow = viewModel.actionEvents) { event ->
                when (event) {
                    is TransactionsActionEvent.NavigateToAddTransaction -> {
                        navigationState.navigateTo(Screen.CreateTransactionScreen.route)
                    }
                    is TransactionsActionEvent.NavigateToDownloadTransaction -> {
                        navigationState.navigateTo(Screen.ExportTransactionScreen.route)
                    }
                }
            }
            TransactionsRootScreen(
                modifier = modifier,
                uiState = uiState,
                onEvent = viewModel::onEvent,
                navigationState = navigationState,
            )
        }
        dialog(route = Screen.CreateTransactionScreen.route) {
            val viewModel = hiltViewModel<CreateTransactionViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ObserveAsEvents(viewModel.actionEvents) {
                when (it) {
                    is CancelTransactionCreation, TransactionCreated -> {
                        navigationState.popBackStack()
                    }
                }
            }
            CreateTransactionRootScreen(
                modifier = modifier,
                uiState = uiState,
                onEvent = viewModel::onEvent,
            )
        }
        dialog(route = Screen.ExportTransactionScreen.route) {
            val viewModel = hiltViewModel<ExportTransactionViewModel>()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            ObserveAsEvents(viewModel.actionEvents) {
                when (it) {
                    is ExportTransactionActionEvent.CancelExportCreation -> {
                        navigationState.popBackStack()
                    }
                    is ExportTransactionActionEvent.TransactionExportSuccess -> {
                        Toast.makeText(context, R.string.file_exported, Toast.LENGTH_SHORT).show()
                        navigationState.popBackStack()
                    }
                    is ExportTransactionActionEvent.TransactionExportFailed -> {
                        Toast.makeText(context, R.string.fail_to_save_file, Toast.LENGTH_SHORT).show()
                        navigationState.popBackStack()
                    }
                }
            }
            ExportTransactionRootScreen(
                modifier = modifier,
                uiState = uiState,
                onEvent = viewModel::onEvent,
            )
        }
        navigation(
            startDestination = Screen.SettingsMainScreen.route,
            route = Screen.SettingsFlow.route,
        ) {
            composable(route = Screen.SettingsMainScreen.route) { entry ->
                val viewModel = entry.sharedViewModel<SettingsViewModel>(navigationState.navHostController)
                ObserveAsEvents(flow = viewModel.actionEvents) { event ->
                    when (event) {
                        is SettingsActionEvent.OnBackClicked -> navigationState.popBackStack()
                        is SettingsActionEvent.OnPreferencesClicked -> {
                            navigationState.navigateTo(Screen.SettingsPreferences.route)
                        }
                        is SettingsActionEvent.OnSecurityClicked -> {
                            navigationState.navigateTo(Screen.SettingsSecurity.route)
                        }
                        is SettingsActionEvent.OnLogoutClicked -> {
                            mainViewModel.terminateSession()
                        }
                        is SettingsActionEvent.OnAccountClicked -> {
                            navigationState.navigateTo(Screen.SettingsAccount.route)
                        }
                    }
                }
                SettingsRootScreen(
                    modifier = modifier,
                    onEvent = viewModel::onEvent
                )
            }
            composable(route = Screen.SettingsPreferences.route) {
                val viewModel = hiltViewModel<SettingsPreferencesViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ObserveAsEvents(flow = viewModel.actionEvents) { event ->
                    when (event) {
                        is SettingsPreferencesActionEvent.OnBackClicked -> navigationState.popBackStack()
                    }
                }
                SettingsPreferencesScreen(
                    modifier = modifier,
                    navigationState = navigationState,
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
                )
            }
            composable(route = Screen.SettingsSecurity.route) {
                val viewModel = hiltViewModel<SettingsSecurityViewModel>()
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                ObserveAsEvents(flow = viewModel.actionEvents) { event ->
                    when (event) {
                        is SettingsSecurityActionEvent.OnBackClicked -> navigationState.popBackStack()
                        is SettingsSecurityActionEvent.RestartSession -> {
                            mainViewModel.startSession()
                        }
                    }
                }
                SettingsSecurityScreen(
                    modifier = modifier,
                    navigationState = navigationState,
                    onEvent = viewModel::onSecurityEvent,
                    uiState = uiState,
                )
            }
            composable(route = Screen.SettingsAccount.route) { entry ->
                val viewModel = entry.sharedViewModel<SettingsViewModel>(navigationState.navHostController)
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                SettingAccountRootScreen(
                    uiState = uiState,
                    onEvent = viewModel::onEvent,
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

@RequiresApi(Build.VERSION_CODES.R)
private fun launchIntent(enrollLauncher: ActivityResultLauncher<Intent>) {
    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
        putExtra(
            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
            BIOMETRIC_STRONG or DEVICE_CREDENTIAL
        )
    }
    enrollLauncher.launch(enrollIntent)
}

private fun showBiometricPrompt(
    viewModel: AuthPinPromptViewModel,
    activity: MainActivity,
    context: Context
) {
    viewModel.biometricManager.showBiometricPrompt(
        activity,
        title = context.getString(R.string.biometric_title),
        description = context.getString(R.string.biometric_description),
    )
}