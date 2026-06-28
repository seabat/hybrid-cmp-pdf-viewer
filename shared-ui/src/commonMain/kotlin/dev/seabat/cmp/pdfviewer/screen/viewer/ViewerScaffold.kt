package dev.seabat.cmp.pdfviewer.screen.viewer

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.skydoves.navgraph.annotations.NavDestination
import com.github.skydoves.navgraph.annotations.NavPreview
import dev.seabat.cmp.pdfviewer.navigation.Screen
import dev.seabat.cmp.pdfviewer.theme.AppColors
import org.jetbrains.compose.ui.tooling.preview.Preview

@NavDestination(route = Screen.Viewer::class)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewerScaffold(fileName: String, onNavigateBack: () -> Unit) {
    Scaffold(
        containerColor = AppColors.contentContainer.toComposeColor(),  // 端末最下部のナビゲーションバーの背景
        topBar = {
            ViewerHeader(fileName = fileName, onNavigateBack = onNavigateBack)
        }
    ) { padding ->
        ViewerContent(
            fileName = fileName,
            modifier = Modifier.padding(padding)
        )
    }
}

@NavPreview(route = Screen.Viewer::class, primary = true)
@Preview
@Composable
fun ViewerScaffoldPreview() {
    ViewerScaffold(fileName = "sample.pdf", onNavigateBack = {})
}