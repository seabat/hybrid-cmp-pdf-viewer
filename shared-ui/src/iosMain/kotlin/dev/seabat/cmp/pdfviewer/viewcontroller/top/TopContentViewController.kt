package dev.seabat.cmp.pdfviewer.viewcontroller.top

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.seabat.cmp.pdfviewer.screen.top.TopContent
import dev.seabat.cmp.pdfviewer.screen.top.TopViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * iOS 向けのトップページ ViewController
 * Kotlin Composable の TopContent を ComposeUIViewController でラップして公開する
 *
 * @param onNavigateToViewer ファイルをタップしたとき、および他アプリから受け取った PDF の
 *   追加が完了したときに呼ばれるコールバック（引数: ファイル名）
 * @param pdfAddBridge Swift の PDF 取り込みと Kotlin ViewModel を橋渡しするオブジェクト
 * @param sortSheetBridge TopHeader の並び替えボタンと Kotlin ViewModel を橋渡しするオブジェクト
 */
fun TopContentViewController(
    onNavigateToViewer: (String) -> Unit,
    pdfAddBridge: PdfAddBridge,
    sortSheetBridge: SortSheetBridge
) = ComposeUIViewController {
    val viewModel: TopViewModel = koinViewModel()

    DisposableEffect(Unit) {
        pdfAddBridge.onAdd = { sourceUrl, name, createdAt, size, openAfterAdd ->
            viewModel.addPdfFile(
                sourceUri = sourceUrl,
                name = name,
                createdAt = createdAt,
                size = size,
                openAfterAdd = openAfterAdd
            )
        }
        sortSheetBridge.onOpen = {
            viewModel.showSortSheet()
        }
        onDispose {
            pdfAddBridge.onAdd = null
            sortSheetBridge.onOpen = null
        }
    }

    // 他アプリから渡された PDF の保存完了後に Viewer へ遷移する
    val pendingViewerFileName by viewModel.pendingViewerFileName.collectAsStateWithLifecycle()
    LaunchedEffect(pendingViewerFileName) {
        val fileName = pendingViewerFileName ?: return@LaunchedEffect
        viewModel.consumePendingViewerFileName()
        onNavigateToViewer(fileName)
    }

    TopContent(onNavigateToViewer = onNavigateToViewer)
}
