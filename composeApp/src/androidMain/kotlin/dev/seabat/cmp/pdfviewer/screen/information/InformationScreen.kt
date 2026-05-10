package dev.seabat.cmp.pdfviewer.screen.information

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable

/**
 * Android 用のインフォメーションページ
 * Scaffold + TopAppBar で [InformationContent] をラップする
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScreen(onNavigateBack: () -> Unit) {
    InformationScaffold(onNavigateBack = onNavigateBack)
}
