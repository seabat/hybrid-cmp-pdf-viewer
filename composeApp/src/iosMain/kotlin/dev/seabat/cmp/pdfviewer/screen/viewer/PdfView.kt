package dev.seabat.cmp.pdfviewer.screen.viewer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import kotlinx.cinterop.ExperimentalForeignApi
import platform.PDFKit.PDFDocument
import platform.PDFKit.PDFView
import platform.PDFKit.kPDFDisplaySinglePageContinuous
import platform.Foundation.NSURL

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun PdfView(filePath: String, modifier: Modifier) {
    UIKitView(
        factory = {
            val pdfView = PDFView()
            pdfView.autoScales = true
            pdfView.displayMode = kPDFDisplaySinglePageContinuous
            val url = NSURL.fileURLWithPath(filePath)
            pdfView.document = PDFDocument(url)
            pdfView
        },
        modifier = modifier
    )
}