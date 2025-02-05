package io.rafaelribeiro.spendless.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.domain.RegistrationError
import io.rafaelribeiro.spendless.domain.Result

sealed class UiText {
	data class DynamicString(
		val value: String,
	) : UiText()

	class StringResource(
		@StringRes val id: Int,
		val args: Array<Any> = arrayOf(),
	) : UiText()

	@Composable
	fun asString(): String =
		when (this) {
			is DynamicString -> value
			is StringResource -> LocalContext.current.getString(id, *args)
		}

	fun asString(context: Context): String =
		when (this) {
			is DynamicString -> value
			is StringResource -> context.getString(id, *args)
		}

	companion object {
		val Empty = DynamicString("")
	}

	fun isNotEmpty() = this != Empty
}

fun RegistrationError.asUiText(): UiText =
	when (this) {
		RegistrationError.USERNAME_ALREADY_EXISTS -> UiText.StringResource(R.string.username_has_already_been_taken)
	}

fun Result.Failure<*, RegistrationError>.asErrorUiText(): UiText = error.asUiText()
