package io.rafaelribeiro.spendless.presentation.screens.dashboard

import io.rafaelribeiro.spendless.domain.Transaction

data class DashboardUiState(
    val username: String,
    val accountBalance: String,
    val previousWeekAmount: String,
    val latestTransactions: List<Transaction> = emptyList(),
    val largestTransaction: Transaction? = null,
)
