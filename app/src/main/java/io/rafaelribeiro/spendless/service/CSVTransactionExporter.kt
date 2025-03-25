package io.rafaelribeiro.spendless.service

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import io.rafaelribeiro.spendless.domain.transaction.Transaction
import javax.inject.Inject

class CSVTransactionExporter @Inject constructor() : TransactionExporter {
    override val fileName = "transactions.csv"
    override val mimeType = "text/csv"

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun export(transactions: List<Transaction>, context: Context): Uri? {
        val csvContent = transactions.toCsv()
        return saveCsvToDownloads(context, fileName, csvContent)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun saveCsvToDownloads(context: Context, fileName: String, csvContent: String): Uri? {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val collection = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val itemUri = resolver.insert(collection, contentValues) ?: return null

        resolver.openOutputStream(itemUri)?.use { outputStream ->
            outputStream.write(csvContent.toByteArray())
        }

        contentValues.clear()
        contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
        resolver.update(itemUri, contentValues, null, null)

        return itemUri
    }

    private fun List<Transaction>.toCsv(): String {
        val header = listOf("id", "amount", "description", "note", "category", "type", "recurrence", "createdAt")
        val rows = this.map { transaction ->
            listOf(
                transaction.id.toString(),
                transaction.amount.toString(),
                transaction.description,
                transaction.note.orEmpty(),
                transaction.category.name,
                transaction.type.name,
                transaction.recurrence.name,
                transaction.createdAt.toString()
            )
        }
        return buildString {
            appendLine(header.joinToString(","))
            rows.forEach { appendLine(it.joinToString(",")) }
        }
    }
}