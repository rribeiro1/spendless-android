package io.rafaelribeiro.spendless.domain.preferences


enum class SessionExpiryDuration(val display: String, val value: Int) {
    MINUTES_5("5 min", 5),
    MINUTES_15("15 min", 15),
    MINUTES_30("30 min", 30),
    MINUTES_60("1 hour", 60);

    companion object {
        private val valueMap: Map<Int, SessionExpiryDuration> = SessionExpiryDuration.entries.associateBy { it.value }
        fun fromValue(value: Int): SessionExpiryDuration = valueMap[value] ?: MINUTES_5
    }
}
