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
        FallBackFont(R.font.figtree_light, weight = FontWeight.Light),
        FallBackFont(R.font.figtree_regular, weight = FontWeight.Normal),
		FallBackFont(R.font.figtree_medium, weight = FontWeight.Medium),
        FallBackFont(R.font.figtree_semibold, weight = FontWeight.SemiBold),
        FallBackFont(R.font.figtree_bold, weight = FontWeight.Bold),
	)

val SpendLessTypography by lazy {
	Typography(
        headlineMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 28.sp,
            lineHeight = 34.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.1.sp
        ),
		titleSmall = TextStyle(
			fontFamily = fontFamily,
			fontWeight = FontWeight.Normal,
			fontSize = 16.sp,
			lineHeight = 24.sp,
		),
		titleMedium = TextStyle(
			fontFamily = fontFamily,
			fontWeight = FontWeight.SemiBold,
			fontSize = 16.sp,
			lineHeight = 24.sp,
		),
        titleLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 26.sp,
        ),
		bodyLarge = TextStyle(
			fontFamily = fontFamily,
			fontWeight = FontWeight.Bold,
			fontSize = 36.sp,
			lineHeight = 44.sp,
		),
        bodyMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            fontFeatureSettings = "tnum"
        ),
        bodySmall = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = "tnum"
        ),
		labelSmall = TextStyle(
			fontFamily = fontFamily,
			fontWeight = FontWeight.Medium,
			fontSize = 14.sp,
			lineHeight = 20.sp,
		),
        labelMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 24.sp,
        ),
        displayMedium = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            lineHeight = 44.sp,
        ),
        displayLarge = TextStyle(
            fontFamily = fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 45.sp,
            lineHeight = 52.sp,
        ),
	)
}
