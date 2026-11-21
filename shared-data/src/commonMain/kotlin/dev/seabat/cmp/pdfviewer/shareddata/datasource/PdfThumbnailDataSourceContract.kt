package dev.seabat.cmp.pdfviewer.shareddata.datasource

interface PdfThumbnailDataSourceContract {
    /**
     * PDF の 1 ページ目をサムネイル画像 (PNG) として生成する
     *
     * @param filePath PDF ファイルの絶対パス
     * @param widthPx 生成する画像の幅 (px)。高さはページの縦横比から算出する
     * @return PNG 形式の ByteArray。ファイルが存在しない場合や生成に失敗した場合は null
     */
    suspend fun renderFirstPage(filePath: String, widthPx: Int): ByteArray?
}
