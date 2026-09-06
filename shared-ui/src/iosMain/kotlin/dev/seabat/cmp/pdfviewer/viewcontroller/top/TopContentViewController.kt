package dev.seabat.cmp.pdfviewer.viewcontroller.top

import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.ComposeUIViewController
import dev.seabat.cmp.pdfviewer.screen.top.TopContent
import dev.seabat.cmp.pdfviewer.screen.top.TopViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * iOS 向けのトップページ ViewController
 * Kotlin Composable の TopContent を ComposeUIViewController でラップして公開する
 *
 * @param onNavigateToViewer ファイルをタップしたときに呼ばれるコールバック（引数: ファイル名）
 * @param pdfAddBridge Swift のドキュメントピッカーと Kotlin ViewModel を橋渡しするオブジェクト
 * @param sortSheetBridge TopHeader の並び替えボタンと Kotlin ViewModel を橋渡しするオブジェクト
 */
fun TopContentViewController(
    onNavigateToViewer: (String) -> Unit,
    pdfAddBridge: PdfAddBridge,
    sortSheetBridge: SortSheetBridge
) = ComposeUIViewController {
    val viewModel: TopViewModel = koinViewModel()

    DisposableEffect(Unit) {
        pdfAddBridge.onAdd = { sourceUrl, name, createdAt, size ->
            viewModel.addPdfFile(sourceUri = sourceUrl, name = name, createdAt = createdAt, size = size)
        }
        sortSheetBridge.onOpen = {
            viewModel.showSortSheet()
        }
        onDispose {
            pdfAddBridge.onAdd = null
            sortSheetBridge.onOpen = null
        }
    }

    TopContent(onNavigateToViewer = onNavigateToViewer)
}
