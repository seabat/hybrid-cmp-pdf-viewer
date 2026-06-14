package dev.seabat.cmp.pdfviewer.screen.top

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import dev.seabat.cmp.pdfviewer.theme.AppColors
import dev.seabat.cmp.pdfviewer.sharedui.generated.resources.Res
import dev.seabat.cmp.pdfviewer.sharedui.generated.resources.top_header
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopHeader(
    onNavigateToInformation: () -> Unit,
    onAddPdf: () -> Unit = {}
) {
    TopAppBar(
        // CMP 1.11.x から ComposeUIViewController 内で TopAppBar がステータスバー分の
        // 上インセットを自動適用するようになり、iOS の frame(height: 64) 枠外に
        // コンテンツが押し出されるため、インセットを 0 に上書きする
        windowInsets = WindowInsets(0),
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = AppColors.headerContainer.toComposeColor(),
            titleContentColor = AppColors.headerContent.toComposeColor(),
            actionIconContentColor = AppColors.headerContent.toComposeColor()
        ),
        title = { Text("PDF ビューア") },
        actions = {
            IconButton(onClick = onAddPdf) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "PDF を追加"
                )
            }
            IconButton(onClick = onNavigateToInformation) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "インフォメーション"
                )
            }
        }
    )
}