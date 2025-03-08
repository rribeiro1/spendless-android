package io.rafaelribeiro.spendless.domain.transaction

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector

enum class TransactionType(val icon: ImageVector, val display: String) {
    EXPENSE(icon = Icons.AutoMirrored.Filled.TrendingDown, display = "Expense"),
    INCOME(icon = Icons.AutoMirrored.Filled.TrendingUp, display = "Income")
}
