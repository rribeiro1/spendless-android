package io.rafaelribeiro.spendless.core.presentation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.domain.transaction.TransactionType
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun <T>SpendLessSegmentedButton(
    title: String? = null,
    options: List<T>,
    getLeadingIcon: (T) -> ImageVector? = { null },
    getText: (T) -> String,
    selectedIndex: Int,
    onOptionSelected: (Int) -> Unit,
    selectedItemColor: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    val backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.08f)
    val selectedColor = MaterialTheme.colorScheme.surfaceContainer

    var rowWidthPx by remember { mutableFloatStateOf(1f) }
    val density = LocalDensity.current
    val rowWidthDp = with(density) { rowWidthPx.toDp() }

    val segmentWidthDp = if (options.isNotEmpty()) rowWidthDp / options.size else 0.dp
    val offsetX = remember { Animatable(0f) }

    LaunchedEffect(selectedIndex, rowWidthPx) {
        val segmentWidthPx = rowWidthPx / options.size
        val targetX = selectedIndex * segmentWidthPx
        offsetX.animateTo(
            targetValue = targetX,
            animationSpec = tween(300)
        )
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, bottom = 8.dp),
            )
        }
        Box(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(backgroundColor, shape = RoundedCornerShape(16.dp))
                .padding(4.dp)
                .onSizeChanged { newSize ->
                    rowWidthPx = newSize.width.toFloat()
                }
        ) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(offsetX.value.toInt(), 0) }
                    .width(segmentWidthDp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(12.dp))
                    .background(selectedColor)
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                options.forEachIndexed { index, option ->
                    val isSelected = index == selectedIndex

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
                            .pointerInput(Unit) { detectTapGestures { onOptionSelected(index) } },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            getLeadingIcon(option)?.let { icon ->
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isSelected) selectedItemColor else MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Text(
                                text = getText(option),
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isSelected) selectedItemColor else MaterialTheme.colorScheme.tertiary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SegmentedButtonPreview() {
    SpendLessTheme {
        SpendLessSegmentedButton(
            options = TransactionType.entries,
            getText = { it.display },
            getLeadingIcon = { it.icon },
            selectedIndex = 0,
            onOptionSelected = {},
        )
    }
}
