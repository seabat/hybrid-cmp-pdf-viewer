package dev.seabat.cmp.pdfviewer.shareddata.datasource

import android.content.Context
import android.content.SharedPreferences
import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import org.koin.core.context.GlobalContext

actual class PdfListDataSource actual constructor() : PdfListDataSourceContract {

    private val prefs: SharedPreferences by lazy {
        GlobalContext.get().get<Context>()
            .getSharedPreferences("pdf_list_prefs", Context.MODE_PRIVATE)
    }

    override fun save(pdfList: List<PdfFile>) {
        prefs.edit().apply {
            putInt("pdf_count", pdfList.size)
            pdfList.forEachIndexed { i, file ->
                putString("pdf_${i}_name", file.name)
                putString("pdf_${i}_createdAt", file.createdAt)
                putString("pdf_${i}_size", file.size)
            }
            apply()
        }
    }

    override fun read(): List<PdfFile> {
        val count = prefs.getInt("pdf_count", 0)
        return (0 until count).mapNotNull { i ->
            val name = prefs.getString("pdf_${i}_name", null) ?: return@mapNotNull null
            val createdAt = prefs.getString("pdf_${i}_createdAt", null) ?: return@mapNotNull null
            val size = prefs.getString("pdf_${i}_size", null) ?: return@mapNotNull null
            PdfFile(name = name, createdAt = createdAt, size = size)
        }
    }
}
