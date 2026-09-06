package dev.seabat.cmp.pdfviewer.viewcontroller.top

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.window.ComposeUIViewController
import dev.seabat.cmp.pdfviewer.screen.top.TopHeader

/**
 * iOS 向けのトップページヘッダーの ViewController
 * Kotlin Composable の TopHeader を ComposeUIViewController でラップして公開する
 */
fun TopHeaderViewController(
    onNavigateToInformation: () -> Unit,
    onAddPdf: () -> Unit = {},
    onSortTapped: () -> Unit = {}
) = ComposeUIViewController {
    TopHeader(
        onNavigateToInformation = onNavigateToInformation,
        onAddPdf = onAddPdf,
        onSortTapped = onSortTapped,
        // CMP 1.11.x 以降、ComposeUIViewController 内で TopAppBar がステータスバー分の
        // 上インセットを自動適用するため、iOS の frame(height: 64) 枠外へ押し出されないよう 0 に上書き
        windowInsets = WindowInsets(0)
    )
}