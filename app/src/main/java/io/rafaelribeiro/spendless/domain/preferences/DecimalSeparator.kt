package io.rafaelribeiro.spendless.domain

enum class DecimalSeparator(val display: String, val symbol: String) {
    DOT("1.00", "."),
    COMMA("1,00", ",");

    companion object {
        fun fromName(name: String): DecimalSeparator {
            return DecimalSeparator.entries.find {
                it.name.equals(name, ignoreCase = true)
            } ?: DOT
        }
    }
}
