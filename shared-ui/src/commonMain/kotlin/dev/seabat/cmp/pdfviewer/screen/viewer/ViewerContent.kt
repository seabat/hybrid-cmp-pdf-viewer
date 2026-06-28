package dev.seabat.cmp.pdfviewer.screen.viewer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.tooling.preview.Preview
import dev.seabat.cmp.pdfviewer.theme.AppColors
import org.koin.compose.viewmodel.koinViewModel

/**
 * ビューアページのコンテンツ (iOS と Android で共通)
 * fileName をキーに ViewerViewModel が filePath を解決し PdfView でレンダリングする
 */
@Composable
fun ViewerContent(
    fileName: String,
    modifier: Modifier = Modifier
) {
    val viewModel: ViewerViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(fileName) {
        viewModel.loadPdf(fileName)
    }

    Box(
        modifier = modifier
            .background(AppColors.contentContainer.toComposeColor())
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is ViewerUiState.Loading -> CircularProgressIndicator()
            is ViewerUiState.Success -> PdfView(
                filePath = state.filePath,
                modifier = Modifier.fillMaxSize()
            )
            is ViewerUiState.Error -> Text("PDF を読み込めませんでした")
        }
    }
}

@Preview
@Composable
fun ViewerContentPreview() {
    ViewerContent(fileName = "sample.pdf")
}