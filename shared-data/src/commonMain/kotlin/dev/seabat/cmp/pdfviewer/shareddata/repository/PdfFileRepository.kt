package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.datasource.PdfFileDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfFileRepositoryContract

class PdfFileRepository(
    private val dataSource: PdfFileDataSourceContract
) : PdfFileRepositoryContract {
    override suspend fun copyToInternalStorage(sourceUri: String, destFileName: String): String =
        dataSource.copyToInternalStorage(sourceUri, destFileName)
}