@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package dev.seabat.cmp.pdfviewer.shareddata.datasource

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSUserDomainMask
import platform.Foundation.NSURL
import platform.Foundation.dataWithContentsOfURL
import platform.Foundation.writeToFile
import platform.Foundation.NSUUID

actual class PdfFileDataSource actual constructor() : PdfFileDataSourceContract {

    override suspend fun copyToInternalStorage(sourceUri: String, destFileName: String): String =
        withContext(Dispatchers.Default) {
            val docsDir = NSSearchPathForDirectoriesInDomains(
                NSDocumentDirectory, NSUserDomainMask, true
            ).first() as String
            val pdfsDir = "$docsDir/pdfs"
            NSFileManager.defaultManager.createDirectoryAtPath(
                pdfsDir, withIntermediateDirectories = true, attributes = null, error = null
            )
            val uniqueName = "${NSUUID().UUIDString}_$destFileName"
            val destPath = "$pdfsDir/$uniqueName"
            val sourceUrl = NSURL.URLWithString(sourceUri) ?: error("無効な URL: $sourceUri")
            val data = NSData.dataWithContentsOfURL(sourceUrl) ?: error("PDF の読み込みに失敗: $sourceUri")
            data.writeToFile(destPath, atomically = true)
            destPath
        }
}