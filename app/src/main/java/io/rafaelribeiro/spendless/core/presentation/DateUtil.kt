package io.rafaelribeiro.spendless.core.presentation

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId.systemDefault

fun Long.timestampToLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(systemDefault())
        .toLocalDate()
}
