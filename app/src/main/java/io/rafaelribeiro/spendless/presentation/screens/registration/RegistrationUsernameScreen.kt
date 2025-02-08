package io.rafaelribeiro.spendless.presentation.screens.registration

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun RegistrationUsernameRootScreen(
	uiState: RegistrationUiState,
	onEvent: (RegistrationUiEvent) -> Unit,
	modifier: Modifier,
) {
	RegistrationUsernameScreen(
		modifier = modifier,
		uiState = uiState,
		onNextClick = { onEvent(RegistrationUiEvent.ActionButtonNextClicked) },
		onUsernameChange = { onEvent(RegistrationUiEvent.UsernameChanged(it)) },
		onLoginLinkClick = { onEvent(RegistrationUiEvent.LoginLinkClicked) },
	)
}

@Composable
fun RegistrationUsernameScreen(
	modifier: Modifier,
	uiState: RegistrationUiState,
	onNextClick: () -> Unit,
	onUsernameChange: (String) -> Unit,
	onLoginLinkClick: () -> Unit,
) {
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
			value = uiState.username,
			onValueChange = {
				onUsernameChange(it)
			},
			textStyle = MaterialTheme.typography.bodyLarge.copy(textAlign = TextAlign.Center),
			singleLine = true,
			modifier = Modifier
				.fillMaxWidth()
				.height(64.dp)
				.background(
					color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f),
					shape = RoundedCornerShape(12.dp),
				)
				.padding(horizontal = 16.dp, vertical = 8.dp),
			decorationBox = { innerTextField ->
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center,
				) {
					if (uiState.username.isEmpty()) {
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
			onClick = onNextClick,
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
			enabled = uiState.nextButtonEnabled,
		) {
			Text(
				text = stringResource(R.string.next),
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
				.clickable(onClick = onLoginLinkClick),
		)
	}
}

@Preview
@Composable
fun UsernameScreenPreview() {
	SpendLessTheme {
		RegistrationUsernameScreen(
			modifier = Modifier.fillMaxSize(),
			uiState = RegistrationUiState(),
			onNextClick = {},
			onUsernameChange = {},
			onLoginLinkClick = {},
		)
	}
}
