package io.rafaelribeiro.spendless.domain.transaction

import java.time.LocalDate
import java.time.YearMonth

data class TransactionRecurrence(
    val type: TransactionRecurrenceType,
    val label: String,
    val nextOccurrence: (LocalDate) -> LocalDate?
) {
    companion object {
        fun getRecurrenceOptions(selectedDate: LocalDate): List<TransactionRecurrence> {
            return listOf(
                TransactionRecurrence(
                    type = TransactionRecurrenceType.NONE,
                    label = "Does not repeat",
                    nextOccurrence = { getNextOccurrence(selectedDate, TransactionRecurrenceType.NONE) }
                ),
                TransactionRecurrence(
                    type = TransactionRecurrenceType.DAILY,
                    label = "Daily",
                    nextOccurrence = { getNextOccurrence(selectedDate, TransactionRecurrenceType.DAILY) }
                ),
                TransactionRecurrence(
                    type = TransactionRecurrenceType.WEEKLY,
                    label = "Weekly on ${selectedDate.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercase() }}",
                    nextOccurrence = { getNextOccurrence(selectedDate, TransactionRecurrenceType.WEEKLY) }
                ),
                TransactionRecurrence(
                    type = TransactionRecurrenceType.MONTHLY,
                    label = "Monthly on the ${ordinal(selectedDate.dayOfMonth)}",
                    nextOccurrence = { getNextOccurrence(selectedDate, TransactionRecurrenceType.MONTHLY) }
                ),
                TransactionRecurrence(
                    type = TransactionRecurrenceType.YEARLY,
                    label = "Yearly on ${selectedDate.month.name.lowercase().replaceFirstChar { it.uppercase() }} ${selectedDate.dayOfMonth}",
                    nextOccurrence = { getNextOccurrence(selectedDate, TransactionRecurrenceType.YEARLY) }
                )
            )
        }

        fun getNextOccurrence(date: LocalDate, type: TransactionRecurrenceType): LocalDate? {
            return when (type) {
                TransactionRecurrenceType.NONE -> null
                TransactionRecurrenceType.DAILY -> date.plusDays(1)
                TransactionRecurrenceType.WEEKLY -> date.plusWeeks(1)
                TransactionRecurrenceType.MONTHLY -> {
                    val nextMonth = date.plusMonths(1)
                    val safeDay = minOf(date.dayOfMonth, nextMonth.lengthOfMonth())
                    nextMonth.withDayOfMonth(safeDay)
                }
                TransactionRecurrenceType.YEARLY -> {
                    val nextYear = date.plusYears(1)
                    val yearMonth = YearMonth.of(nextYear.year, date.month)
                    val safeDay = minOf(date.dayOfMonth, yearMonth.lengthOfMonth())
                    nextYear.withDayOfMonth(safeDay)
                }
            }
        }
    }
}

fun ordinal(n: Int): String = when {
    n in 11..13 -> "${n}th"
    n % 10 == 1 -> "${n}st"
    n % 10 == 2 -> "${n}nd"
    n % 10 == 3 -> "${n}rd"
    else -> "${n}th"
}
