package dev.seabat.cmp.pdfviewer.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import dev.seabat.cmp.pdfviewer.theme.AppColors

/**
 * ビューアページのコンテンツ (iOS と Android で共通)
 * 暫定的に "Hello World!" を表示する
 */
@Composable
fun ViewerContent(
    fileName: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(color = AppColors.contentContainer.toComposeColor()).fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hello World!")
    }
}
