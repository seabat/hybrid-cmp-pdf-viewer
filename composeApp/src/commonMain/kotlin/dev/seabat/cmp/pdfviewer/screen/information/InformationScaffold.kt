package dev.seabat.cmp.pdfviewer.screen.information

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.seabat.cmp.pdfviewer.theme.AppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InformationScaffold(
    onNavigateBack: () -> Unit,
    onShowVersionAlert: () -> Unit = {},
    onShowBioAuth: () -> Unit = {}
) {
    Scaffold(
        containerColor = AppColors.contentContainer.toComposeColor(),  // 端末最下部のナビゲーションバーの背景
        topBar = {
            InformationHeader(onNavigateBack = onNavigateBack)
        }
    ) { padding ->
        InformationContent(
            modifier = Modifier.padding(padding),
            onVersionTapped = onShowVersionAlert,
            onShowBioAuth = onShowBioAuth
        )
    }
}
