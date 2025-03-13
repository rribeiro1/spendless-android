package io.rafaelribeiro.spendless.presentation.screens.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.core.presentation.AutoResizingText
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme

@Composable
fun DashboardPreviousWeek(
    modifier: Modifier,
    amountPreviousWeekDisplay: String,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 12.dp)
    ) {
        AutoResizingText(
            text = amountPreviousWeekDisplay,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 18.sp
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxFontSize = 18.sp,
            minFontSize = 14.sp
        )
        AutoResizingText(
            text = stringResource(R.string.previous_week),
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 12.sp,
                lineHeight = 16.sp
            ),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            maxFontSize = 12.sp,
            minFontSize = 8.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true, widthDp = 200, heightDp = 100)
@Composable
fun DashboardPreviousWeekPreview() {
    SpendLessTheme {
        DashboardPreviousWeek(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp),
            amountPreviousWeekDisplay = "$1.000,00"
        )
    }
}
