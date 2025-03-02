package io.rafaelribeiro.spendless.domain

interface ExpenseFormatterService {
    suspend fun format(amount: Double): String
}
