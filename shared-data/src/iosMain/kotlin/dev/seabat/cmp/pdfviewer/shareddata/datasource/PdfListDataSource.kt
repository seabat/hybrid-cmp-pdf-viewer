package dev.seabat.cmp.pdfviewer.shareddata.datasource

import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import platform.Foundation.NSUserDefaults

actual class PdfListDataSource actual constructor() : PdfListDataSourceContract {

    private val defaults = NSUserDefaults.standardUserDefaults

    override fun save(pdfList: List<PdfFile>) {
        defaults.setInteger(pdfList.size.toLong(), forKey = "pdf_count")
        pdfList.forEachIndexed { i, file ->
            defaults.setObject(file.name, forKey = "pdf_${i}_name")
            defaults.setObject(file.createdAt, forKey = "pdf_${i}_createdAt")
            defaults.setObject(file.size, forKey = "pdf_${i}_size")
        }
    }

    override fun read(): List<PdfFile> {
        val count = defaults.integerForKey("pdf_count").toInt()
        return (0 until count).mapNotNull { i ->
            val name = defaults.stringForKey("pdf_${i}_name") ?: return@mapNotNull null
            val createdAt = defaults.stringForKey("pdf_${i}_createdAt") ?: return@mapNotNull null
            val size = defaults.stringForKey("pdf_${i}_size") ?: return@mapNotNull null
            PdfFile(name = name, createdAt = createdAt, size = size)
        }
    }
}
