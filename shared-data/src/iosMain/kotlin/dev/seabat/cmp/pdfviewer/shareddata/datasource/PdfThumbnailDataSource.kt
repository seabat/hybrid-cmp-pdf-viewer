@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package dev.seabat.cmp.pdfviewer.shareddata.datasource

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.CoreGraphics.CGSizeMake
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.PDFKit.PDFDocument
import platform.PDFKit.kPDFDisplayBoxMediaBox
import platform.UIKit.UIImagePNGRepresentation
import platform.posix.memcpy

actual class PdfThumbnailDataSource actual constructor() : PdfThumbnailDataSourceContract {

    override suspend fun renderFirstPage(filePath: String, widthPx: Int): ByteArray? =
        withContext(Dispatchers.Default) {
            if (!NSFileManager.defaultManager.fileExistsAtPath(filePath)) return@withContext null

            val url = NSURL.fileURLWithPath(filePath)
            val document = PDFDocument(uRL = url) ?: return@withContext null
            val page = document.pageAtIndex(0u) ?: return@withContext null

            // ページの縦横比を保ったまま指定幅に収める
            val bounds = page.boundsForBox(kPDFDisplayBoxMediaBox)
            val (pageWidth, pageHeight) = bounds.useContents { size.width to size.height }
            if (pageWidth <= 0.0 || pageHeight <= 0.0) return@withContext null
            val heightPx = (widthPx * pageHeight / pageWidth).toInt().coerceAtLeast(1)

            val image = page.thumbnailOfSize(
                size = CGSizeMake(widthPx.toDouble(), heightPx.toDouble()),
                forBox = kPDFDisplayBoxMediaBox
            )
            UIImagePNGRepresentation(image)?.toByteArray()
        }
}

/** NSData を Kotlin の ByteArray へコピーする */
private fun NSData.toByteArray(): ByteArray {
    val size = length.toInt()
    if (size == 0) return ByteArray(0)
    return ByteArray(size).apply {
        usePinned { pinned -> memcpy(pinned.addressOf(0), bytes, length) }
    }
}
