package dev.seabat.cmp.pdfviewer.shareddomain.repository

interface PdfFileRepositoryContract {
    suspend fun copyToInternalStorage(sourceUri: String, destFileName: String): String
}