package io.rafaelribeiro.spendless.domain

enum class LockoutDuration(val title: String, val value: Long) {
    SECONDS_15("15s", 15000L),
    SECONDS_30("30s", 30000L),
    SECONDS_60("1 min", 60000L),
    SECONDS_300("5 min", 300000L);

    companion object {
        private val valueMap: Map<Long, LockoutDuration> = entries.associateBy { it.value }
        fun fromValue(value: Long): LockoutDuration = valueMap[value] ?: SECONDS_30
    }
}
