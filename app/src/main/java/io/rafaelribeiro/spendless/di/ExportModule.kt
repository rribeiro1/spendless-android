package io.rafaelribeiro.spendless.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.service.CsvTransactionExportService
import io.rafaelribeiro.spendless.service.PdfTransactionExportService
import io.rafaelribeiro.spendless.service.TransactionExportService
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class ExportModule {
    @Binds
    @Named("csv")
    abstract fun bindCsvExporter(impl: CsvTransactionExportService): TransactionExportService

    @Binds
    @Named("pdf")
    abstract fun bindPdfExporter(impl: PdfTransactionExportService): TransactionExportService
}