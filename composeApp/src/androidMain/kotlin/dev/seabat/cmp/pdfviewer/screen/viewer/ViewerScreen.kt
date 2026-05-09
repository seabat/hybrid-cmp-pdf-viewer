package dev.seabat.cmp.pdfviewer.screen.viewer

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

/**
 * Android 用のビューアページ
 * Scaffold + TopAppBar(ファイル名) で [ViewerContent] をラップする
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(fileName: String, onNavigateBack: () -> Unit) {
    ViewerScaffold(fileName, onNavigateBack)
}
