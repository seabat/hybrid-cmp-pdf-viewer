package dev.seabat.cmp.pdfviewer.screen.top

import android.net.Uri
import android.provider.DocumentsContract
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import dev.seabat.cmp.pdfviewer.theme.AppColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.koin.compose.viewmodel.koinViewModel

/**
 * Android 用のトップページ
 * Scaffold + TopAppBar で [TopContent] をラップする
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopScreen(onNavigateToViewer: (String) -> Unit, onNavigateToInformation: () -> Unit) {
    val viewModel: TopViewModel = koinViewModel()
    val context = LocalContext.current

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

    Scaffold(
        containerColor = AppColors.contentContainer.toComposeColor(),
        topBar = {
            TopHeader(
                onNavigateToInformation = onNavigateToInformation,
                onAddPdf = { pdfPickerLauncher.launch(arrayOf("application/pdf")) }
            )
        }
    ) { padding ->
        TopContent(
            onNavigateToViewer = onNavigateToViewer,
            modifier = Modifier.padding(padding)
        )
    }
}

private fun formatFileSize(bytes: Long): String = when {
    bytes >= 1_048_576 -> "%.1f MB".format(bytes / 1_048_576.0)
    bytes >= 1_024 -> "%.1f KB".format(bytes / 1_024.0)
    else -> "$bytes B"
}
