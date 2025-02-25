package io.rafaelribeiro.spendless.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions

class NavigationState(
	val navHostController: NavHostController,
) {
	fun navigateTo(route: String, navOptions: NavOptions? = null) {
		navHostController.navigate(route, navOptions)
	}
	fun popBackStack() = navHostController.popBackStack()

    /**
     * Trigger the pin prompt screen anywhere in the app. This will clear the back stack and navigate to the pin prompt screen.
     */
    fun triggerPinPromptScreen() {
        navigateTo(
            route = Screen.PinPromptScreen.route,
            navOptions = navOptions {
                popUpTo(navHostController.graph.startDestinationId) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        )
    }
}

@Composable
fun rememberNavigationState(
	navHostController: NavHostController = rememberNavController(),
): NavigationState {
	return remember {
		NavigationState(navHostController)
	}
}
