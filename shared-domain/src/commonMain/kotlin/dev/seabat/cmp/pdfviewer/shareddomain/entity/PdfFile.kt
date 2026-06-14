package dev.seabat.cmp.pdfviewer.shareddomain.entity

/** PDF ファイル情報 */
data class PdfFile(
    val fileName: String,
    val displayName: String = "",
    val createdAt: String,
    val size: String,
    val filePath: String = ""
)
