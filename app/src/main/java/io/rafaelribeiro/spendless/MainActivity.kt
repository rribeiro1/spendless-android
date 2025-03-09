package io.rafaelribeiro.spendless

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import io.rafaelribeiro.spendless.navigation.RootAppNavigation
import io.rafaelribeiro.spendless.navigation.rememberNavigationState
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme
import io.rafaelribeiro.spendless.presentation.widget.CreateTransactionWidget.WIDGET_INTENT_KEY

// TODO: Add a view model to handle app initialization state.
private var isAppReady: Boolean = true

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val launchedFromWidget = intent.getBooleanExtra(WIDGET_INTENT_KEY, false)
		installSplashScreen().apply {
			setKeepOnScreenCondition {
				!isAppReady
			}
		}
		enableEdgeToEdge()
		setContent {
			SpendLessTheme {
                RootAppNavigation(
					navigationState = rememberNavigationState(),
					launchedFromWidget = launchedFromWidget
				)
			}
		}
	}
}
