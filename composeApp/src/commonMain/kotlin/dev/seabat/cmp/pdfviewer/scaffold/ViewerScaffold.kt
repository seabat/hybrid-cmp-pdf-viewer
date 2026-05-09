package dev.seabat.cmp.pdfviewer.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.seabat.cmp.pdfviewer.content.ViewerContent
import dev.seabat.cmp.pdfviewer.header.ViewerHeader
import dev.seabat.cmp.pdfviewer.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScaffold(fileName: String, onNavigateBack: () -> Unit) {
    Scaffold(
        containerColor = AppColors.contentContainer.toComposeColor(),  // 端末最下部のナビゲーションバーの背景
        topBar = {
            ViewerHeader(fileName = fileName, onNavigateBack = onNavigateBack)
        }
    ) { padding ->
        ViewerContent(
            fileName = fileName,
            modifier = Modifier.padding(padding)
        )
    }
}