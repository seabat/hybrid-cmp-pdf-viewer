package dev.seabat.cmp.pdfviewer.shareddomain.repository

interface PdfThumbnailRepositoryContract {
    suspend fun renderFirstPage(filePath: String, widthPx: Int): ByteArray?
}
