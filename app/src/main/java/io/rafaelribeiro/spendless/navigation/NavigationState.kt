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

    fun navigateAndClearBackStack(route: String) {
        navHostController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

    /**
     * This Screen should be triggered when user's session has expired.
     * Trigger the pin prompt screen anywhere in the app. This will clear the back stack and navigate to the pin prompt screen.
     */
    fun triggerPinPromptScreen() {
        navigateTo(
            route = Screen.PinPromptScreen.route,
            navOptions = navOptions {
                launchSingleTop = true
                popUpTo(0) { inclusive = true }
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
