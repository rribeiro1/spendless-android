package io.rafaelribeiro.spendless.core.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import io.rafaelribeiro.spendless.domain.transaction.TransactionCategory
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun <T>SpendLessDropDown(
    title: String? = null,
    itemBackgroundColor: Color,
    values: List<T> = emptyList(),
    getLeadingIcon: (T) -> String,
    fixedLeadingIcon: String? = null,
    getText: (T) -> String,
    selectedValue: T? = null,
    selectedPosition: Int? = null,
    onItemSelected: (T) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val boxWidth = remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val sharedScrollState = rememberScrollState()
    val itemPosition = remember(selectedValue, selectedPosition) {
        mutableIntStateOf(
            when {
                selectedValue != null -> values.indexOf(selectedValue).takeIf { it >= 0 } ?: 0
                selectedPosition != null && selectedPosition in values.indices -> selectedPosition
                else -> 0
            }
        )
    }
    fun hasLeadingIcon(): Boolean {
        return getLeadingIcon(values[itemPosition.intValue]).isNotEmpty() || fixedLeadingIcon != null
    }
    fun openedDropDownMenuHeight(): Int {
        return if (values.size >= 4) 192 else values.size * 48
    }
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
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .spendlessShadow()
                .background(MaterialTheme.colorScheme.surfaceContainer, shape = RoundedCornerShape(16.dp))
                .onGloballyPositioned { layoutCoordinates -> boxWidth.intValue = layoutCoordinates.size.width }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { isDropDownExpanded.value = true }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = if (hasLeadingIcon()) 4.dp else 16.dp)
                ) {
                    if (hasLeadingIcon()) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(itemBackgroundColor)
                        ) {
                            Text(
                                text = fixedLeadingIcon ?: getLeadingIcon(values[itemPosition.intValue]),
                                style = MaterialTheme.typography.labelMedium,
                            )
                        }
                    }
                    Text(
                        text = getText(values[itemPosition.intValue]),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Expand dropdown",
                    modifier = Modifier.padding(end = 12.dp)
                )
            }
            /**
             * Override the default shape for the dropdown menu to have a rounded corner.
             * @see https://stackoverflow.com/questions/66781028/jetpack-compose-dropdownmenu-with-rounded-corners
             */
            MaterialTheme(
                shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(16.dp))
            ) {
                CustomDropdownMenu(
                    expanded = isDropDownExpanded.value,
                    onDismissRequest = { isDropDownExpanded.value = false },
                    modifier = Modifier
                        .width(with(density) { boxWidth.intValue.toDp() })
                ) {
                    Box(
                        modifier = Modifier
                            .height(openedDropDownMenuHeight().dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(sharedScrollState)
                        ) {
                            values.forEachIndexed { index, value ->
                                DropdownMenuItem(
                                    text = {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = if (hasLeadingIcon()) 0.dp else 16.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            ) {
                                                if (hasLeadingIcon()) {
                                                    Box(
                                                        contentAlignment = Alignment.Center,
                                                        modifier = Modifier
                                                            .size(40.dp)
                                                            .clip(RoundedCornerShape(12.dp))
                                                            .background(if (fixedLeadingIcon == null) itemBackgroundColor else Color.Transparent)
                                                    ) {
                                                        Text(
                                                            text = if (fixedLeadingIcon == null) getLeadingIcon(
                                                                value
                                                            ) else "",
                                                            style = MaterialTheme.typography.labelMedium,
                                                        )
                                                    }
                                                }
                                                Text(
                                                    text = getText(value),
                                                    style = MaterialTheme.typography.labelMedium
                                                )
                                            }
                                            if (getText(values[itemPosition.intValue]) == getText(value)) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Expand dropdown",
                                                    modifier = Modifier
                                                        .padding(end = 12.dp)
                                                        .size(24.dp),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    },
                                    onClick = {
                                        isDropDownExpanded.value = false
                                        itemPosition.intValue = index
                                        onItemSelected(value)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    contentPadding = PaddingValues(start = 4.dp, top = 0.dp, bottom = 0.dp)
                                )
                            }
                        }

                        // Custom Scrollbar
                        val scrollFraction = if (sharedScrollState.maxValue > 0) {
                            sharedScrollState.value.toFloat() / sharedScrollState.maxValue.toFloat()
                        } else 0f

                        val scrollbarHeight = 100.dp
                        val scrollbarOffset = (scrollFraction * (200.dp - scrollbarHeight)).coerceIn(20.dp, 180.dp - scrollbarHeight)

                        if (values.size > 4) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .offset(y = scrollbarOffset, x = (-4).dp)
                                    .width(4.dp)
                                    .height(scrollbarHeight)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDropdownMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    if (expanded) {
        Popup(
            onDismissRequest = onDismissRequest,
            popupPositionProvider = DropdownMenuProvider(),
        ) {
            Surface(
                modifier = modifier,
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                shadowElevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(0.dp)
                ) {
                    content()
                }
            }
        }
    }
}

@SuppressLint("ComposableNaming")
@Composable
fun DropdownMenuProvider() = object : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        return IntOffset(
            x = anchorBounds.left,
            y = anchorBounds.bottom + 10
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
fun DropDownPreview() {
    SpendLessTheme {
        SpendLessDropDown(
            values = TransactionCategory.categories,
            itemBackgroundColor = MaterialTheme.colorScheme.secondary,
            getLeadingIcon = { it.emoji },
            getText = { it.displayName },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
fun DropDownWithoutItemIconsPreview() {
    SpendLessTheme {
        SpendLessDropDown(
            values = TransactionCategory.categories,
            itemBackgroundColor = MaterialTheme.colorScheme.tertiaryContainer,
            getLeadingIcon = { "" },
            fixedLeadingIcon = "\uD83D\uDD04" /* 🔄 */,
            getText = { it.displayName },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 100)
@Composable
fun DropDownWithoutLeadingIconPreview() {
    SpendLessTheme {
        SpendLessDropDown(
            values = TransactionCategory.categories,
            itemBackgroundColor = MaterialTheme.colorScheme.secondary,
            getLeadingIcon = { "" },
            fixedLeadingIcon = null,
            getText = { it.displayName },
            modifier = Modifier.padding(16.dp)
        )
    }
}

