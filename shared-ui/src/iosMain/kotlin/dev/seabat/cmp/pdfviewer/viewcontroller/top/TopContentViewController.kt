package dev.seabat.cmp.pdfviewer.viewcontroller.top

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
 * @param sortSheetBridge Swift のネイティブなソートシートと Kotlin ViewModel を橋渡しするオブジェクト
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
        sortSheetBridge.onSortOptionSelected = { field, order ->
            viewModel.onSortOptionSelected(field, order)
        }
        onDispose {
            pdfAddBridge.onAdd = null
            sortSheetBridge.onSortOptionSelected = null
        }
    }

    // Swift 側でシートを開く際に最新の選択状態を読めるよう、ソート条件の変更をブリッジへ反映し続ける
    LaunchedEffect(Unit) {
        viewModel.sortField.collect { sortSheetBridge.currentSortField = it }
    }
    LaunchedEffect(Unit) {
        viewModel.sortOrder.collect { sortSheetBridge.currentSortOrder = it }
    }

    TopContent(onNavigateToViewer = onNavigateToViewer)
}
