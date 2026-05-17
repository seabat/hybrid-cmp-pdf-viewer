package dev.seabat.cmp.pdfviewer.viewcontroller.top

import androidx.compose.ui.window.ComposeUIViewController
import dev.seabat.cmp.pdfviewer.screen.top.TopHeader

/**
 * iOS 向けのトップページヘッダーの ViewController
 * Kotlin Composable の TopHeader を ComposeUIViewController でラップして公開する
 */
fun TopHeaderViewController(
    onNavigateToInformation: () -> Unit,
    onAddPdf: () -> Unit = {}
) = ComposeUIViewController {
    TopHeader(
        onNavigateToInformation = onNavigateToInformation,
        onAddPdf = onAddPdf
    )
}