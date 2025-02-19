package io.rafaelribeiro.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class NavigationState(
	val navHostController: NavHostController,
) {
	fun navigateTo(route: String) {
		navHostController.navigate(route)
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
