package io.rafaelribeiro.spendless.presentation.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Segment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rafaelribeiro.spendless.data.repository.TransactionCreator
import io.rafaelribeiro.spendless.domain.TransactionType.EXPENSE
import io.rafaelribeiro.spendless.domain.TransactionType.INCOME
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardUiEvent
import io.rafaelribeiro.spendless.presentation.screens.dashboard.DashboardViewModel.Companion.toUiModel
import io.rafaelribeiro.spendless.presentation.screens.dashboard.TransactionUiModel
import io.rafaelribeiro.spendless.presentation.theme.LightOlive
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme
import io.rafaelribeiro.spendless.presentation.theme.Success

@Composable
fun DashboardTransactionItem(
    transaction: TransactionUiModel,
    modifier: Modifier,
    onEvent: (DashboardUiEvent) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .padding(
                start = if (transaction.extended) 8.dp else 0.dp,
                end = if (transaction.extended) 8.dp else 0.dp,
                bottom = if (transaction.extended) 4.dp else 0.dp,
                top = 0.dp,
            )
            .fillMaxWidth()
            .shadow(
                elevation = if (transaction.extended) 2.dp else 0.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = DefaultShadowColor.copy(
                    red = 24 / 255f,
                    green = 0 / 255f,
                    blue = 64 / 255f,
                    alpha = if (transaction.extended) 0.2f else 0f
                ),
            )
            .background(if (transaction.extended) MaterialTheme.colorScheme.onPrimary else Color.Transparent)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = if (transaction.extended) 4.dp else 12.dp,
                        vertical = if (transaction.extended) 4.dp else 6.dp
                    )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .matchParentSize()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (transaction.type == EXPENSE) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = transaction.category.emoji,
                            fontSize = 18.sp,
                        )
                    }

                    if (transaction.note != null) {
                        val noteColor = when {
                            transaction.type == EXPENSE && transaction.extended -> MaterialTheme.colorScheme.primaryContainer
                            transaction.type == EXPENSE -> MaterialTheme.colorScheme.inversePrimary
                            transaction.type == INCOME && transaction.extended -> LightOlive
                            transaction.type == INCOME -> MaterialTheme.colorScheme.surfaceDim
                            else -> MaterialTheme.colorScheme.surfaceDim
                        }
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .align(Alignment.BottomEnd)
                                .offset(x = 4.dp, y = 4.dp)
                                .shadow(
                                    elevation = 16.dp,
                                    shape = RoundedCornerShape(6.dp),
                                    spotColor = DefaultShadowColor.copy(
                                        red = 24 / 255f,
                                        green = 0 / 255f,
                                        blue = 64 / 255f,
                                        alpha = 0.2f
                                    ),
                                )
                                .background(MaterialTheme.colorScheme.onPrimary)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { onEvent(DashboardUiEvent.TransactionNoteClicked(transaction.id)) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Segment,
                                contentDescription = "Note attached",
                                tint = noteColor,
                                modifier = Modifier
                                    .size(14.dp)
                                    .graphicsLayer(scaleX = -1f)
                            )
                        }
                    }
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = transaction.description,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = transaction.category.displayName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Text(
                    text = transaction.amountDisplay,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (transaction.type == INCOME) Success else MaterialTheme.colorScheme.onSurface,
                    modifier = modifier.padding(end = 4.dp)
                )
            }
            if (transaction.extended && transaction.note != null) {
                Text(
                    text = transaction.note,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(start = 60.dp, end = 12.dp, bottom = 12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionItemPreview() {
    SpendLessTheme {
        DashboardTransactionItem(
            transaction = TransactionCreator.createTransaction().toUiModel(),
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TransactionSelectedItemPreview() {
    SpendLessTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(top = 8.dp, bottom = 4.dp)
        ) {
            DashboardTransactionItem(
                transaction = TransactionCreator.createTransaction().toUiModel(),
                modifier = Modifier.background(MaterialTheme.colorScheme.background)
            )
            DashboardTransactionItem(
                transaction = TransactionCreator.createTransaction().toUiModel().copy(
                    extended = true,
                    note = "This is a note attached to this transaction"
                ),
                modifier = Modifier
            )
        }
    }
}
