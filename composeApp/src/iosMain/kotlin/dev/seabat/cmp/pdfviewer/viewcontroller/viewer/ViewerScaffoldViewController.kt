package dev.seabat.cmp.pdfviewer.viewcontroller.viewer

import androidx.compose.ui.window.ComposeUIViewController
import dev.seabat.cmp.pdfviewer.screen.viewer.ViewerScaffold

/**
 * iOS 向けのビューアページ ViewController
 * Kotlin Composable の ViewerScaffold を ComposeUIViewController でラップして公開する
 *
 * @param fileName 表示対象のファイル名
 */
fun ViewerScaffoldViewController(fileName: String, onNavigateBack: () -> Unit) =
    ComposeUIViewController {
        ViewerScaffold(fileName = fileName, onNavigateBack = onNavigateBack)
    }