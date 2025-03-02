package io.rafaelribeiro.spendless.core.presentation

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.ZoneId.systemDefault

fun formatDateTime(timestamp: Long): String {
    val instant = Instant.ofEpochMilli(timestamp)
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
    return LocalDateTime.ofInstant(instant, systemDefault()).format(formatter)
}

fun Long.timestampToLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this)
        .atZone(systemDefault())
        .toLocalDate()
}
