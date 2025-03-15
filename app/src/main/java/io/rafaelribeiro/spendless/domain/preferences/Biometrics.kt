package io.rafaelribeiro.spendless.domain.preferences


enum class Biometrics(val value: Boolean) {
    ENABLE(true),
    DISABLE(false);

    companion object {
        private val valueMap: Map<Boolean, Biometrics> = Biometrics.entries.associateBy { it.value }
        fun fromValue(value: Boolean): Biometrics = valueMap[value] ?: DISABLE
        fun getIndexFromValue(value: Boolean): Int = fromValue(value).ordinal
    }

}
