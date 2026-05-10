package dev.seabat.cmp.pdfviewer.shareddomain.entity

/** PDF ファイル情報 */
data class PdfFile(
    val name: String,
    val createdAt: String,
    val size: String
)

/** 開発段階のサンプルデータ（暫定） */
val samplePdfFiles = listOf(
    PdfFile(name = "sample1.pdf", createdAt = "2025-01-15 10:30", size = "1.2 MB"),
    PdfFile(name = "sample2.pdf", createdAt = "2025-02-20 14:45", size = "3.5 MB"),
    PdfFile(name = "report.pdf", createdAt = "2025-03-10 09:00", size = "512 KB"),
    PdfFile(name = "manual.pdf", createdAt = "2025-04-05 16:20", size = "8.7 MB"),
)
