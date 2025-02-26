package io.rafaelribeiro.spendless.domain

enum class ExpenseFormat(val value: String) {
    NEGATIVE("-$10"),
    PARENTHESES("($10)")
}
