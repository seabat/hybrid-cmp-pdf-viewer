package dev.seabat.cmp.pdfviewer.screen.viewer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.CoreGraphics.CGRectMake
import platform.Foundation.NSData
import platform.Foundation.NSFileManager
import platform.Foundation.dataWithContentsOfFile
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFView
import platform.PDFKit.kPDFDisplaySinglePageContinuous
import platform.UIKit.UIView

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PdfView(filePath: String, modifier: Modifier) {
    UIKitView<UIView>(
        factory = {
            val pdfView: PDFView? = PDFView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0))
            pdfView?.apply {
                autoScales = true
                displayMode = kPDFDisplaySinglePageContinuous
            } ?: UIView(frame = CGRectMake(0.0, 0.0, 0.0, 0.0))
        },
        update = { view ->
            if (NSFileManager.defaultManager.fileExistsAtPath(filePath)) {
                (view as? PDFView)?.let { pdfView ->
                    val data: NSData? = NSData.dataWithContentsOfFile(filePath)
                    val doc: PDFDocument? = data?.let { PDFDocument(data = it) }
                    doc?.let { pdfView.document = it }
                }
            }
        },
        modifier = modifier
    )
}
