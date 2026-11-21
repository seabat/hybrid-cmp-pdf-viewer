package dev.seabat.cmp.pdfviewer.shareddomain.repository

class FakePdfThumbnailRepository : PdfThumbnailRepositoryContract {
    private val storage = mutableMapOf<String, ByteArray>()

    fun register(filePath: String, png: ByteArray) {
        storage[filePath] = png
    }

    override suspend fun renderFirstPage(filePath: String, widthPx: Int): ByteArray? =
        storage[filePath]
}
