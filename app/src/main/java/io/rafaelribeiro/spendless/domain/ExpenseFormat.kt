package io.rafaelribeiro.spendless.domain

enum class ExpenseFormat(val value: String) {
    NEGATIVE("-$10"),
    PARENTHESES("($10)");

    companion object {
        fun fromName(name: String): ExpenseFormat {
          return entries.find { it.name.equals(name, ignoreCase = true) } ?: NEGATIVE
        }
    }
}
