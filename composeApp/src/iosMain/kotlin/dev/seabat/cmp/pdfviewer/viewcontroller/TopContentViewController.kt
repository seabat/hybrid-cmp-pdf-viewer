package dev.seabat.cmp.pdfviewer.viewcontroller

import androidx.compose.ui.window.ComposeUIViewController
import dev.seabat.cmp.pdfviewer.content.TopContent

/**
 * iOS 向けのトップページ ViewController
 * Kotlin Composable の TopContent を ComposeUIViewController でラップして公開する
 *
 * @param onNavigateToViewer ファイルをタップしたときに呼ばれるコールバック（引数: ファイル名）
 */
fun TopContentViewController(onNavigateToViewer: (String) -> Unit) =
    ComposeUIViewController {
        TopContent(onNavigateToViewer = onNavigateToViewer)
    }
