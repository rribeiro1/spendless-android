package io.rafaelribeiro.spendless.presentation.screens.registration.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import io.rafaelribeiro.spendless.domain.TransactionCategory
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun <T>SpendLessDropDown(
    title: String? = null,
    itemBackgroundColor: Color,
    values: List<T> = emptyList(),
    getLeadingIcon: (T) -> String,
    getText: (T) -> String,
    onItemSelected: (T) -> Unit = {}
) {
    val isDropDownExpanded = remember { mutableStateOf(false) }
    val itemPosition = remember { mutableIntStateOf(0) }
    val boxWidth = remember { mutableIntStateOf(0) }
    val density = LocalDensity.current
    val sharedScrollState = rememberScrollState()

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
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .shadow(
                    elevation = 32.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = DefaultShadowColor.copy(
                        red = 24 / 255f,
                        green = 0 / 255f,
                        blue = 64 / 255f,
                        alpha = 0.3f
                    ),
                )
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
                    modifier = Modifier.padding(start = 4.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(itemBackgroundColor)
                    ) {
                        Text(
                            text = getLeadingIcon(values[itemPosition.intValue]),
                            style = MaterialTheme.typography.labelMedium,
                        )
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
                            .height(200.dp)
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
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            ) {
                                                Box(
                                                    contentAlignment = Alignment.Center,
                                                    modifier = Modifier
                                                        .size(40.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(itemBackgroundColor)
                                                ) {
                                                    Text(
                                                        text = getLeadingIcon(value),
                                                        style = MaterialTheme.typography.labelMedium,
                                                    )
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

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun DropDownDemoPreview() {
    SpendLessTheme {
        SpendLessDropDown(
            values = TransactionCategory.categories,
            itemBackgroundColor = MaterialTheme.colorScheme.secondary,
            getLeadingIcon = { it.emoji },
            getText = { it.displayName }
        )
    }
}
