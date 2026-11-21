package dev.seabat.cmp.pdfviewer.shareddomain.usecase

import dev.seabat.cmp.pdfviewer.shareddomain.repository.PdfThumbnailRepositoryContract

class RenderPdfThumbnailUseCase(
    private val pdfThumbnailRepository: PdfThumbnailRepositoryContract
) : RenderPdfThumbnailUseCaseContract {
    override suspend fun invoke(filePath: String, widthPx: Int): ByteArray? {
        if (filePath.isEmpty()) return null
        return pdfThumbnailRepository.renderFirstPage(filePath, widthPx)
    }
}
