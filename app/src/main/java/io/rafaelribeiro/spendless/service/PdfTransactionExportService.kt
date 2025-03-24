package io.rafaelribeiro.spendless.service

import android.content.Context
import android.net.Uri
import io.rafaelribeiro.spendless.domain.transaction.Transaction
import javax.inject.Inject

class PdfTransactionExportService @Inject constructor() : TransactionExportService {
    override val fileName = "transactions.pdf"
    override val mimeType = "application/pdf"

    override fun export(transactions: List<Transaction>, context: Context): Uri? {
        // Use Android's PdfDocument or a library like iText or PdfBox
        TODO("Implement PDF export logic")
    }
}