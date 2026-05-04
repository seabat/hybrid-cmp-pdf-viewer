package dev.seabat.cmp.pdfviewer.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.seabat.cmp.pdfviewer.content.TopContent
import dev.seabat.cmp.pdfviewer.header.TopHeader
import dev.seabat.cmp.pdfviewer.theme.AppColors

/**
 * Android 用のトップページ
 * Scaffold + TopAppBar で [TopContent] をラップする
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen(onNavigateToViewer: (String) -> Unit) {
    Scaffold(
        containerColor = AppColors.contentContainer.toComposeColor(),  // 端末最下部のナビゲーションバーの背景
        topBar = {
            TopHeader()
        }
    ) { padding ->
        TopContent(
            onNavigateToViewer = onNavigateToViewer,
            modifier = Modifier.padding(padding)
        )
    }
}
