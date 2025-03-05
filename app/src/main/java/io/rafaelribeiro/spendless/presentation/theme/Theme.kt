package io.rafaelribeiro.spendless.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SpendLessColorScheme =
	lightColorScheme(
		background = SoftLavender,
		primary = RoyalPurple,
		secondary = Lavender,
		onPrimary = White,
		onSurface = CharcoalBlack,
        primaryContainer = VividPurple,
        surfaceContainer = White,
        secondaryContainer = LimeGreen,
        onSecondaryContainer = Olive,
		error = DarkRed,
        tertiary = DeepPurple,
        surfaceVariant = Yellow, // Secondary Fixed in Figma.
        inversePrimary = PastelPurple,
        surfaceDim = VibrantYellowGreen, // Secondary Fixed Dim in Figma.
        surfaceContainerLow = GrayLight,
        onSurfaceVariant = GrayDark,
	)

@Composable
fun SpendLessTheme(content: @Composable () -> Unit) {
	MaterialTheme(
		colorScheme = SpendLessColorScheme,
		typography = SpendLessTypography,
		content = content,
	)
}
