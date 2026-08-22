package dev.seabat.cmp.pdfviewer.screen.top

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.theme.AppColors
import dev.seabat.cmp.pdfviewer.sharedui.generated.resources.Res
import dev.seabat.cmp.pdfviewer.sharedui.generated.resources.top_created_at
import dev.seabat.cmp.pdfviewer.sharedui.generated.resources.top_size
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * トップページのコンテンツ (iOS と Android で共通)
 * ファイル一覧を表示し、タップで [onNavigateToViewer] を呼び出す
 */
@Composable
fun TopContent(
    onNavigateToViewer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: TopViewModel = koinViewModel()
    val pdfList by viewModel.pdfList.collectAsStateWithLifecycle()

    TopContent(
        pdfList = pdfList,
        onNavigateToViewer = onNavigateToViewer,
        onDeleteFile = viewModel::deletePdfFile,
        modifier = modifier
    )
}

/** ViewModel に依存しない描画専用の実装（Preview から直接呼び出せる） */
@Composable
private fun TopContent(
    pdfList: List<PdfFile>,
    onNavigateToViewer: (String) -> Unit,
    onDeleteFile: (PdfFile) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.background(AppColors.contentContainer.toComposeColor()).fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(pdfList, key = { it.fileName }) { file ->
            PdfFileItem(
                file = file,
                onClick = { onNavigateToViewer(file.fileName) },
                onDelete = { onDeleteFile(file) }
            )
        }
    }
}

@Preview
@Composable
fun TopContentPreview() {
    TopContent(
        pdfList = listOf(
            PdfFile(
                fileName = "sample1.pdf",
                displayName = "サンプル1",
                createdAt = "2026-08-01 10:00",
                size = "1.2MB"
            ),
            PdfFile(
                fileName = "sample2.pdf",
                displayName = "サンプル2",
                createdAt = "2026-08-10 15:30",
                size = "3.4MB"
            )
        ),
        onNavigateToViewer = {},
        onDeleteFile = {}
    )
}

/** PDF ファイル一覧の各アイテム。右から左にスワイプすると [onDelete] を呼び出す */
@Composable
private fun PdfFileItem(file: PdfFile, onClick: () -> Unit, onDelete: () -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState()

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        onDismiss = { direction ->
            if (direction == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
            }
        },
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CardDefaults.shape)
                    .background(Color.Red)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "削除",
                    tint = Color.White
                )
            }
        }
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = file.displayName.ifEmpty { file.fileName },
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "作成日時: ${file.createdAt}",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "サイズ: ${file.size}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
