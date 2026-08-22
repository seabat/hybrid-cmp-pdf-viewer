package dev.seabat.cmp.pdfviewer.shareddata.datasource

interface PdfFileDataSourceContract {
    suspend fun copyToInternalStorage(sourceUri: String, destFileName: String): String
    suspend fun delete(filePath: String)
}