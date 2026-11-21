package dev.seabat.cmp.pdfviewer.shareddomain.usecase

interface RenderPdfThumbnailUseCaseContract {
    /**
     * PDF の 1 ページ目のサムネイル画像 (PNG) を生成する
     *
     * @return PNG 形式の ByteArray。パスが空、または生成に失敗した場合は null
     */
    suspend operator fun invoke(filePath: String, widthPx: Int): ByteArray?
}
