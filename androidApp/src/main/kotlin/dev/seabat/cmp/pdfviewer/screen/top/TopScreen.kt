package dev.seabat.cmp.pdfviewer.screen.top

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.seabat.cmp.pdfviewer.theme.AppColors
import androidx.compose.ui.tooling.preview.Preview
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavEdge
import com.github.skydoves.navgraph.annotations.NavGraphRoot
import com.github.skydoves.navgraph.annotations.NavPreview
import dev.seabat.cmp.pdfviewer.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel

/**
 * トップページ
 *
 * @param incomingPdfUri 他アプリから「開く」「共有」で渡された PDF の Uri 文字列。
 *   受け取ったら一覧へ追加して Viewer を開く
 * @param onIncomingPdfConsumed [incomingPdfUri] の処理が済んだことを通知するコールバック。
 *   同じ Uri が再処理されないよう呼び出し側で保持をクリアする
 */
@NavGraphRoot
@NavDestination(route = Screen.Top::class)
@NavEdge(to = Screen.Information::class, label = "インフォメーションへ")
@NavEdge(to = Screen.Viewer::class, label = "ビューアへ")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen(
    onNavigateToViewer: (String) -> Unit,
    onNavigateToInformation: () -> Unit,
    incomingPdfUri: String? = null,
    onIncomingPdfConsumed: () -> Unit = {}
) {
    val viewModel: TopViewModel = koinViewModel()
    val context = LocalContext.current

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        val metadata = readPdfUriMetadata(context, uri)
        viewModel.addPdfFile(
            sourceUri = uri.toString(),
            name = metadata.name,
            createdAt = metadata.createdAt,
            size = metadata.size
        )
    }

    // 他アプリから渡された PDF を一覧へ追加する
    LaunchedEffect(incomingPdfUri) {
        val uriString = incomingPdfUri ?: return@LaunchedEffect
        val metadata = readPdfUriMetadata(context, Uri.parse(uriString))
        viewModel.addPdfFile(
            sourceUri = uriString,
            name = metadata.name,
            createdAt = metadata.createdAt,
            size = metadata.size,
            openAfterAdd = true
        )
        onIncomingPdfConsumed()
    }

    // 他アプリから渡された PDF の保存完了後に Viewer へ遷移する
    val pendingViewerFileName by viewModel.pendingViewerFileName.collectAsState()
    LaunchedEffect(pendingViewerFileName) {
        val fileName = pendingViewerFileName ?: return@LaunchedEffect
        viewModel.consumePendingViewerFileName()
        onNavigateToViewer(fileName)
    }

    Scaffold(
        containerColor = AppColors.contentContainer.toComposeColor(),
        topBar = {
            TopHeader(
                onNavigateToInformation = onNavigateToInformation,
                onAddPdf = { pdfPickerLauncher.launch(arrayOf("application/pdf")) },
                onSortTapped = viewModel::showSortSheet
            )
        }
    ) { padding ->
        TopContent(
            onNavigateToViewer = onNavigateToViewer,
            modifier = Modifier.padding(padding)
        )
    }
}

@NavPreview(route = Screen.Top::class, primary = true)
@Preview
@Composable
fun TopScreenPreview() {
    TopScreen(onNavigateToViewer = {}, onNavigateToInformation = {})
}
