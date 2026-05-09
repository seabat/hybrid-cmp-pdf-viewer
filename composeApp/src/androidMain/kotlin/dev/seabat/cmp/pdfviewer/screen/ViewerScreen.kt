package dev.seabat.cmp.pdfviewer.screen

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import dev.seabat.cmp.pdfviewer.content.ViewerContent
import dev.seabat.cmp.pdfviewer.scaffold.ViewerScaffold

/**
 * Android 用のビューアページ
 * Scaffold + TopAppBar(ファイル名) で [ViewerContent] をラップする
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(fileName: String, onNavigateBack: () -> Unit) {
    ViewerScaffold(fileName, onNavigateBack)
}
