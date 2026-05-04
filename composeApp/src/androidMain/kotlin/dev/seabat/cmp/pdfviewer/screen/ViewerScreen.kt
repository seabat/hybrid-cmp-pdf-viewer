package dev.seabat.cmp.pdfviewer.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.seabat.cmp.pdfviewer.content.ViewerContent

/**
 * Android 用のビューアページ
 * Scaffold + TopAppBar(ファイル名) で [ViewerContent] をラップする
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScreen(fileName: String) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(fileName) })
        }
    ) { padding ->
        ViewerContent(
            fileName = fileName,
            modifier = Modifier.padding(padding)
        )
    }
}
