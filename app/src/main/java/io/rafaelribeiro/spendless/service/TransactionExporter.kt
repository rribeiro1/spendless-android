package io.rafaelribeiro.spendless.service

import android.content.Context
import android.net.Uri
import io.rafaelribeiro.spendless.domain.transaction.Transaction

interface TransactionExporter {
    fun export(transactions: List<Transaction>, context: Context): Uri?
    val fileName: String
    val mimeType: String
}