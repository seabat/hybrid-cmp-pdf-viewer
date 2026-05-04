package dev.seabat.cmp.pdfviewer.viewcontroller

import androidx.compose.ui.window.ComposeUIViewController
import dev.seabat.cmp.pdfviewer.content.ViewerContent

/**
 * iOS 向けのビューアページ ViewController
 * Kotlin Composable の ViewerContent を ComposeUIViewController でラップして公開する
 *
 * @param fileName 表示対象のファイル名
 */
fun ViewerContentViewController(fileName: String) =
    ComposeUIViewController {
        ViewerContent(fileName = fileName)
    }
