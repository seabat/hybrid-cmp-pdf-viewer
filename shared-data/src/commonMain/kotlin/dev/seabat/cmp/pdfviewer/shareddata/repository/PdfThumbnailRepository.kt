package dev.seabat.cmp.pdfviewer.shareddata.repository

import dev.seabat.cmp.pdfviewer.shareddata.datasource.PdfThumbnailDataSourceContract
import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfThumbnailRepositoryContract

class PdfThumbnailRepository(
    private val dataSource: PdfThumbnailDataSourceContract
) : PdfThumbnailRepositoryContract {
    override suspend fun renderFirstPage(filePath: String, widthPx: Int): ByteArray? =
        dataSource.renderFirstPage(filePath, widthPx)
}
