package dev.seabat.cmp.pdfviewer.shareddata.datasource

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File

actual class PdfThumbnailDataSource actual constructor() : PdfThumbnailDataSourceContract {

    override suspend fun renderFirstPage(filePath: String, widthPx: Int): ByteArray? =
        withContext(Dispatchers.IO) {
            val file = File(filePath)
            if (!file.exists()) return@withContext null

            runCatching {
                ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use { fd ->
                    PdfRenderer(fd).use { renderer ->
                        if (renderer.pageCount == 0) return@use null
                        renderer.openPage(0).use { page ->
                            // ページの縦横比を保ったまま指定幅に収める
                            val scale = widthPx.toFloat() / page.width
                            val heightPx = (page.height * scale).toInt().coerceAtLeast(1)
                            val bitmap = Bitmap.createBitmap(widthPx, heightPx, Bitmap.Config.ARGB_8888)
                            // PDF の余白は透明で描画されるため、白で塗りつぶしてから描画する
                            bitmap.eraseColor(Color.WHITE)
                            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                            val png = ByteArrayOutputStream().use { stream ->
                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                                stream.toByteArray()
                            }
                            bitmap.recycle()
                            png
                        }
                    }
                }
            }.getOrNull()
        }
}
