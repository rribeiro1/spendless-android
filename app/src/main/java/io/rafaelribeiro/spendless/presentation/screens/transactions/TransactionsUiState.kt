package io.rafaelribeiro.spendless.presentation.screens.transactions

import io.rafaelribeiro.spendless.presentation.screens.dashboard.GroupedTransactions

data class TransactionsUiState(
    val transactions: List<GroupedTransactions> = emptyList()
)
