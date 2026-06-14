package dev.seabat.cmp.pdfviewer.shareddata.datasource

interface PdfFileDataSourceContract {
    suspend fun copyToInternalStorage(sourceUri: String, destFileName: String): String
}