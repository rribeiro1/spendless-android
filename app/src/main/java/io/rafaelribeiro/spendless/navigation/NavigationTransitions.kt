package io.rafaelribeiro.spendless.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry


@Composable
internal fun exitTransition(
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    val isFadeOutScreen = isFadeTransitionActive(initialState, targetState)
    if (isFadeOutScreen)
        fadeOut(animationSpec = tween(700))
    else {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(500)
        )
    }
}
@Composable
internal fun popExitTransition(
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition = {
    val isFadeOutScreen = isFadeTransitionActive(initialState, targetState)
    if (isFadeOutScreen)
        fadeOut(animationSpec = tween(700))
    else {
        slideOutOfContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(500)
        )
    }
}

@Composable
internal fun enterTransition(
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    val isFadeOutScreen = isFadeTransitionActive(initialState, targetState)
    if (isFadeOutScreen)
        fadeIn(animationSpec = tween(700))
    else {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.Start,
            animationSpec = tween(500)
        )
    }
}

@Composable
internal fun popEnterTransition(
): AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition = {
    val isFadeOutScreen = isFadeTransitionActive(initialState, targetState)
    if (isFadeOutScreen)
        fadeIn(animationSpec = tween(700))
    else {
        slideIntoContainer(
            AnimatedContentTransitionScope.SlideDirection.End,
            animationSpec = tween(500)
        )
    }
}

private fun isFadeTransitionActive(initialState: NavBackStackEntry, targetState: NavBackStackEntry): Boolean {
    // Nested function to check if a route is a fade screen
    fun isFadeScreen(route: String?): Boolean {
        return when (route) {
            Screen.LoginScreen.route -> true
            Screen.TransactionsScreen.route -> true
            // Add more screens here as needed
            else -> false
        }
    }
    val initialScreen = initialState.destination.route
    val targetScreen = targetState.destination.route
    val isFadeOutScreen = isFadeScreen(targetScreen) || isFadeScreen(initialScreen)
    return isFadeOutScreen
}
