package io.rafaelribeiro.spendless

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import io.rafaelribeiro.spendless.navigation.RootAppNavigation
import io.rafaelribeiro.spendless.navigation.rememberNavigationState
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme
import io.rafaelribeiro.spendless.presentation.widget.CreateTransactionWidget.WIDGET_INTENT_KEY
import io.rafaelribeiro.spendless.workers.RecurringTransactionWorker
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainActivity : FragmentActivity() {

	private var launchedFromWidget by mutableStateOf(false)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		scheduleRecurringTransactionWorker(this)
		getIntent(intent)
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

	override fun onNewIntent(intent: Intent) {
		super.onNewIntent(intent)
		getIntent(intent)
	}

	private fun getIntent(intent: Intent) {
		launchedFromWidget = intent.getBooleanExtra(WIDGET_INTENT_KEY, false)
	}

	/**
	 * Schedules the recurring transaction worker to run every 24 hours.
	 * @param context The context to use to schedule the worker.
	 * @see RecurringTransactionWorker
	 */
	private fun scheduleRecurringTransactionWorker(context: Context) {
		val constraints = Constraints.Builder()
			.setRequiresBatteryNotLow(true)
			.setRequiresDeviceIdle(false)
			.setRequiresCharging(false)
			.build()
		val workRequest = PeriodicWorkRequestBuilder<RecurringTransactionWorker>(24, TimeUnit.HOURS)
			.setConstraints(constraints)
			.build()
		WorkManager.getInstance(context).enqueueUniquePeriodicWork(
			RecurringTransactionWorker.WORK_NAME,
			ExistingPeriodicWorkPolicy.KEEP,
			workRequest
		)
	}
}
