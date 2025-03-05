package io.rafaelribeiro.spendless.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.rafaelribeiro.spendless.R
import io.rafaelribeiro.spendless.domain.CurrencySymbol
import io.rafaelribeiro.spendless.domain.DecimalSeparator
import io.rafaelribeiro.spendless.domain.ExpenseFormat
import io.rafaelribeiro.spendless.domain.ThousandSeparator
import io.rafaelribeiro.spendless.presentation.screens.registration.components.SpendLessDropDown
import io.rafaelribeiro.spendless.presentation.screens.registration.components.SpendLessSegmentedButton
import io.rafaelribeiro.spendless.presentation.theme.SpendLessTheme


@Composable
fun PreferencesScreen(
    buttonText: String,
    exampleExpenseFormat: String,
    expensesFormat: ExpenseFormat,
    decimalSeparator: DecimalSeparator,
    thousandSeparator: ThousandSeparator,
    currencySymbol: CurrencySymbol,
    buttonEnabled: Boolean = true,
    onButtonClicked: () -> Unit = {},
    onExpensesFormatSelected: (ExpenseFormat) -> Unit = {},
    onDecimalSeparatorSelected: (DecimalSeparator) -> Unit = {},
    onThousandSeparatorSelected: (ThousandSeparator) -> Unit = {},
    onCurrencySelected: (CurrencySymbol) -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 32.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = DefaultShadowColor.copy(
                    red = 24 / 255f,
                    green = 0 / 255f,
                    blue = 64 / 255f,
                    alpha = 0.4f
                ),
            )
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = exampleExpenseFormat,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 8.dp),
            )
            Text(
                text = stringResource(R.string.spend_this_month),
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
    }

    SpendLessSegmentedButton(
        title = stringResource(R.string.expenses_format),
        options = ExpenseFormat.entries.map { it.value },
        selectedIndex = ExpenseFormat.entries.indexOf(expensesFormat),
        onOptionSelected = {
            onExpensesFormatSelected(ExpenseFormat.entries[it])
        }
    )
    SpendLessDropDown(
        title = stringResource(R.string.currency),
        values = CurrencySymbol.entries,
        itemBackgroundColor = MaterialTheme.colorScheme.onPrimary,
        getText = { it.title },
        getLeadingIcon = { it.symbol },
        onItemSelected = { onCurrencySelected(it) },
        selectedValue = currencySymbol,
    )
    SpendLessSegmentedButton(
        title = stringResource(R.string.decimal_separator),
        options = DecimalSeparator.entries.map { it.value },
        selectedIndex = DecimalSeparator.entries.indexOf(decimalSeparator),
        onOptionSelected = {
            onDecimalSeparatorSelected(DecimalSeparator.entries[it])
        }
    )
    SpendLessSegmentedButton(
        title = stringResource(R.string.thousands_separator),
        options = ThousandSeparator.entries.map { it.value },
        selectedIndex = ThousandSeparator.entries.indexOf(thousandSeparator),
        onOptionSelected = {
            onThousandSeparatorSelected(ThousandSeparator.entries[it])
        }
    )
    SpendLessButton(
        text = buttonText,
        modifier = Modifier.padding(top = 34.dp),
        enabled = buttonEnabled,
        onClick = onButtonClicked,
    )
}

@Preview
@Composable
fun PreferencesScreenPreview() {
    SpendLessTheme {
        Column (modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()) {
            PreferencesScreen(
                exampleExpenseFormat = "-$10,382.45",
                expensesFormat = ExpenseFormat.NEGATIVE,
                decimalSeparator = DecimalSeparator.DOT,
                thousandSeparator = ThousandSeparator.COMMA,
                currencySymbol = CurrencySymbol.DOLLAR,
                buttonText = "Save",
            )
        }
    }
}
