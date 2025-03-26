package io.rafaelribeiro.spendless.service

import android.content.ContentValues
import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import io.rafaelribeiro.spendless.domain.transaction.Transaction
import io.rafaelribeiro.spendless.domain.transaction.TransactionType
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class PDFTransactionExporter @Inject constructor() : TransactionExporter {
    override val fileName = "transactions.pdf"
    override val mimeType = "application/pdf"

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    private val headerPaint = Paint().apply {
        color = Color.BLACK
        textSize = 24f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val subHeaderPaint = Paint().apply {
        color = Color.BLACK
        textSize = 16f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }
    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 12f
    }
    private val incomePaint = Paint().apply {
        color = Color.rgb(0, 128, 0)
        textSize = 12f
    }
    private val expensePaint = Paint().apply {
        color = Color.rgb(192, 0, 0)
        textSize = 12f
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun export(transactions: List<Transaction>, context: Context): Uri? {
        return generatePdf(context, fileName, transactions)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun generatePdf(context: Context, fileName: String, transactions: List<Transaction>): Uri? {
        val pdfDocument = PdfDocument()
        val startX = 40f
        val pageWidth = 595f
        val pageHeight = 842f

        fun startNewPage(pageNumber: Int): Pair<PdfDocument.Page, Float> {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth.toInt(), pageHeight.toInt(), pageNumber).create()
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            var y = 60f

            // Draw header
            canvas.drawText("Transaction Report", startX, y, headerPaint)
            y += 40f

            // Draw subheader with column headers
            val columnWidths = listOf(150f, 100f, 120f, 150f)
            val headers = listOf("Date/Time", "Amount", "Description", "Category")

            var currentX = startX
            headers.forEachIndexed { index, header ->
                canvas.drawText(header, currentX, y, subHeaderPaint)
                currentX += columnWidths[index]
            }

            // Draw separator line
            y += 10f
            canvas.drawLine(startX, y, pageWidth - startX, y, textPaint)
            y += 20f

            return Pair(page, y)
        }

        var pageNumber = 1
        var (currentPage, yPosition) = startNewPage(pageNumber)
        var canvas = currentPage.canvas

        val columnWidths = listOf(150f, 100f, 120f, 150f)

        transactions.sortedByDescending { it.createdAt }.forEachIndexed { _, tx ->
            if (yPosition > pageHeight - 60f) {
                pdfDocument.finishPage(currentPage)
                pageNumber++
                val (newPage, newY) = startNewPage(pageNumber)
                currentPage = newPage
                canvas = newPage.canvas
                yPosition = newY
            }

            var currentX = startX
            
            // Format and draw each column
            val formattedDate = dateFormat.format(tx.createdAt)
            canvas.drawText(formattedDate, currentX, yPosition, textPaint)
            currentX += columnWidths[0]

            val formattedAmount = String.format("%.2f", tx.amount)
            val amountPaint = when (tx.type) {
                TransactionType.INCOME -> incomePaint
                TransactionType.EXPENSE -> expensePaint
            }
            canvas.drawText(formattedAmount, currentX, yPosition, amountPaint)
            currentX += columnWidths[1]

            canvas.drawText(tx.description.take(30), currentX, yPosition, textPaint)
            currentX += columnWidths[2]

            canvas.drawText(tx.category.displayName, currentX, yPosition, textPaint)

            yPosition += 20f
        }

        pdfDocument.finishPage(currentPage)

        // Save to Downloads
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, mimeType)
            put(MediaStore.Downloads.IS_PENDING, 1)
        }

        val downloadsUri = MediaStore.Downloads.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val uri = resolver.insert(downloadsUri, contentValues)

        uri?.let {
            resolver.openOutputStream(it)?.use { output ->
                pdfDocument.writeTo(output)
            }
            contentValues.clear()
            contentValues.put(MediaStore.Downloads.IS_PENDING, 0)
            resolver.update(uri, contentValues, null, null)
        }

        pdfDocument.close()
        return uri
    }
}