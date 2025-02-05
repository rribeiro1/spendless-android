package io.rafaelribeiro.spendless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationScreen
import io.rafaelribeiro.spendless.presentation.screens.registration.RegistrationViewModel
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

// TODO: Add a view model to handle app initialization state.
private var isAppReady: Boolean = true

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		installSplashScreen().apply {
			setKeepOnScreenCondition {
				!isAppReady
			}
		}
		enableEdgeToEdge()
		setContent {
			SpendLessTheme {
				Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
					val viewModel: RegistrationViewModel = hiltViewModel()
					RegistrationScreen(
						modifier = Modifier.padding(innerPadding),
						onRegistrationClick = viewModel::exists,
						onLoginClick = {},
					)
				}
			}
		}
	}
}
