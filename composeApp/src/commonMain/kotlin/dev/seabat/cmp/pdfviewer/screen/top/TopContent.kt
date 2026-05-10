package dev.seabat.cmp.pdfviewer.screen.top

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.seabat.cmp.pdfviewer.shareddomain.entity.PdfFile
import dev.seabat.cmp.pdfviewer.shareddomain.entity.samplePdfFiles
import dev.seabat.cmp.pdfviewer.theme.AppColors

/**
 * トップページのコンテンツ (iOS と Android で共通)
 * ファイル一覧を表示し、タップで [onNavigateToViewer] を呼び出す
 */
@Composable
fun TopContent(
    onNavigateToViewer: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.background(AppColors.contentContainer.toComposeColor()).fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(samplePdfFiles) { file ->
            PdfFileItem(
                file = file,
                onClick = { onNavigateToViewer(file.name) }
            )
        }
    }
}

/** PDF ファイル一覧の各アイテム */
@Composable
private fun PdfFileItem(file: PdfFile, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = file.name,
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
