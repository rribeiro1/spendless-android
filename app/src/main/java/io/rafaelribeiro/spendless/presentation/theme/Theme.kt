package io.rafaelribeiro.spendless.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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
	/* Other default colors to override
	background = Color(0xFFFFFBFE),
	surface = Color(0xFFFFFBFE),
	onSecondary = Color.White,
	onTertiary = Color.White,
	onBackground = Color(0xFF1C1B1F),
	onSurface = Color(0xFF1C1B1F),
	 */
	)

@Composable
fun SpendLessTheme(content: @Composable () -> Unit) {
	MaterialTheme(
		colorScheme = SpendLessColorScheme,
		typography = SpendLessTypography,
		content = content,
	)
}
