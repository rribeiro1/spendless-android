package io.rafaelribeiro.spendless.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SpendLessColorScheme =
	lightColorScheme(
		background = SoftLavender,
		primary = RoyalPurple,
		secondary = Lavender,
		tertiary = DeepPurple,
		onPrimary = White,
		onSurface = CharcoalBlack,
		error = DarkRed,
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
