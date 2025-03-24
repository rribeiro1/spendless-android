package io.rafaelribeiro.spendless.domain.transaction

enum class TransactionExportRange(val displayName: String) {
    LAST_THREE_MONTHS("Last three months"),
    LAST_MONTH("Last month"),
    CURRENT_MONTH("Current month"),
    SPECIFIC_MONTH("Specific Month"),
}