package io.rafaelribeiro.spendless.domain.preferences


enum class CurrencySymbol(val symbol: String, val title: String) {
    DOLLAR("$", "US Dollar (USD)"),
    EURO("€", "Euro (EUR)"),
    POUND("£", "British Pound Sterling (GBP)"),
    YEN("¥", "Japanese Yen (JPY)"),
    SWISS_FRANC("CHF", "Swiss Franc (CHF)"),
    CANADIAN_DOLLAR("C$", "Canadian Dollar (CAD)"),
    AUSTRALIAN_DOLLAR("A$", "Australian Dollar (AUD)"),
    CHINESE_YUAN("¥", "Chinese Yuan Renminbi (CNY)"),
    INDIAN_RUPEE("₹", "Indian Rupee (INR)"),
    SOUTH_AFRICAN_RAND("R", "South African Rand (ZAR)");

    companion object {
        fun fromName(name: String): CurrencySymbol {
            return CurrencySymbol.entries.find {
                it.name.equals(name, ignoreCase = true)
            } ?: DOLLAR
        }
    }
}
