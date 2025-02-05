package io.rafaelribeiro.spendless.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import io.rafaelribeiro.spendless.R
import androidx.compose.ui.text.font.Font as FallBackFont

private val provider =
	GoogleFont.Provider(
		providerAuthority = "com.google.android.gms.fonts",
		providerPackage = "com.google.android.gms",
		certificates = R.array.com_google_android_gms_fonts_certs,
	)

val fontName = GoogleFont(name = "Figtree")
val fontFamily =
	FontFamily(
		Font(googleFont = fontName, fontProvider = provider),
		FallBackFont(R.font.figtree_medium, weight = FontWeight.Medium),
		FallBackFont(R.font.figtree_regular, weight = FontWeight.Normal),
		FallBackFont(R.font.figtree_light, weight = FontWeight.Light),
	)

val SpendLessTypography =
	Typography(
		titleSmall =
			TextStyle(
				fontFamily = fontFamily,
				fontWeight = FontWeight.Normal,
				fontSize = 16.sp,
				lineHeight = 24.sp,
			),
		titleMedium =
			TextStyle(
				fontFamily = fontFamily,
				fontWeight = FontWeight.SemiBold,
				fontSize = 28.sp,
				lineHeight = 34.sp,
			),
		bodyLarge =
			TextStyle(
				fontFamily = fontFamily,
				fontWeight = FontWeight.Bold,
				fontSize = 36.sp,
				lineHeight = 44.sp,
			),
		labelSmall =
			TextStyle(
				fontFamily = fontFamily,
				fontWeight = FontWeight.SemiBold,
				fontSize = 16.sp,
				lineHeight = 24.sp,
			),
	)
