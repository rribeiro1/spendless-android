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

    // Todo: another way to observe WorkManager. It can be integrated this way too!
//    val workInfoLiveData = WorkManager.getInstance(this)
//        .getWorkInfoByIdLiveData(mainViewModel.userSessionWorkerRequest.id)
//    val workManager = WorkManager.getInstance(this).getWorkInfosByTagFlow("asd")

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val launchedFromWidget = intent.getBooleanExtra(WIDGET_INTENT_KEY, false)
		installSplashScreen().apply {
			setKeepOnScreenCondition {
				!isAppReady
			}
		}
        (applicationContext as SpendLessApplication).sessionTimeout.observe(this) { timedOut ->
            if (timedOut)
                mainViewModel.expireSession()
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
