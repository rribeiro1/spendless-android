package io.rafaelribeiro.spendless.domain

enum class DecimalSeparator(val value: String, val symbol: String) {
    DOT("1.00", "."),
    COMMA("1,00", ","),
}
