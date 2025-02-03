package io.rafaelribeiro.spendless

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import io.rafaelribeiro.spendless.ui.features.registration.RegistrationScreen
import io.rafaelribeiro.spendless.ui.theme.SpendLessTheme

// TODO: Add a view model to handle app initialization state.
private var isAppReady: Boolean = true

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
					RegistrationScreen(
						modifier = Modifier.padding(innerPadding),
						onNextClick = {
							Log.i("MainActivity", "User clicked on 'Next'")
						},
						onHaveAccountClick = {
							Log.i("MainActivity", "User clicked on 'Have an account?'")
						},
					)
				}
			}
		}
	}
}
