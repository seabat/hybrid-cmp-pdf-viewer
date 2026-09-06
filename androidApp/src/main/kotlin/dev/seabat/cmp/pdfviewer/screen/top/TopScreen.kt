package dev.seabat.cmp.pdfviewer.screen.top

import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.seabat.cmp.pdfviewer.theme.AppColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.tooling.preview.Preview
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavEdge
import com.github.skydoves.navgraph.annotations.NavGraphRoot
import com.github.skydoves.navgraph.annotations.NavPreview
import dev.seabat.cmp.pdfviewer.navigation.Screen
import org.koin.compose.viewmodel.koinViewModel

@NavGraphRoot
@NavDestination(route = Screen.Top::class)
@NavEdge(to = Screen.Information::class, label = "インフォメーションへ")
@NavEdge(to = Screen.Viewer::class, label = "ビューアへ")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen(onNavigateToViewer: (String) -> Unit, onNavigateToInformation: () -> Unit) {
    val viewModel: TopViewModel = koinViewModel()
    val context = LocalContext.current
    var showSortSheet by remember { mutableStateOf(false) }

    val pdfPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        var name = "unknown.pdf"
        var sizeBytes = 0L
        var lastModifiedMillis = 0L
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val nameIdx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                val sizeIdx = cursor.getColumnIndex(OpenableColumns.SIZE)
                val modifiedIdx = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED)
                if (nameIdx >= 0) name = cursor.getString(nameIdx) ?: name
                if (sizeIdx >= 0) sizeBytes = cursor.getLong(sizeIdx)
                if (modifiedIdx >= 0) lastModifiedMillis = cursor.getLong(modifiedIdx)
            }
        }
        val size = formatFileSize(sizeBytes)
        val dateSource = if (lastModifiedMillis > 0) Date(lastModifiedMillis) else Date()
        // 固定フォーマット向けに Locale.US と GregorianCalendar を指定することで、
        // 日本語ロケールでも和暦にならず西暦で出力される
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
        formatter.calendar = java.util.GregorianCalendar()
        val createdAt = formatter.format(dateSource)
        viewModel.addPdfFile(sourceUri = uri.toString(), name = name, createdAt = createdAt, size = size)
    }

    if (showSortSheet) {
        val sortField by viewModel.sortField.collectAsStateWithLifecycle()
        val sortOrder by viewModel.sortOrder.collectAsStateWithLifecycle()
        SortBottomSheet(
            sortField = sortField,
            sortOrder = sortOrder,
            onSortOptionSelected = { field, order ->
                viewModel.onSortOptionSelected(field, order)
                showSortSheet = false
            },
            onDismissRequest = { showSortSheet = false }
        )
    }

    Scaffold(
        containerColor = AppColors.contentContainer.toComposeColor(),
        topBar = {
            TopHeader(
                onNavigateToInformation = onNavigateToInformation,
                onAddPdf = { pdfPickerLauncher.launch(arrayOf("application/pdf")) },
                onSortTapped = { showSortSheet = true }
            )
        }
    ) { padding ->
        TopContent(
            onNavigateToViewer = onNavigateToViewer,
            modifier = Modifier.padding(padding)
        )
    }
}

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

@NavPreview(route = Screen.Top::class, primary = true)
@Preview
@Composable
fun TopScreenPreview() {
    TopScreen(onNavigateToViewer = {}, onNavigateToInformation = {})
}

private fun formatFileSize(bytes: Long): String = when {
    bytes >= 1_048_576 -> "%.1f MB".format(bytes / 1_048_576.0)
    bytes >= 1_024 -> "%.1f KB".format(bytes / 1_024.0)
    else -> "$bytes B"
}
