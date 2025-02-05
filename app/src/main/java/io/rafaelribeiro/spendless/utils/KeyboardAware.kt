package io.rafaelribeiro.spendless.utils

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun KeyboardAware(
	content: @Composable () -> Unit,
) {
	Column(modifier = Modifier.imePadding()) {
		content()
	}
}
