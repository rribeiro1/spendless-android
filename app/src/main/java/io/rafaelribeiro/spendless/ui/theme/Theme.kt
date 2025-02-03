package io.rafaelribeiro.spendless.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SpendLessColorScheme =
	lightColorScheme(
		background = SoftLavender,
		primary = RoyalPurple,
		onPrimary = White,
		onSurface = CharcoalBlack,
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
