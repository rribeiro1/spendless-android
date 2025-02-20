package io.rafaelribeiro.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController

class NavigationState(
	val navHostController: NavHostController,
) {
	fun navigateTo(route: String, navOptions: NavOptions? = null) {
		navHostController.navigate(route, navOptions)
	}
	fun popBackStack() = navHostController.popBackStack()
}

@Composable
fun rememberNavigationState(
	navHostController: NavHostController = rememberNavController(),
): NavigationState {
	return remember {
		NavigationState(navHostController)
	}
}
