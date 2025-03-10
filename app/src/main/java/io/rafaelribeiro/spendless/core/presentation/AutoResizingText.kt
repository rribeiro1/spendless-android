package io.rafaelribeiro.spendless.core.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AutoResizingText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    color: Color,
    textOverflow: TextOverflow = TextOverflow.Visible,
    maxFontSize: TextUnit = 12.sp,
    minFontSize: TextUnit = 8.sp
) {
    var textStyle by remember {
        mutableStateOf(
            style.copy(
                fontSize = maxFontSize,
                color = color
            )
        )
    }
    /**
     * This flag is used to stop shrinking once the text fits properly,
     * preventing unnecessary recompositions.
     */
    var readyToDraw by remember { mutableStateOf(false) }
    Box(modifier = modifier) {
        Text(
            text = text,
            maxLines = 1,
            overflow = textOverflow,
            style = textStyle,
            softWrap = false,
            onTextLayout = { result ->
                if (!readyToDraw && result.didOverflowWidth) {
                    val newFontSize = (textStyle.fontSize.value - 1).coerceAtLeast(minFontSize.value).sp
                    textStyle = textStyle.copy(fontSize = newFontSize)
                } else {
                    readyToDraw = true
                }
            }
        )
    }
}

@Composable
@Preview(showBackground = true)
fun AutoResizingTextPreview() {
    Column(
        modifier = Modifier
            .width(200.dp)
    ) {
        AutoResizingText(
            text = "This is a very long text that should be resized",
            modifier = Modifier,
            maxFontSize = 12.sp,
            minFontSize = 6.sp,
            textOverflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}