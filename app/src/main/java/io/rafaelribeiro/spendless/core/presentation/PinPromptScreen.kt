package io.rafaelribeiro.spendless.core.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun PinPromptScreen(
	modifier: Modifier,
	title: String,
	description: String,
    pinLockRemainingSeconds: Int = 0,
	currentPinSize: Int = 0,
    pinPadEnabled: Boolean = true,
	biometricsEnabled: Boolean = false,
	onNumberClick: (String) -> Unit = {},
	onBackspaceClick: () -> Unit = {},
	onBiometricsClick: () -> Unit = {},
) {
	Column(
		horizontalAlignment = Alignment.CenterHorizontally,
		modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 26.dp)
            .fillMaxSize(),
	) {
		Image(
			painter = painterResource(id = R.drawable.icon),
			contentDescription = "App Icon",
			modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
		)
		Text(
			text = title,
			color = MaterialTheme.colorScheme.onSurface,
			style = MaterialTheme.typography.headlineMedium,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(bottom = 8.dp),
		)
		FormattedText(
            allText = description,
            textToBeFormattedAsBold = if (pinLockRemainingSeconds == 0) null else pinLockRemainingSeconds.formatSecondsToMMSS(),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 55.dp),
        )
		PinCircleIndicator(
			pinSize = 5,
			switched = currentPinSize,
            pinPadEnabled = pinPadEnabled,
			modifier = Modifier.padding(bottom = 51.dp),
		)
		PinPad(
			onNumberClick = { onNumberClick(it) },
			onBackspaceClick = { onBackspaceClick() },
			onBiometricsClick = { onBiometricsClick() },
            pinPadEnabled = pinPadEnabled,
			biometricsEnabled = biometricsEnabled,
		)
		Spacer(modifier = Modifier.height(62.dp))
	}
}

@Composable
fun PinCircleIndicator(
	pinSize: Int = 5,
	switched: Int = 0,
    pinPadEnabled: Boolean,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
	) {
		repeat(pinSize) {
			PinCircle(switched = it < switched, pinPadEnabled = pinPadEnabled)
		}
	}
}

@Composable
private fun PinCircle(
	switched: Boolean = false,
    pinPadEnabled: Boolean,
) {
	val pinColor by animateColorAsState(
		targetValue = when {
            !pinPadEnabled -> MaterialTheme.colorScheme.onBackground.copy(0.04f)
            switched -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f)
        },
		animationSpec = tween(durationMillis = 400),
	)
	Box(
		modifier = Modifier
            .size(18.dp)
            .clip(CircleShape)
            .background(pinColor),
	)
}

@Composable
fun PinPad(
	onNumberClick: (String) -> Unit,
	onBackspaceClick: () -> Unit,
	onBiometricsClick: () -> Unit,
    pinPadEnabled: Boolean = true,
	biometricsEnabled: Boolean = false,
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
	) {
		repeat(3) { rowIndex ->
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
			) {
				repeat(3) { columnIndex ->
					PinPadButton(
						text = "${rowIndex * 3 + columnIndex + 1}",
                        enabled = pinPadEnabled,
						onClick = { onNumberClick("${rowIndex * 3 + columnIndex + 1}") },
					)
				}
			}
		}
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement
				.spacedBy(4.dp, Alignment.CenterHorizontally),
		) {
			if (biometricsEnabled) {
				PinPadButton(
					icon = Icons.Filled.Fingerprint,
					iconSize = 40.dp,
					color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
					disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
					onClick = { onBiometricsClick() },
				)
			} else {
				Spacer(modifier = Modifier.size(108.dp))
			}
			PinPadButton(
				text = "0",
                enabled = pinPadEnabled,
				onClick = { onNumberClick("0") },
			)
			PinPadButton(
				icon = Icons.AutoMirrored.Filled.Backspace,
				color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
                disabledContainerColor = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                enabled = pinPadEnabled,
				onClick = { onBackspaceClick() },
			)
		}
	}
}

@Composable
fun PinPadButton(
	text: String = "",
	icon: ImageVector? = null,
	iconSize: Dp? = null,
	iconDescription: String = "",
	color: Color = MaterialTheme.colorScheme.secondary,
    disabledContainerColor: Color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f),
    enabled: Boolean = true,
	onClick: () -> Unit,
) {
	Button(
		onClick = { onClick() },
		shape = RoundedCornerShape(32.dp),
		colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f),
        ),
		modifier = Modifier.size(108.dp),
        enabled = enabled,
	) {
		if (icon != null) {
			Icon(
				modifier = iconSize?.let { Modifier.size(iconSize) } ?: Modifier,
				imageVector = icon,
				contentDescription = iconDescription,
				tint = if (enabled) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
			)
		} else {
			Text(
				text = text,
				style = MaterialTheme.typography.headlineLarge,
				color = if (enabled) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f),
			)
		}
	}
}

@Preview
@Composable
fun RegistrationPinScreenPreview() {
	SpendLessTheme {
		PinPromptScreen(
			modifier = Modifier.fillMaxSize(),
			title = "Create PIN",
			description = "Use PIN to login to your account",
		)
	}
}
