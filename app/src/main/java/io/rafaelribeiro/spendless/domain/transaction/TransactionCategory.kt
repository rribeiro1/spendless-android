package io.rafaelribeiro.spendless.domain.transaction

enum class TransactionCategory(val emoji: String, val displayName: String) {
    HOME("🏠", "Home"),
    FOOD("🍕", "Food & Groceries"),
    ENTERTAINMENT("💻", "Entertainment"),
    CLOTHING("👔", "Clothing & Accessories"),
    HEALTH("❤️", "Health & Wellness"),
    PERSONAL_CARE("🛁", "Personal Care"),
    TRANSPORTATION("🚗", "Transportation"),
    EDUCATION("🎓", "Education"),
    SAVINGS("💎", "Saving & Investments"),
    OTHER("⚙️", "Other"),
    INCOME("💰", "Income");

    companion object {
        val categories = entries
    }
}
