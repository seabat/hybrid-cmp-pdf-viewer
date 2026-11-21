package dev.seabat.cmp.pdfviewer.screen.top

import dev.seabat.cmp.pdfviewer.shareddomain.usecase.RenderPdfThumbnailUseCaseContract

/** サムネイルは生成しない（null を返す）Fake。ViewModel のテストでは画像のデコードを行わない */
class FakeRenderPdfThumbnailUseCase : RenderPdfThumbnailUseCaseContract {
    override suspend fun invoke(filePath: String, widthPx: Int): ByteArray? = null
}
