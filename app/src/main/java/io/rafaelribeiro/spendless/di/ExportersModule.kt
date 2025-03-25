package io.rafaelribeiro.spendless.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.rafaelribeiro.spendless.service.CSVTransactionExporter
import io.rafaelribeiro.spendless.service.PDFTransactionExporter
import io.rafaelribeiro.spendless.service.TransactionExporter
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
abstract class ExportersModule {
    @Binds
    @Named("csv")
    abstract fun bindCsvExporter(impl: CSVTransactionExporter): TransactionExporter

    @Binds
    @Named("pdf")
    abstract fun bindPdfExporter(impl: PDFTransactionExporter): TransactionExporter
}