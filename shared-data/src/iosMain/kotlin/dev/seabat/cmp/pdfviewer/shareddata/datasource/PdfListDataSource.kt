package dev.seabat.cmp.pdfviewer.shareddata.datasource

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDefaults
import platform.Foundation.NSUserDomainMask

actual class PdfListDataSource actual constructor() : PdfListDataSourceContract {

    private val defaults = NSUserDefaults.standardUserDefaults

    override fun save(pdfList: List<PdfFile>) {
        val docsDir = getDocumentsDirectory()
        defaults.setInteger(pdfList.size.toLong(), forKey = "pdf_count")
        pdfList.forEachIndexed { i, file ->
            defaults.setObject(file.fileName, forKey = "pdf_${i}_name")
            defaults.setObject(file.displayName, forKey = "pdf_${i}_displayName")
            defaults.setObject(file.createdAt, forKey = "pdf_${i}_createdAt")
            defaults.setObject(file.size, forKey = "pdf_${i}_size")
            // コンテナ UUID が変わっても有効なよう Documents からの相対パスで保存する
            val relativePath = file.filePath.removePrefix("$docsDir/")
            defaults.setObject(relativePath, forKey = "pdf_${i}_filePath")
        }
    }

    override fun read(): List<PdfFile> {
        val docsDir = getDocumentsDirectory()
        val count = defaults.integerForKey("pdf_count").toInt()
        return (0 until count).mapNotNull { i ->
            val fileName = defaults.stringForKey("pdf_${i}_name") ?: return@mapNotNull null
            val displayName = defaults.stringForKey("pdf_${i}_displayName") ?: ""
            val createdAt = defaults.stringForKey("pdf_${i}_createdAt") ?: return@mapNotNull null
            val size = defaults.stringForKey("pdf_${i}_size") ?: return@mapNotNull null
            val relativePath = defaults.stringForKey("pdf_${i}_filePath") ?: ""
            // 実行時の Documents ディレクトリを基準に絶対パスを復元する
            val filePath = if (relativePath.isNotEmpty()) "$docsDir/$relativePath" else ""
            PdfFile(fileName = fileName, displayName = displayName, createdAt = createdAt, size = size, filePath = filePath)
        }
    }

    private fun getDocumentsDirectory(): String =
        NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, true).first() as String
}
