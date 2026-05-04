package dev.seabat.cmp.pdfviewer.viewcontroller

import androidx.compose.ui.window.ComposeUIViewController
import dev.seabat.cmp.pdfviewer.content.TopContent
import dev.seabat.cmp.pdfviewer.header.TopHeader

/**
 * iOS 向けのトップページヘッダーの ViewController
 * Kotlin Composable の TopHeader を ComposeUIViewController でラップして公開する
 */
fun TopHeaderViewController() =
    ComposeUIViewController {
        TopHeader()
    }