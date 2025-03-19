package io.rafaelribeiro.spendless

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint
import io.rafaelribeiro.spendless.navigation.RootAppNavigation
import io.rafaelribeiro.spendless.navigation.rememberNavigationState
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme
import io.rafaelribeiro.spendless.presentation.widget.CreateTransactionWidget.WIDGET_INTENT_KEY

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val launchedFromWidget = intent.getBooleanExtra(WIDGET_INTENT_KEY, false)
		installSplashScreen()
		enableEdgeToEdge()
		setContent {
			SpendLessTheme {
                RootAppNavigation(
					navigationState = rememberNavigationState(),
					launchedFromWidget = launchedFromWidget,
				)
			}
		}
	}
}
