package io.rafaelribeiro.spendless.domain

enum class ThousandSeparator(val value: String, val symbol: String) {
    DOT("1.000", "."),
    COMMA("1,000", ","),
    SPACE("1 000", " ");

    companion object {
        fun fromName(name: String): ThousandSeparator {
            return ThousandSeparator.entries.find {
                it.name.equals(name, ignoreCase = true)
            } ?: COMMA
        }
    }
}
