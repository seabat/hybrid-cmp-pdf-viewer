package dev.seabat.cmp.pdfviewer.shareddata.datasource

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.context.GlobalContext
import java.io.File
import java.util.UUID

actual class PdfFileDataSource actual constructor() : PdfFileDataSourceContract {

    private val context: Context by lazy { GlobalContext.get().get() }

    override suspend fun copyToInternalStorage(sourceUri: String, destFileName: String): String =
        withContext(Dispatchers.IO) {
            val destDir = File(context.filesDir, "pdfs").also { it.mkdirs() }
            val uniqueName = "${UUID.randomUUID()}_$destFileName"
            val destFile = File(destDir, uniqueName)
            context.contentResolver.openInputStream(Uri.parse(sourceUri))?.use { input ->
                destFile.outputStream().use { output -> input.copyTo(output) }
            }
            destFile.absolutePath
        }
}