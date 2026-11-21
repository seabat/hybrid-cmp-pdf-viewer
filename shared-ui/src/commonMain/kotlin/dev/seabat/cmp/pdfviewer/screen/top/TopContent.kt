package dev.seabat.cmp.pdfviewer.screen.top

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
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
    val isSortSheetVisible by viewModel.isSortSheetVisible.collectAsStateWithLifecycle()
    val sortField by viewModel.sortField.collectAsStateWithLifecycle()
    val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
    val thumbnails by viewModel.thumbnails.collectAsStateWithLifecycle()

    TopContent(
        pdfList = pdfList,
        thumbnails = thumbnails,
        onNavigateToViewer = onNavigateToViewer,
        onDeleteFile = viewModel::deletePdfFile,
        isSortSheetVisible = isSortSheetVisible,
        sortField = sortField,
        sortOrder = sortOrder,
        onSortOptionSelected = viewModel::selectSortOption,
        onDismissSortSheet = viewModel::dismissSortSheet,
        modifier = modifier
    )
}

/** ViewModel に依存しない描画専用の実装（Preview から直接呼び出せる） */
@Composable
private fun TopContent(
    pdfList: List<PdfFile>,
    thumbnails: Map<String, ImageBitmap> = emptyMap(),
    onNavigateToViewer: (String) -> Unit,
    onDeleteFile: (PdfFile) -> Unit,
    isSortSheetVisible: Boolean = false,
    sortField: SortField = SortField.DATE,
    sortOrder: SortOrder = SortOrder.DESC,
    onSortOptionSelected: (SortField, SortOrder) -> Unit = { _, _ -> },
    onDismissSortSheet: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        BoxWithConstraints(
            modifier = Modifier.background(AppColors.contentContainer.toComposeColor()).fillMaxSize()
        ) {
            // 幅が広い場合（横向きやタブレット）は 2 列で表示する
            val columns = if (maxWidth >= GRID_BREAKPOINT) 2 else 1
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(pdfList, key = { it.fileName }) { file ->
                    PdfFileItem(
                        file = file,
                        thumbnail = thumbnails[file.fileName],
                        onClick = { onNavigateToViewer(file.fileName) },
                        onDelete = { onDeleteFile(file) }
                    )
                }
            }
        }

        if (isSortSheetVisible) {
            SortBottomSheet(
                sortField = sortField,
                sortOrder = sortOrder,
                onSortOptionSelected = onSortOptionSelected,
                onDismissRequest = onDismissSortSheet
            )
        }
    }
}

@Preview
@Composable
fun TopContentPreview() {
    TopContent(
        pdfList = previewPdfList(),
        onNavigateToViewer = {},
        onDeleteFile = {}
    )
}

/** 幅が広い場合に 2 列になることを確認する Preview */
@Preview(widthDp = 800, heightDp = 400)
@Composable
fun TopContentWidePreview() {
    TopContent(
        pdfList = previewPdfList(),
        onNavigateToViewer = {},
        onDeleteFile = {}
    )
}

private fun previewPdfList(): List<PdfFile> = listOf(
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
)

/** 並び替え設定用のボトムシート。項目（日付/ファイル名）と向き（昇順/降順）を選択するとすぐに適用される */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortBottomSheet(
    sortField: SortField,
    sortOrder: SortOrder,
    onSortOptionSelected: (SortField, SortOrder) -> Unit,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(text = "並び替え", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "項目", style = MaterialTheme.typography.labelLarge)
            SortOptionRow(
                label = "日付",
                selected = sortField == SortField.DATE,
                onClick = { onSortOptionSelected(SortField.DATE, sortOrder) }
            )
            SortOptionRow(
                label = "ファイル名",
                selected = sortField == SortField.NAME,
                onClick = { onSortOptionSelected(SortField.NAME, sortOrder) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "順番", style = MaterialTheme.typography.labelLarge)
            SortOptionRow(
                label = "昇順",
                selected = sortOrder == SortOrder.ASC,
                onClick = { onSortOptionSelected(sortField, SortOrder.ASC) }
            )
            SortOptionRow(
                label = "降順",
                selected = sortOrder == SortOrder.DESC,
                onClick = { onSortOptionSelected(sortField, SortOrder.DESC) }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SortOptionRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = selected, onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}

@Preview
@Composable
fun SortOptionRowPreview() {
    // 選択済み / 未選択の両方を並べて確認する
    Column {
        SortOptionRow(label = "日付", selected = true, onClick = {})
        SortOptionRow(label = "ファイル名", selected = false, onClick = {})
    }
}

/**
 * PDF ファイル一覧の各アイテム。右から左にスワイプすると [onDelete] を呼び出す
 *
 * @param thumbnail 1 ページ目のサムネイル。生成前・生成失敗時は null
 */
@Composable
private fun PdfFileItem(
    file: PdfFile,
    thumbnail: ImageBitmap?,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
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
            // SwipeToDismissBox は背景を常に描画するため、スワイプ中以外は何も描画しない。
            // これによりカードの背後から赤が覗くのを防ぐ（Preview の初回描画で露出する問題の対策）
            val isSwiping = dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CardDefaults.shape)
                    .background(if (isSwiping) Color.Red else Color.Transparent)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                if (isSwiping) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "削除",
                        tint = Color.White
                    )
                }
            }
        }
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
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
                Spacer(modifier = Modifier.width(12.dp))
                PdfThumbnail(thumbnail = thumbnail)
            }
        }
    }
}

/** 1 ページ目のサムネイル。生成前・生成失敗時は白い枠のみを表示する */
@Composable
private fun PdfThumbnail(thumbnail: ImageBitmap?) {
    Box(
        modifier = Modifier
            .size(width = THUMBNAIL_WIDTH, height = THUMBNAIL_HEIGHT)
            .background(Color.White)
            .border(width = 1.dp, color = Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (thumbnail != null) {
            Image(
                bitmap = thumbnail,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
    }
}

private val THUMBNAIL_WIDTH = 48.dp
private val THUMBNAIL_HEIGHT = 64.dp

/** この幅以上で一覧を 2 列にする。Material のレスポンシブ指針の medium 幅に合わせている */
private val GRID_BREAKPOINT = 600.dp

@Preview
@Composable
fun PdfFileItemPreview() {
    // 一覧と同じグレー背景の上に配置し、白いカードの境界が分かるようにする
    Box(
        modifier = Modifier
            .background(AppColors.contentContainer.toComposeColor())
            .padding(16.dp)
    ) {
        PdfFileItem(
            thumbnail = null,
            file = PdfFile(
                fileName = "sample1.pdf",
                displayName = "サンプル1",
                createdAt = "2026-08-01 10:00",
                size = "1.2MB"
            ),
            onClick = {},
            onDelete = {}
        )
    }
}
