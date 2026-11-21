package dev.seabat.cmp.pdfviewer.shareddata.datasource

class FakePdfThumbnailDataSource : PdfThumbnailDataSourceContract {
    /** 存在するものとして扱うファイルパスと、返すダミー PNG バイト列 */
    private val storage = mutableMapOf<String, ByteArray>()

    fun register(filePath: String, png: ByteArray) {
        storage[filePath] = png
    }

    override suspend fun renderFirstPage(filePath: String, widthPx: Int): ByteArray? =
        storage[filePath]
}
