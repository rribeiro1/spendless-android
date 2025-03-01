package io.rafaelribeiro.spendless.core.presentation

import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.ZoneId.systemDefault

fun formatDateTime(instant: Instant): String {
    val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH)
    return LocalDateTime.ofInstant(instant, systemDefault()).format(formatter)
}
