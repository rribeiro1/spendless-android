package io.rafaelribeiro.spendless.core.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun ErrorDialog(
	errorMessage: UiText,
	modifier: Modifier = Modifier,
) {
	AnimatedVisibility(
		visible = errorMessage.isNotEmpty(),
		enter = fadeIn(animationSpec = tween(durationMillis = 300)) ,
		exit = fadeOut(animationSpec = tween(durationMillis = 300)),
		modifier = modifier
	) {
		KeyboardAware {
			Box(
				modifier = Modifier
					.fillMaxWidth()
					.height(48.dp)
					.background(MaterialTheme.colorScheme.error)
					.padding(vertical = 12.dp, horizontal = 16.dp),
				contentAlignment = Alignment.Center
			) {
				Text(
					text = errorMessage.asString(),
					color = MaterialTheme.colorScheme.onPrimary,
					style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Medium),
					modifier = Modifier.align(Alignment.Center)
				)
			}
		}
	}
}
