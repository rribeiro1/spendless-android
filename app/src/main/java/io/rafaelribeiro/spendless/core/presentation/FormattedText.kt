package io.rafaelribeiro.spendless.domain

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import java.util.Locale

@Composable
fun FormattedText(
    allText: String,
    textToBeFormattedAsBold: String? = null,
    color: Color = Color.Unspecified,
    style: TextStyle = TextStyle.Default,
    textAlign: TextAlign = TextAlign.Start,
    modifier: Modifier = Modifier,
) {
    val annotatedString = if (textToBeFormattedAsBold != null) {
         buildAnnotatedString {
            append(allText.substringBefore("%s"))
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(textToBeFormattedAsBold)
            }
            append(allText.substringAfter("%s"))
        }
    } else AnnotatedString(allText)
    Text(
        text = annotatedString,
        color = color,
        style = style,
        textAlign = textAlign,
        modifier = modifier,
    )
}

fun Int.formatSecondsToMMSS(): String {
    val minutes = this / 60
    val remainingSeconds = this % 60
    return String.format(Locale.getDefault(),"%02d:%02d", minutes, remainingSeconds)
}
