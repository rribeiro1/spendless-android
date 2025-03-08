package io.rafaelribeiro.spendless.domain.preferences

enum class ExpenseFormat(val display: String) {
    NEGATIVE("-$10"),
    PARENTHESES("($10)");

    companion object {
        fun fromName(name: String): ExpenseFormat {
          return entries.find { it.name.equals(name, ignoreCase = true) } ?: NEGATIVE
        }
    }
}
