package io.rafaelribeiro.spendless.ui.features.registration

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.ui.DevicePreviews
import io.rafaelribeiro.spendless.ui.theme.SpendLessTheme

@Composable
fun RegistrationScreen(
	modifier: Modifier,
	onNextClick: (String) -> Unit,
	onHaveAccountClick: () -> Unit,
) {
	var username by remember { mutableStateOf("") }

	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
			.background(MaterialTheme.colorScheme.background)
			.padding(26.dp)
			.fillMaxSize(),
	) {
		Image(
			painter = painterResource(id = R.drawable.icon),
			contentDescription = "App Icon",
			modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
		)
		Text(
			text = stringResource(R.string.welcome_to_spendless),
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.titleMedium,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(bottom = 8.dp),
		)
		Text(
			text = stringResource(R.string.create_unique_username),
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.titleSmall,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(bottom = 36.dp),
		)
		BasicTextField(
			value = username,
			onValueChange = { username = it },
			textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
			modifier = Modifier
				.fillMaxWidth()
				.height(64.dp)
				.background(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
					shape = RoundedCornerShape(12.dp)
				)
				.padding(horizontal = 16.dp, vertical = 8.dp),
			decorationBox = { innerTextField ->
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center,
				) {
					if (username.isEmpty()) {
						Text(
							text = stringResource(R.string.username_placeholder),
							style = MaterialTheme.typography.bodyLarge,
							color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
						)
					}
					innerTextField()
				}
			},
		)
		Button(
			onClick = { onNextClick(username) },
			modifier = Modifier
				.padding(top = 16.dp)
				.height(48.dp)
				.fillMaxWidth(),
			colors = ButtonDefaults.buttonColors(
				contentColor = MaterialTheme.colorScheme.onPrimary,
				containerColor = MaterialTheme.colorScheme.primary,
				disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
				disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
			),
			shape = RoundedCornerShape(16.dp),
			enabled = username.isNotBlank(),
		) {
			Text(
				text = "Next" ,
				style = MaterialTheme.typography.labelSmall,
			)
		}
		Text(
			text = stringResource(R.string.already_have_an_account),
			color = MaterialTheme.colorScheme.primary,
			style = MaterialTheme.typography.labelSmall,
			textAlign = TextAlign.Center,
			modifier = Modifier
				.padding(top = 28.dp)
				.clickable(onClick = onHaveAccountClick),
		)
	}
}

@DevicePreviews
@Composable
fun RegistrationScreenPreview() {
	SpendLessTheme {
		RegistrationScreen(
			modifier = Modifier.fillMaxSize(),
			onNextClick = {},
			onHaveAccountClick = {},
		)
	}
}
