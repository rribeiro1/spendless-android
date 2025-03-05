package io.rafaelribeiro.spendless.domain

enum class LockoutDuration(val title: String, val value: Int) {
    SECONDS_15("15s", 15),
    SECONDS_30("30s", 30),
    SECONDS_60("1 min", 60),
    SECONDS_300("5 min", 300);

    companion object {
        private val valueMap: Map<Int, LockoutDuration> = entries.associateBy { it.value }
        fun fromValue(value: Int): LockoutDuration = valueMap[value] ?: SECONDS_30
    }
}
